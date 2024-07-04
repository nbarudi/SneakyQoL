package ca.bungo.sneakyqol.settings.keybindings;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

import static ca.bungo.sneakyqol.settings.KeybindHandling.sneakyKeybindSection;

public class BetterSneakKeybind {

    public static boolean isFakeSneaking = false;

    public BetterSneakKeybind() {
        KeyBinding betterSneakKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.sneakyqol.toggle_better_sneak",
                InputUtil.Type.KEYSYM,
                -1,
                sneakyKeybindSection
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client ->{
            if(betterSneakKeybind.wasPressed()) {
                if(client.player != null){
                    isFakeSneaking = !isFakeSneaking;
                    if(isFakeSneaking) {
                        ClientCommandC2SPacket startPacket = new ClientCommandC2SPacket(MinecraftClient.getInstance().player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY);
                        client.getNetworkHandler().sendPacket(startPacket);
                    }
                    else {
                        ClientCommandC2SPacket startPacket = new ClientCommandC2SPacket(MinecraftClient.getInstance().player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY);
                        client.getNetworkHandler().sendPacket(startPacket);
                    }
                }
            }
        });
    }

}
