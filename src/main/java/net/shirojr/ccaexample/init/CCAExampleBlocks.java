package net.shirojr.ccaexample.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.shirojr.ccaexample.CCAExample;
import net.shirojr.ccaexample.block.ItemHolderBlock;

@SuppressWarnings("unused")
public interface CCAExampleBlocks {
    ItemHolderBlock ITEM_HOLDER = register("item_holder", new ItemHolderBlock(AbstractBlock.Settings.create()), true);

    @SuppressWarnings("SameParameterValue")
    private static <T extends Block> T register(String name, T entry, boolean registerDefaultItem) {
        Identifier id = CCAExample.getId(name);
        T registeredEntry = Registry.register(Registries.BLOCK, id, entry);
        if (registerDefaultItem) {
            Registry.register(Registries.ITEM, id, new BlockItem(registeredEntry, new Item.Settings()));
        }
        return registeredEntry;
    }

    static void initialize() {
        // static initialisation
    }
}
