package ca.bungo.sneakyqol.settings.keybindings;

import ca.bungo.sneakyqol.utility.PlayerUtility;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import static ca.bungo.sneakyqol.settings.KeybindHandling.sneakyKeybindSection;

public class ShowNamesKeybind {
    public static String hoveredPlayersName = "";

    public ShowNamesKeybind() {
        KeyBinding showNamesKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.shownames",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                sneakyKeybindSection
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (showNamesKeybind.isPressed()) {
                PlayerEntity player = PlayerUtility.getTargetPlayer(client, 45);
                if(player == null) {
                    hoveredPlayersName = "";
                    return;
                }

                hoveredPlayersName = player.getName().getString();
            }
            else if(!showNamesKeybind.isPressed() && !hoveredPlayersName.isEmpty()){
                hoveredPlayersName = "";
            }
        });

        HudRenderCallback.EVENT.register((screen, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            if(client.player == null)
                return;

            TextRenderer textRenderer = client.textRenderer;
            MultilineText text = MultilineText.create(textRenderer, Text.of("§b" + hoveredPlayersName));
            int x = client.getWindow().getScaledWidth()/2 + text.getMaxWidth();
            int y = client.getWindow().getScaledHeight()/2;

            RenderSystem.enableBlend();

            text.drawCenterWithShadow(screen, x, y);
            RenderSystem.disableBlend();
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
