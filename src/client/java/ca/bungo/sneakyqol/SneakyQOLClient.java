package ca.bungo.sneakyqol;

import ca.bungo.sneakyqol.settings.KeybindHandling;
import ca.bungo.sneakyqol.utility.ResourceExtractor;
import ca.bungo.sneakyqol.vtt.EventHandlerVTT;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;

public class SneakyQOLClient implements ClientModInitializer {

	public static final String MOD_NAMESPACE = "ca.bungo.sneakyqol";

	@Override
	public void onInitializeClient() {

		/*
		// Get the config directory
		File configDir = new File(FabricLoader.getInstance().getConfigDir().toString(), "sneakyqol");
		// Initialize the VoiceToText feature
		VoiceToText.initialize(configDir);*/


		//Starting Keybind Handling:
		KeybindHandling.initializeKeybindHandler();

		//Features
		//new LeanFeature();

		try {
			File configDir = new File(FabricLoader.getInstance().getConfigDir().toString(), "sneakyqol");
			File modelDir = new File(configDir, "vosk-model");
			if(!modelDir.exists()){
				ResourceExtractor.copyDirectoryFromResources("models/vosk-model.zip", modelDir);
			}
			EventHandlerVTT.register();
		} catch (IOException e){
			e.printStackTrace();
			SneakyQOL.LOGGER.error("Failed to extract Vosk Model! Not Enabling VTT");
		}
	}
}