package ejektaflex.bountiful.api.item

import ejektaflex.bountiful.api.logic.IBountyData
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.registries.IForgeRegistryEntry

interface IItemBounty : IForgeRegistryEntry<Item> {

    /**
     * Returns the underlying IBountyData stored in the stack's NBT data.
     */
    fun getBountyData(stack: ItemStack): IBountyData

    /**
     * When given a bounty ItemStack, this method attempts to expire the amount of bountyTime it has left on the board.
     */
    fun tryExpireBoardTime(stack: ItemStack)

    /**
     * Decrements the amount of bountyTime left on the bounty. Returns true if it's run out.
     */
    fun tickBountyTime(stack: ItemStack, world: World): Boolean

    /**
     * Decrements the amount of bountyTime left on the board. Returns true if it's run out.
     */
    fun tickBoardTime(stack: ItemStack): Boolean

    /**
     * When given an ItemStack of ItemBounty, this method ensures that the stack has bounty NBT data.
     */
    fun ensureBounty(stack: ItemStack)

}