package net.shirojr.ccaexample.init;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.shirojr.ccaexample.CCAExample;
import net.shirojr.ccaexample.block.entity.ItemHolderBlockEntity;

public interface CCAExampleBlockEntities {
    BlockEntityType<ItemHolderBlockEntity> ITEM_HOLDER = register("item_holder", ItemHolderBlockEntity::new, CCAExampleBlocks.ITEM_HOLDER);


    @SuppressWarnings("SameParameterValue")
    private static  <T extends BlockEntity> BlockEntityType<T> register(String name,
                                                                        BlockEntityType.BlockEntityFactory<? extends T> factory,
                                                                        Block... blocks) {
        Identifier id = CCAExample.getId(name);
        BlockEntityType.Builder<T> builder = BlockEntityType.Builder.create(factory, blocks);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, builder.build());
    }

    static void initialize() {
        // static initialisation
    }
}
