package ca.bungo.sneakyqol.utility;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SneakyToasts implements Toast {

    private static final Identifier TEXTURE = new Identifier("sneakyqol", "toast/background");

    private long startTime;
    public Text title;
    public Text description;
    public boolean justUpdated = true;

    public SneakyToasts(Text title, Text description) {
        this.title = title;
        this.description = description;
    }

    public static void showToast(Text title, Text description, ToastManager manager) {
        SneakyToasts toast = manager.getToast(SneakyToasts.class, TYPE);

        if(toast == null){
            toast = new SneakyToasts(title, description);
            manager.add(toast);
        } else {
            toast.title = title;
            toast.description = description;
            toast.justUpdated = true;
        }

    }

    @Override
    public Toast.Visibility draw(DrawContext context, ToastManager manager, long startTime) {
        if (this.justUpdated) {
            this.startTime = startTime;
            this.justUpdated = false;
        }

        context.drawGuiTexture(TEXTURE, 0, 0, this.getWidth(), this.getHeight());
        context.drawText(manager.getClient().textRenderer, this.title, 30, 7, -11534256, false);
        context.drawText(manager.getClient().textRenderer, this.description, 30, 18, -16777216, false);

        return (double) (startTime - this.startTime) >= 5000.0 * manager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }
}
