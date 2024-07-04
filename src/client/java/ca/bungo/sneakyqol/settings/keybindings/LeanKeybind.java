package ca.bungo.sneakyqol.settings.keybindings;

import ca.bungo.sneakyqol.settings.GlobalValues;
import ca.bungo.sneakyqol.settings.KeybindHandling;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class LeanKeybind {

    public LeanKeybind() {

        KeyBinding leanLeftKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.leanleft",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KeybindHandling.sneakyKeybindSection
        ));

        KeyBinding leanRightKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.leanright",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                KeybindHandling.sneakyKeybindSection
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(leanLeftKeybind.isPressed() && !leanRightKeybind.isPressed()) {
                GlobalValues.leanDirection = -1;
            } else if(leanRightKeybind.isPressed() && !leanLeftKeybind.isPressed()) {
                GlobalValues.leanDirection = 1;
            } else {
                GlobalValues.leanDirection = 0;
            }
        });

    }

}
