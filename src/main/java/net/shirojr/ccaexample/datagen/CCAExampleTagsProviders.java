package net.shirojr.ccaexample.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.registry.RegistryWrapper;
import net.shirojr.ccaexample.init.CCAExampleTags;

import java.util.concurrent.CompletableFuture;

public class CCAExampleTagsProviders {
    public static class ItemTagsProvider extends FabricTagProvider.ItemTagProvider {
        public ItemTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            getOrCreateTagBuilder(CCAExampleTags.ItemTags.DISPLAYABLE)
                    .addOptionalTag(ConventionalItemTags.CROPS)
                    .addOptionalTag(ConventionalItemTags.ARMORS)
                    .addOptionalTag(ConventionalItemTags.MELEE_WEAPON_TOOLS)
                    .addOptionalTag(ConventionalItemTags.RANGED_WEAPON_TOOLS);
        }
    }

    public static void registerAll(FabricDataGenerator.Pack pack) {
        pack.addProvider(ItemTagsProvider::new);
    }
}
