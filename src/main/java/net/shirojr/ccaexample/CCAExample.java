package net.shirojr.ccaexample;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import net.shirojr.ccaexample.init.CCAExampleBlockEntities;
import net.shirojr.ccaexample.init.CCAExampleBlocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CCAExample implements ModInitializer {
	public static final String MOD_ID = "ccaexample";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CCAExampleBlocks.initialize();
		CCAExampleBlockEntities.initialize();

		LOGGER.info("Taking a look at Cardinal Components API, eh?");
	}

	public static Identifier getId(String path) {
		return Identifier.of(MOD_ID, path);
	}
}