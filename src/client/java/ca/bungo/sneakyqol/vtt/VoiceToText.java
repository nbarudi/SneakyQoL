package ca.bungo.sneakyqol.vtt;

import ca.bungo.sneakyqol.SneakyQOL;
import ca.bungo.sneakyqol.utility.ResourceExtractor;
import ca.bungo.sneakyqol.utility.SneakyToasts;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VoiceToText {
    private static final int SAMPLE_RATE = 16000;
    private static boolean isEnabled = false;
    private static File CONFIG_DIR;

    private static MicrophoneHandler microphoneHandler = null;

    public static void initialize(File configDir) {
        try {
            // Extract model files to the config directory
            CONFIG_DIR = configDir;
            File modelDir = new File(configDir, "vosk-model");
            ResourceExtractor.copyDirectoryFromResources("models/vosk-model.zip", modelDir);
            LibVosk.setLogLevel(LogLevel.WARNINGS);
            isEnabled = true;
        } catch (IOException e) {
            e.printStackTrace();
            isEnabled = false; // Disable the voice recognition feature on error
        }
    }

    public static void startVoiceRecognition() {
        if (!isEnabled) {
            return;
        }

        try {
            if(microphoneHandler == null) {
                microphoneHandler = new MicrophoneHandler(new AudioFormat(SAMPLE_RATE, 16, 1, true, false));
            }
            microphoneHandler.startListening();
            System.out.println("Started listening...");
        } catch (Exception e) {

        }


    }

    private static void playbackAudio(byte[] audioData) {
        new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                if (!AudioSystem.isLineSupported(info)) {
                    System.err.println("Playback line not supported");
                    return;
                }
                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();
                line.write(audioData, 0, audioData.length);
                line.drain();
                line.close();
                System.out.println("Audio playback completed.");
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                System.err.println("Failed to play back audio.");
            }
        }).start();
    }

    public static void stopVoiceRecognitionAndProcess() {
        if (microphoneHandler != null) {
            microphoneHandler.stopListening();
            byte[] audioData = microphoneHandler.readData();
            if (audioData != null) {
                playbackAudio(audioData);
                processAudioData(audioData);
            } else {
                System.err.println("No audio data read from microphone.");
            }
        } else {
            System.err.println("Microphone handler is null.");
        }
    }

    private static void processAudioData(byte[] audioData) {
        new Thread(() -> {
            try {
                // Get the config directory

                // Load the model
                File modelDir = new File(CONFIG_DIR, "vosk-model");
                try (Model model = new Model(modelDir.getAbsolutePath());
                     Recognizer recognizer = new Recognizer(model, SAMPLE_RATE)) {

                    System.out.println("Processing audio data...");
                    if (recognizer.acceptWaveForm(audioData, audioData.length)) {
                        SneakyQOL.LOGGER.info(JsonParser.parseString(recognizer.getResult()).getAsJsonObject().get("text").getAsString());
                    }

                    System.out.println(recognizer.getFinalResult());
                }
            } catch (IOException e) {
                e.printStackTrace();
                MinecraftClient.getInstance().execute(() -> {
                    SneakyToasts.showToast(Text.of("Voice Recognition"), Text.of("Failed to Process"),
                            MinecraftClient.getInstance().getToastManager());
                });
            }
        }).start();
    }
}
