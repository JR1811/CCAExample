package net.shirojr.ccaexample;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.shirojr.ccaexample.datagen.CCAExampleModelProvider;
import net.shirojr.ccaexample.datagen.CCAExampleTagsProviders;

public class CCAExampleDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider(CCAExampleModelProvider::new);
		CCAExampleTagsProviders.registerAll(pack);
	}
}
