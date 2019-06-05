package ejektaflex.scalingwealth

import com.google.gson.GsonBuilder
import ejektaflex.scalingwealth.dsl.DataDSL
import ejektaflex.scalingwealth.struct.Interval
import java.io.File


println("Generating JSON data..")

val i = Interval("[2, 4]")

println(Math.PI in i)

val mc = "minecraft"
val em = "embers"
val ba = "betteranimalsplus"
val mm = "mowziesmobs"

var gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .setPrettyPrinting()
        .registerTypeAdapter(Interval::class.java, Interval.IntervalAdapter())
        .create()

val table = DataDSL().apply {


    forMob("$mc:blaze") {
        drop(30, "$mc:nether_wart", 0.1)
        drop(60, "$mc:blaze_powder", 0.15)
        drop(90, "$mc:blaze_rod", 0.04)
        drop(120, "botania:quartz:2", 0.15)
        drop(150, "embers:shard_ember", 0.1)
        drop(180, "embers:crystal_ember", 0.05)
        drop(210, "botania:blazeblock", 0.01)
        drop(240, "botania:rune:1", 0.01)
    }

    forMob("$ba:brownbear") {
        drop(25, "minecraft:wool:12", 0.1)
    }

    forMob("$mc:creeper") {
        drop(25, "$mc:fire_charge", 0.1, 1..2)
        drop(50, "forestry:propolis", 0.12, 1..2)
        drop(75, "forestry:fertilizer_compound", 0.07, 1..2)
        drop(150, "$mc:tnt", 0.1)
        drop(200, "tconstruct:throwball:1", 0.1)
    }

    forMob("mysticalwildlife:dusk_lurker") {
        drop(0, "$mc:rotten_flesh", 0.1)
        drop(30, "quark:arrow_torch", 0.1)
        drop(60, "embers:dust_ash", 0.1, 1..3)

    }

    forMob("$mc:enderman") {
        drop(30, "$mc:pumpkin_pie", 0.1)
        drop(60, "quark:arrow_ender", 0.15)
        drop(90, "minecraft:obsidian", 0.08)
        drop(120, "$mc:ender_eye", 0.1)
        drop(150, "quark:biotite", 0.1)
        drop(180, "biomesoplenty:gem", 0.08) // ender amethyst
        drop(210, "forestry:ingot_copper", 0.05)
        drop(240, "forestry:thermionic_tubes:12", 0.1) // ender electron tube
    }

    forMob("$mc:ender_dragon") {
        drop(50, "$mc:elytra", 1.0, 1..3)
    }

    forMob("$ba:feralwolf") {
        drop(30, "minecraft:wool", 0.2)
    }

    // TESTING
    forMob("$mm:foliaath") {
        drop(0, "minecraft:fern", 0.8, 1..4)
    }

    forMob("$mm:lantern") {
        drop(25, "minecraft:glowstone_dust", 1.0, 1..3)
        drop(50, "minecraft:glowstone", 0.3)
        drop(75, "twilightforest:torchberries", 0.3)
    }

    forMob("$mm:naga") {
        drop(25, "tconstruct:punji", 0.5)
        drop(40, "minecraft:bone", 0.8, 1..7)
    }

    forMob("$mm:frostmaw") {

        drop(25, "forestry:crafting_material:5", 0.35, 1..3) // Ice shard
        drop(35, "forestry:bee_combs:4", 0.25, 1..2) // Frozen Comb
        drop(100, "cookingforblockheads:fridge", 0.35)


        drop(25, "minecraft:diamond", 0.35)
        drop(75, "minecraft:diamond", 0.35)
        drop(125, "minecraft:diamond", 0.35)
        drop(175, "minecraft:diamond", 0.35)
        drop(225, "minecraft:diamond", 0.35)


    }

    forMob("$mc:ghast") {
        drop(50, "tconstruct:nuggets", 0.25)
        drop(50, "tconstruct:nuggets", 0.25)
    }

    forMob("$mm:grottol") {
        drop(25, "minecraft:redstone", 1.0, 1..3)
        drop(50, "minecraft:emerald", 0.5)
        drop(75, "biomesoplenty:gem:1", 0.08)
        drop(100, "biomesoplenty:gem:2", 0.08)
        drop(125, "biomesoplenty:gem:3", 0.08)
        drop(150, "biomesoplenty:gem:4", 0.08)
        drop(175, "biomesoplenty:gem:5", 0.08)
        drop(200, "biomesoplenty:gem:6", 0.08)
        drop(225, "biomesoplenty:gem:7", 0.08)
    }

    forMob("$mc:guardian") {
        drop(25, "harvestcraft:fishsticksitem", 0.75)
        drop(50, "minecraft:prismarine_crystals", 0.9, 1..5)
    }

    forMob("$mc:magma_cube") {
        drop(25, "$mc:magma", 0.15)
        drop(50, "forestry:bee_combs:2", 0.25) // Simmering comb
        drop(75, "tconstruct:slime_sapling:2", 0.15)
    }

    forMobs("$mc:skeleton", "$mc:stray") {
        drop(15, "$mc:feather", 0.1)
        drop(30, "$mc:dye:15", 0.15)
        drop(60, "$mc:flint", 0.08)
        drop(90, "$mc:bone_block", 0.08)
        drop(120, "$mc:iron_nugget", 0.08)
        drop(150, "quark:arrow_explosive", 0.10)
    }

    forMob("$mc:spider") {
        drop(30, "forestry:crafting_material:2", 0.1, 1..4)
        drop(60, "$mc:dye", 0.1)
        drop(90, "minecraft:slime_ball", 0.025)
        drop(120, "forestry:propolis", 0.1)
        drop(150, "forestry:propolis:1", 0.1)
        //drop(150, "forestry:propolis:1", 0.1)
    }

    forMob("biomesoplenty:wasp") {
        drop(25, "forestry:bee_combs", 0.4) // Honey
        drop(50, "forestry:bee_combs:2", 0.3) // Simmering
    }

    forMob("$mc:witch") {
        drop(25, "$mc:glass_bottle", 0.8, 1..2)
        drop(50, "$mc:nether_wart", 0.8, 1..3)
    }

    forMob("$mc:wither_skeleton") {
        drop(25, "quark:black_ash", 0.1)
        drop(50, "quark:basalt", 0.1)
        drop(25, "quark:biome_cobblestone", 0.1) // brimstone
        drop(150, "$mc:spectral_arrow", 0.1)
    }

    forMob("$mc:zombie") {
        drop(15, "$mc:carrot", 0.08)
        drop(30, "$mc:torch", 0.06)
        drop(30, "$em:nugget_copper", 0.02)
        drop(45, "$mc:ladder", 0.02)
        drop(60, "$mc:coal:1", 0.08)
        drop(75, "$mc:stone_button", 0.02)
        drop(90, "$mc:leather", 0.008)

        drop(105, "$mc:spruce_fence", 0.02)

        drop(135, "$mc:clay_ball", 0.02)

        drop(165, "$mc:glass_bottle", 0.02)

        drop(195, "harvestcraft:saltitem", 0.03)

        drop(225, "tconstruct:edible:3", 0.01) // Coag Slime
        drop(50, "harvestcraft:zombiejerkyitem", 0.25)

    }

    forMob("$mc:zombie_pigman") {
        drop(25, "$mc:quartz", 0.08)
        drop(75, "$mc:golden_carrot", 0.1)
        drop(125, "tconstruct:nuggets:4", 0.2, 1..2)
    }

    // Only put common drops here!
    forEveryMob {

        drop(50, "scalinghealth:difficultychanger:1", 0.005)

    }


}.also {
    val toWrite = gson.toJson(it.root)
    val location = File("K:\\MultiMC\\instances\\Donut\\.minecraft\\config\\scalingwealth")
    val dest = File(location, "drops.json")
    dest.writeText(toWrite)
}
