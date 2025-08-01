package net.shirojr.ccaexample.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.shirojr.ccaexample.CCAExample;
import net.shirojr.ccaexample.init.CCAExampleBlocks;

public class CCAExampleModelProvider extends FabricModelProvider {
    public CCAExampleModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.blockStateCollector.accept(
                BlockStateModelGenerator.createSingletonBlockState(CCAExampleBlocks.ITEM_HOLDER, CCAExample.getId("block/item_holder"))
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
