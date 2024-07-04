package ca.bungo.sneakyqol.settings.keybindings;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import static ca.bungo.sneakyqol.settings.KeybindHandling.sneakyKeybindSection;

public class SimpleCommandKeybinds {

    public SimpleCommandKeybinds() {

        KeyBinding bindCommandSit = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.sit",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        KeyBinding bindCommandBellyflop = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.bellyflop",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        KeyBinding bindCommandMcl = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.mcl",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        KeyBinding bindCommandFury = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.fury",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        KeyBinding bindCommandLay = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.lay",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        KeyBinding bindCommandCampfire = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.campfire",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        KeyBinding bindCommandWalk = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.walk",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        KeyBinding bindCommandMeGasp = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.me.gasp",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        KeyBinding bindCommandMeSneaking = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.me.sneaking",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        KeyBinding bindCommandMeSmellsFlowers = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.me.smells_flowers",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        KeyBinding bindCommandMeWaves = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.me.waves",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        KeyBinding bindCommandMeSalutes = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.me.salutes",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if(bindCommandFury.wasPressed())
                sendCommand("/fury");

            if(bindCommandSit.wasPressed())
                sendCommand("/sit");

            if(bindCommandBellyflop.wasPressed())
                sendCommand("/bellyflop");

            if(bindCommandCampfire.wasPressed())
                sendCommand("/campfire");

            if(bindCommandLay.wasPressed())
                sendCommand("/lay");

            if(bindCommandMcl.wasPressed())
                sendCommand("/mcl");

            if(bindCommandMeGasp.wasPressed())
                sendCommand("/me :O");

            if(bindCommandMeSmellsFlowers.wasPressed())
                sendCommand("/me smells the flowers");

            if(bindCommandMeSneaking.wasPressed())
                sendCommand("/mee sneaking");


            if(bindCommandWalk.wasPressed())
                sendCommand("/walk");

            if(bindCommandMeWaves.wasPressed())
                sendCommand("/me waves");

            if(bindCommandMeSalutes.wasPressed())
                sendCommand("/me salutes");

        });

    }


    private void sendCommand(String command){
        MinecraftClient.getInstance().execute(() -> {
            ChatScreen screen = new ChatScreen("");
            Screen prevScreen = MinecraftClient.getInstance().currentScreen;
            MinecraftClient.getInstance().setScreen(screen);
            screen.sendMessage(command, false);
            MinecraftClient.getInstance().setScreen(prevScreen);
        });
    }

}
