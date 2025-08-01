package net.shirojr.ccaexample.cca.component;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.shirojr.ccaexample.CCAExample;
import net.shirojr.ccaexample.CCAExampleComponents;
import net.shirojr.ccaexample.block.entity.ItemHolderBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.function.Consumer;

// IMPORTANT! extend from the ticking component here instead of implementing it on the Impl class, to expose ticking for the registration!
public interface SyncedBlockInventoryComponent extends Component, CommonTickingComponent {
    Identifier IDENTIFIER = CCAExample.getId("synced_block_inventory");    // <-- needs to show up in fabric.mod.json

    static SyncedBlockInventoryComponent fromBlockEntity(ItemHolderBlockEntity blockEntity) {
        return CCAExampleComponents.SYNCED_BLOCK_INVENTORY.get(blockEntity);
    }

    long getAge();

    DefaultedList<ItemStack> getInventory();

    void modifyInventory(Consumer<DefaultedList<ItemStack>> consumer, boolean shouldSync);

    boolean add(boolean shouldSync, ItemStack stack);

    @Nullable
    ItemStack remove(boolean shouldSync);

    default void clear(boolean shouldSync) {
        modifyInventory(DefaultedList::clear, shouldSync);
    }

    default boolean isFull() {
        for (ItemStack stack : getInventory()) {
            if (stack.isEmpty()) return false;
        }
        return true;
    }

    default boolean hasContent() {
        for (ItemStack entry : getInventory()) {
            if (!entry.isEmpty()) return true;
        }
        return false;
    }
}
