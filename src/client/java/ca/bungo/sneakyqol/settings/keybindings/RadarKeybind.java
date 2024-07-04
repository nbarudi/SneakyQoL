package ca.bungo.sneakyqol.settings.keybindings;

import ca.bungo.sneakyqol.SneakyQOL;
import ca.bungo.sneakyqol.utility.PlayerUtility;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

import static ca.bungo.sneakyqol.settings.KeybindHandling.sneakyKeybindSection;

public class RadarKeybind {

    private static boolean radarActive = false;

    public RadarKeybind(){

        KeyBinding radarKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.radar",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                sneakyKeybindSection
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(radarKeybind.wasPressed()){
                radarActive = !radarActive;
                if (client.player != null)
                    client.player.sendMessage(Text.of("§eToggled Radar " + (radarActive ? "§aOn" : "§cOff")), true);
            }
        });

        HudRenderCallback.EVENT.register((context, delta) ->{

            int X_OFFSET = 15;
            int Y_OFFSET = 15;

            MinecraftClient client = MinecraftClient.getInstance();
            if(client.player == null || !radarActive) return;

            PlayerEntity localPlayer = client.player;

            List<PlayerEntity> playerEntityList = PlayerUtility.getAllPlayers(client);
            TextRenderer textRenderer = client.textRenderer;
            List<Text> playerInfo = new ArrayList<>();

            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            // Define box dimensions
            int boxWidth = 100 + X_OFFSET;
            int boxHeight = 15*(playerEntityList.size()-1);

            // Calculate box position (centered)
            int x = (screenWidth - boxWidth) - X_OFFSET;
            int y = (screenHeight - boxHeight) - Y_OFFSET;


            context.fill(x, y, x + boxWidth, y + boxHeight, 0x90000000);


            //drawBackground(context, x - 10, y - 10, x + 140, y + playerEntityList.size() * 10 + 10);

            for(PlayerEntity player : playerEntityList){
                if(player.getUuid().toString().equals(client.player.getUuid().toString())) continue;
                int playerDistance = (int)Math.floor(player.getPos().distanceTo(localPlayer.getPos()));
                Text text = Text.of("§e" + player.getEntityName() + "§r(§b" + playerDistance +"§r)");
                playerInfo.add(text);
            }

            MultilineText multilineText = MultilineText.createFromTexts(textRenderer, playerInfo);
            multilineText.draw(context, x+X_OFFSET, y+2, Y_OFFSET, 0xff0000);

        });

    }

}
