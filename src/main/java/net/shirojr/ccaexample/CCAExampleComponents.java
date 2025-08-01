package net.shirojr.ccaexample;

import net.shirojr.ccaexample.block.entity.ItemHolderBlockEntity;
import net.shirojr.ccaexample.cca.component.SyncedBlockInventoryComponent;
import net.shirojr.ccaexample.cca.implementation.SyncedBlockEntityInventoryComponentImpl;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class CCAExampleComponents implements BlockComponentInitializer {
    public static final ComponentKey<SyncedBlockInventoryComponent> SYNCED_BLOCK_INVENTORY =
            ComponentRegistry.getOrCreate(SyncedBlockInventoryComponent.IDENTIFIER, SyncedBlockInventoryComponent.class);

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(ItemHolderBlockEntity.class, SYNCED_BLOCK_INVENTORY, SyncedBlockEntityInventoryComponentImpl::new);
    }
}
