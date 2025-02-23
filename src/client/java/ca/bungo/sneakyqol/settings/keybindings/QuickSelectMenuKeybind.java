package ca.bungo.sneakyqol.settings.keybindings;

import ca.bungo.sneakyqol.settings.KeybindHandling;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class QuickSelectMenuKeybind {

    public boolean isWheelActive = false;

    public QuickSelectMenuKeybind() {

        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.quickmenu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_GRAVE_ACCENT,
                KeybindHandling.sneakyKeybindSection
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
//            if(keyBinding.isPressed()){
//                if(!isWheelActive){
//                    isWheelActive = true;
//                    client.mouse.unlockCursor();
//                }
//            }
//            else{
//                if(isWheelActive){
//                    isWheelActive = false;
//                    client.mouse.lockCursor();
//                }
//            }

//            if(keyBinding.wasPressed())
//                client.setScreen(new QuickSelectMenu());

        });

        HudRenderCallback.EVENT.register((context, renderer) ->{
            //if(isWheelActive)
                //quickSelectMenu.render(context);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(isWheelActive && client.mouse.wasLeftButtonClicked()){
                //quickSelectMenu.handleMouseclick(client.mouse.getX(), client.mouse.getY());
            }
        });

    }

}
