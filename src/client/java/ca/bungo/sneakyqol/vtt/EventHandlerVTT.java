package ca.bungo.sneakyqol.vtt;

import ca.bungo.sneakyqol.SneakyQOL;
import ca.bungo.sneakyqol.settings.keybindings.VoiceToTextKeybind;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Text;
import org.vosk.Model;

import javax.sound.sampled.AudioFormat;
import java.io.File;

public class EventHandlerVTT {

    /** The following variables are used to store the speech recognizer*/
    private static MicrophoneHandler microphoneHandler;

    /** The following variables are used to store the microphone handler*/
    private static SpeechRecognizer speechRecognizer;

    /** The following variables are used to store the last recognized result*/
    private static String lastResult = "";

    /** The following variables are used to store the thread that listens to the microphone*/
    private static Thread listenThread;

    public static void register() {

        ClientLifecycleEvents.CLIENT_STARTED.register(EventHandlerVTT::handelClientStartEvent);

        ClientTickEvents.END_CLIENT_TICK.register(EventHandlerVTT::handleEndClientTickEvent);

        ClientTickEvents.START_CLIENT_TICK.register(EventHandlerVTT::handleStartClientTickEvent);

        ClientLifecycleEvents.CLIENT_STOPPING.register(EventHandlerVTT::handleClientStopEvent);
    }

    public static void resetBuffers(){
        lastResult = "";
    }

    private static void listenThreadTask() {
        while (true) {
            try {
                if (speechRecognizer == null) {         // wait 10 seconds and try to initialize the speech recognizer again
                    if (MinecraftClient.getInstance().player != null) {
                        MinecraftClient.getInstance().player.sendMessage(Text.of("§cAcoustic Model Load Failed"), true);
                    }
                    // listenThread.wait(10000);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ie) {
                        continue;
                    }
                    speechRecognizer = new SpeechRecognizer(new Model(), 16000);
                } else if (microphoneHandler == null) {  // wait 10 seconds and try to initialize the microphone handler again
                    listenThread.wait(10000);
                    microphoneHandler = new MicrophoneHandler(new AudioFormat(16000, 16, 1, true, false));
                    microphoneHandler.startListening();  // Try to restart microphone
                } else {                                 // If the speech recognizer and the microphone handler are initialized successfully
                    String tmp = speechRecognizer.getStringMsg(microphoneHandler.readData());
                    if (!tmp.equals("") && !tmp.equals(lastResult) &&
                            VoiceToTextKeybind.wasPressed) {   // Read audio data from the microphone and send it to the speech recognizer for recognition
                            lastResult = tmp;                           // restore the recognized text
                    }
                }
            } catch (Exception e) {
                SneakyQOL.LOGGER.error(e.getMessage());
            }
        }
    }

    private static void handelClientStartEvent(MinecraftClient client) {     // when the client launch
        File configDir = new File(FabricLoader.getInstance().getConfigDir().toString(), "sneakyqol");
        File modelDir = new File(configDir, "vosk-model");
        SneakyQOL.LOGGER.info("Loading acoustic model from " + modelDir.getPath() + "   ..."); // Log the path of the acoustic model
        try {                                  // Initialize the speech recognizer
            speechRecognizer = new SpeechRecognizer(new Model(modelDir.getAbsolutePath()), 16000);
            SneakyQOL.LOGGER.info("Acoustic model loaded successfully!");
        }catch (Exception e1) {
            SneakyQOL.LOGGER.error(e1.getMessage());
        }
        try {                                   // Initialize the microphone handler, single channel, 16 bits per sample, signed, little endian
            microphoneHandler = new MicrophoneHandler(new AudioFormat(16000, 16, 1, true, false));
            microphoneHandler.startListening();
            SneakyQOL.LOGGER.info("Microphone handler initialized successfully!");
        } catch (Exception e2) {
            SneakyQOL.LOGGER.error(e2.getMessage());
        }
        listenThread = new Thread(EventHandlerVTT::listenThreadTask);
        listenThread.start();
    }

    private static void handleClientStopEvent(MinecraftClient client) {
        listenThread.interrupt();                 // Stop the thread that listens to the microphone
        microphoneHandler.stopListening();        // Stop listening to the microphone
        speechRecognizer = null;
        microphoneHandler = null;
        listenThread = null;                      // Clear the thread
    }

    private static void handleEndClientTickEvent(MinecraftClient client) {     // When the client ticks, check if the user presses the key V
        if (client.player!=null &&                                             // If the player is not null
                VoiceToTextKeybind.wasPressed &&           // If the user presses the key V
                microphoneHandler != null &&                                   // If the microphone initialization is successful
                !lastResult.equals("")) {                                      // If the recognized text is not empty
            // Send the recognized text to the server as a chat message automatically
            if (true) {
                client.player.networkHandler.sendChatMessage("(VTT) " + lastResult);
                client.player.sendMessage(Text.of("§aMessage Sent"), true);
            } else {
                client.setScreen(new ChatScreen("(VTT) " + lastResult));
                if (client.currentScreen!=null) client.currentScreen.applyKeyPressNarratorDelay();
            }
            lastResult = "";                                                   // Clear the recognized text
        }
    }

    private static void handleStartClientTickEvent(MinecraftClient client) {  // handle another client tick event to notify the user that the speech recognition is in progress and the game is not frozen
        if (client.player!=null && VoiceToTextKeybind.wasPressed) {  // If the user presses the key V
            client.player.sendMessage(Text.of("§eRecording & Recognizing..."), true);
        } else if (lastResult.length() > 0) {
            lastResult = "";
        }
    }

}
