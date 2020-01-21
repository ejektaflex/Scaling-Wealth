package ejektaflex.scalingwealth.proxy

import ejektaflex.scalingwealth.AttributeContainer
import ejektaflex.scalingwealth.ScalingWealth
import ejektaflex.scalingwealth.ext.registryName
import ejektaflex.scalingwealth.ext.toPretty
import net.minecraft.block.BlockTorch
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.ai.attributes.IAttributeInstance
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.silentchaos512.scalinghealth.api.ScalingHealthAPI
import net.silentchaos512.scalinghealth.world.ScalingHealthSavedData
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random


open class CommonProxy : IProxy {

    override fun postInit(e: FMLPostInitializationEvent) {
        ScalingWealth.warnErrors()
    }

    private fun Double.clamp(min: Double, max: Double): Double = Math.max(min, Math.min(this, max))

    @SubscribeEvent
    fun onEntityDies(e: LivingDeathEvent) {

        if (e.entityLiving is EntityPlayer) {
            (e.entityLiving as EntityPlayer).let {
                val theDeadTorchbearer = torchbearers.keys.find { tb -> tb.displayNameString == it.displayNameString }

                if (theDeadTorchbearer != null) {
                    println("Removed torchbearer because of death: ${theDeadTorchbearer.displayNameString}")
                    torchbearers.remove(it)
                }

            }
        }

        /*
        if (e.entity is EntityPlayer && e.source?.trueSource !is EntityPlayer && e.source?.trueSource is EntityLivingBase) {
            val playerDiff = ScalingHealthAPI.getPlayerDifficulty(e.entity as EntityPlayer)
            val mobDiff = ScalingHealthAPI.getEntityDifficulty(e.source!!.trueSource as EntityLivingBase)
            var lossOnDeath = 6.0 / (0.75 + (max(0.0, mobDiff - playerDiff) / 20.0))
            lossOnDeath = lossOnDeath.clamp(2.0, 5.0)
            println("Diffs: $playerDiff, $mobDiff")
            println("Player ${e.entityLiving} lost $lossOnDeath difficulty!")
            ScalingHealthAPI.addPlayerDifficulty(e.entity as EntityPlayer, max(-playerDiff, -lossOnDeath))
        }

        if (e.entity !is EntityPlayer && e.entity is EntityLivingBase && e.source?.trueSource is EntityPlayer) {
            val player = e.source.trueSource as EntityPlayer
            val playerDiff = ScalingHealthAPI.getPlayerDifficulty(player)
            val mobDiff = ScalingHealthAPI.getEntityDifficulty(e.entityLiving)
            var proto = 0.4
            if (playerDiff == 0.0) {
                proto *= 1.4
            }

            // 20% diff boost when mob has 50% or higher difficulty
            proto *= when {
                // Reward no difficulty for killing passive mobs
                e.entityLiving is IAnimals -> 0.0
                // Avoid div-by-zero
                playerDiff == 0.0 -> 1.2
                // Apply bonus above 50% increase
                mobDiff / playerDiff >= 1.5 -> 1.2
                // Normal scaling
                else -> 1.0
            }

            println("Giving $player $proto difficulty!")
            ScalingHealthAPI.addPlayerDifficulty(player, proto)

        }

         */

    }

    fun getGroupsFor(eName: String): List<String> {
        return ScalingWealth.drops.groups.filter { eName in it.value }.map { it.key }
    }

    fun handleDropsFor(eName: String, e: LivingDropsEvent, diff: Double, luck: Int) {
        if (eName in ScalingWealth.drops.entities) {
            val dropTable = ScalingWealth.drops.entities[eName]!!
            val validPools = dropTable.filter { diff in it.key }
            for (pool in validPools) {
                for (definition in pool.value) {

                    val shouldBeBlight = definition.mustBeBlight ?: false

                    val passesBlightCheck = (shouldBeBlight && ScalingHealthAPI.isBlight(e.entityLiving))
                            || definition.mustBeBlight == null

                    val poolCoord = pool.key.coords
                    val scaledChance = definition.chance + ((poolCoord.second - poolCoord.first) * definition.scaleChance)
                    val fortunifiedLuckChance = scaledChance * (1 + (0.25 * luck))
                    println("Fortunified: $scaledChance -> $fortunifiedLuckChance")

                    if (passesBlightCheck) {
                        val stack = definition.toItemStack()
                        if (stack == null) {
                            ScalingWealth.logger.warn("Item not found!: $definition")
                        } else {
                            if (Random.nextDouble() <= fortunifiedLuckChance) {
                                e.entityLiving.entityDropItem(stack, 0.5f)
                            } else {
                                ScalingWealth.logger.debug("Did not drop ${stack.displayName}, not not meet chance")
                            }
                        }
                    }


                }
            }
        }
    }

    fun EntityPlayer.giveEffect(eid: Int, amp: Int) {
        val toGive = PotionEffect(Potion.getPotionById(eid) ?: Potion.getPotionById(1)!!, 50, amp)
        this.addPotionEffect(toGive)
    }

    val dist = 5f

    var torchbearers: MutableMap<EntityPlayer, Map<Int, Array<Float>>> = mutableMapOf()


    private fun getTorchEffects(player: EntityPlayer): Map<Int, Array<Float>> {
        val maps = player.heldEquipment.map { item -> item.toPretty }
                .filter { it in ScalingWealth.drops.validTorches.keys }
                .mapNotNull { ScalingWealth.drops.validTorches[it] }

        val retMap = mutableMapOf<Int, Array<Float>>()
        for (map in maps) {
            for (key: Int in map.keys) {
                retMap[key] = map[key] ?: error("Oh noes, $key was not found in map")
            }
        }

        return retMap
    }



    // ### Modified Spawn & Despawn Rules (Same;

    @SubscribeEvent
    open fun onSpawn(event: CheckSpawn) {
        event.result = Event.Result.DENY

        for (player in event.world.playerEntities) {
            val distDiff = abs(event.y - player.posY)
            if (event.entityLiving.getDistance(player) <= 32f && distDiff < 2.5f) {
                if (Random.nextFloat() < ScalingWealth.spawnChance) {
                    event.result = Event.Result.DEFAULT
                }
            }
        }

    }

    private fun changeAttribute(entity: EntityLiving, inst: AttributeContainer, numForMult: Double) {
        val attr: IAttributeInstance = entity.getEntityAttribute(inst.attr)
        attr.let { att ->

            val newMax = att.baseValue * numForMult
            att.baseValue = newMax
        }
    }



    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onEntityJoinWorldLast(event: EntityJoinWorldEvent) {

        if (event.entity is EntityLiving) {

            (event.entity as EntityLiving).let {

                val monsterAttributes = ScalingWealth.drops.fx[it.registryName.toString()]

                if (monsterAttributes != null) {
                    println("Modifying attributes for: ${it.registryName.toString()}")

                    for ((attr, v) in monsterAttributes) {
                        changeAttribute(it, attr, v)
                    }

                }

            }

        }

    }

    @SubscribeEvent
    open fun onCheckDespawn(event: LivingSpawnEvent.AllowDespawn) {
        if (event.entityLiving is IMob) {
            var isOut = true
            for (player in event.world.playerEntities) {
                val distDiff = abs(event.y - player.posY)
                isOut = if (event.entityLiving.getDistance(player) > 56f || distDiff >= 3f) {
                    isOut && true
                } else {
                    false
                }
            }
            if (isOut) {
                if (event.entityLiving.registryName.toString() in ScalingWealth.drops.doNotDespawn) {
                    println("Allowed ${event.entityLiving.registryName.toString()} to live")
                    event.result = Event.Result.DENY
                } else {
                    event.result = Event.Result.ALLOW
                }
            }
        }
    }

    // ### Torchbearer archetype effects

    @SubscribeEvent
    fun applyTorchEffects(e: TickEvent.WorldTickEvent) {
        if ((e.world?.totalWorldTime ?: 0L) % 20L != 10L) {
            return
        }

        println("Current torchbearers: ${torchbearers.keys.map { it.displayNameString }}")

        if (torchbearers.size == 1) {


            val theTorchbearer = torchbearers.keys.first()
            val effects = torchbearers.values.first()

            //println("Only one torchbearer, giving effects to: ${theTorchbearer.displayNameString}")

            // Torchbearer only effect
            //theTorchbearer.giveEffect(21)

            for (player in e.world.playerEntities) {

                for (effect in effects) {

                    if (player.getDistance(theTorchbearer) <= effect.value[0]) {

                        if (player.displayNameString == theTorchbearer.displayNameString) {
                            if (effect.key !in ScalingWealth.drops.unshared) {
                                val amp = effect.value[1].toInt()
                                player.giveEffect(effect.key, amp)
                            }
                        } else {
                            val amp = effect.value[1].toInt()
                            player.giveEffect(effect.key, amp)
                        }

                    }

                }


            }

        } else if (torchbearers.size >= 2) {

            for (falseBearer in torchbearers.keys.drop(1)) {
                falseBearer.giveEffect(20, 1)
            }

        }

    }


    @SubscribeEvent
    fun targetChange(e: LivingSetAttackTargetEvent) {
        if (e.target !is EntityPlayer) {
            if (e.entityLiving.registryName.toString() in ScalingWealth.drops.forcePlayerTargeting) {
                val players = e.entityLiving.world.playerEntities
                if (players.size > 0) {
                    val closestPlayer = players.minBy { e.entityLiving.getDistance(it) }!!
                    println("Locking onto: ${closestPlayer.displayNameString}")
                    (e.entityLiving as EntityLiving).attackTarget = closestPlayer

                }
            }
        }
    }

    @SubscribeEvent
    fun setTorchbearer(e: LivingEvent.LivingUpdateEvent) {
        if (e.entity?.world?.isRemote == true || e.entityLiving !is EntityPlayer) {
            return
        }
        if ((e.entity?.world?.totalWorldTime ?: 0L) % 20L != 0L) {
            return
        }

        (e.entityLiving as EntityPlayer).let {
            val holdingStuff = it.heldEquipment.map { item -> item.toPretty }
            if (holdingStuff.any { name -> name in ScalingWealth.drops.validTorches.keys }) {
                //println("Setting torchbearer effects for ${it.displayNameString}")
                torchbearers[it] = getTorchEffects(it)
            } else {
                torchbearers.remove(it)
            }
        }

    }


    @SubscribeEvent
    fun entityDrops(e: LivingDropsEvent) {

        if (e.entity?.world?.isRemote == true) {
            return
        }

        // Choose how to calculate difficulty
        val diff = when (ScalingWealth.config.difficultyBasedOnPlayer && e.source.trueSource is EntityPlayer) {
            true -> ScalingHealthAPI.getPlayerDifficulty(e.source.trueSource as EntityPlayer)
            false -> ScalingHealthAPI.getEntityDifficulty(e.entityLiving)
        }

        val playerSrc = e.source.trueSource as? EntityPlayer

        val bonusLuck = playerSrc?.luck?.toInt() ?: 0

        //println("Giving $bonusLuck due to bonus luck!")

        val entityKey = e.entityLiving.registryName.toString()
        handleDropsFor("*", e, diff, bonusLuck)
        handleDropsFor(entityKey, e, diff, bonusLuck)
        getGroupsFor(entityKey).forEach { groupName ->
            handleDropsFor(groupName, e, diff, bonusLuck)
        }
    }

    @SubscribeEvent
    fun lightTorches(e: PlayerInteractEvent.RightClickBlock) {
        val player = e.entityPlayer
        if (torchbearers.keys.size == 1 && torchbearers.keys.first() == player) {
            if (e.world.getBlockState(e.pos)?.block?.registryName?.toString() == "thebetweenlands:damp_torch"
                    && e.itemStack.toPretty == "minecraft:coal") {
                val oldFace = e.world.getBlockState(e.pos).getValue(BlockTorch.FACING)
                e.world.setBlockState(e.pos,
                        Blocks.TORCH.defaultState.withProperty(BlockTorch.FACING, oldFace)
                )
                e.itemStack.shrink(1)
            }
        }
    }

    private fun changeDiff(world: World, amt: ScalingHealthSavedData.() -> Double) {
        val data = ScalingHealthSavedData.get(world)
        val newAmount = min(250.0, max(0.0, data.difficulty + amt(data)))
        data.difficulty = newAmount
        data.markDirty()
    }

    @SubscribeEvent
    fun decreaseDiffOnDeath(e: LivingDeathEvent) {
        if (e.entityLiving is EntityPlayer) {
            changeDiff(e.entityLiving.world) {
                //min(difficulty * ScalingWealth.deathLossPercent, ScalingWealth.deathLoss)
                val diffHere = ScalingHealthSavedData.get(e.entityLiving.world).difficulty

                (diffHere * -0.01) - ScalingWealth.deathLoss
            }
        } else {
            changeDiff(e.entityLiving.world) { ScalingWealth.deathGain }
        }

    }




}