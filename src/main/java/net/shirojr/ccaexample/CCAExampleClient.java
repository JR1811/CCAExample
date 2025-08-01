package net.shirojr.ccaexample;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.shirojr.ccaexample.block.client.ItemHolderBlockEntityRenderer;
import net.shirojr.ccaexample.init.CCAExampleBlockEntities;

public class CCAExampleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(CCAExampleBlockEntities.ITEM_HOLDER, ItemHolderBlockEntityRenderer::new);
    }
}
