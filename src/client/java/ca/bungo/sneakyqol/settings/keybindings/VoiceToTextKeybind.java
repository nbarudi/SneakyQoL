package ca.bungo.sneakyqol.settings.keybindings;

import ca.bungo.sneakyqol.vtt.EventHandlerVTT;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import static ca.bungo.sneakyqol.settings.KeybindHandling.sneakyKeybindSection;

public class VoiceToTextKeybind {

    public static boolean wasPressed = false;

    public VoiceToTextKeybind() {
        KeyBinding voiceToTextKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.voicetotext",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_CAPS_LOCK,
                sneakyKeybindSection
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (voiceToTextKeybind.isPressed()) {
                if (!wasPressed) {
                    wasPressed = true;
                    EventHandlerVTT.resetBuffers();
                }
            } else {
                if (wasPressed) {
                    wasPressed = false;
                    //VoiceToText.stopVoiceRecognitionAndProcess();
                }
            }
        });
    }

}
