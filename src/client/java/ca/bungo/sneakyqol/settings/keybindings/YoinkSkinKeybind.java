package ca.bungo.sneakyqol.settings.keybindings;

import ca.bungo.sneakyqol.SneakyQOL;
import ca.bungo.sneakyqol.utility.PlayerUtility;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import static ca.bungo.sneakyqol.settings.KeybindHandling.sneakyKeybindSection;

public class YoinkSkinKeybind {

    public YoinkSkinKeybind(){
        KeyBinding yoinkSkin = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.yoink_skin",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                sneakyKeybindSection
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (yoinkSkin.wasPressed()) {
                PlayerEntity entity = PlayerUtility.getTargetPlayer(client, 25);
                if (entity != null) {
                    String SkinURL = PlayerUtility.fetchPlayerSkin(entity);
                    if(client.player == null) return;
                    client.player.sendMessage(Text.of("§e" + entity.getEntityName() + "'s Skin URL:\n").copy().append(
                            Text.literal("§b" + SkinURL)
                                    .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, SkinURL)))
                    ));
                }
                else {
                    if(client.player == null) return;
                    client.player.sendMessage(Text.of("§4Make sure you're looking at a player!"), true);
                }
            }
        });
    }

}
