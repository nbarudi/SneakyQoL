package ca.bungo.sneakyqol.settings.keybindings;

import ca.bungo.sneakyqol.SneakyQOL;
import ca.bungo.sneakyqol.settings.KeybindHandling;
import ca.bungo.sneakyqol.utility.PlayerUtility;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;

import java.util.*;

import static ca.bungo.sneakyqol.settings.KeybindHandling.sneakyKeybindSection;

public class TestKeybind {

    public TestKeybind() {
        KeyBinding testKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.testing",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                sneakyKeybindSection
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (testKeybind.wasPressed()) {
                if(client.player == null) return;

                for(PlayerEntity player : PlayerUtility.getAllPlayers(client)){
                    SneakyQOL.LOGGER.info("Found Player: {}", player.getName().getString());
                }
            }
        });


    }

}
