package ca.bungo.sneakyqol.mixin.client;


import ca.bungo.sneakyqol.settings.GlobalValues;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRenderMixin {

    private static final float LEAN_SPEED = 0.1f;

    @Inject(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", at = @At("HEAD"))
    private void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
        float targetLeanAngle = 0.0f;
        float targetLeanOffset = 0.0f;

        if (GlobalValues.leanDirection == -1) {
            targetLeanAngle = -15.0F; // Tilt left
            targetLeanOffset = 0.66F;
        } else if (GlobalValues.leanDirection == 1) {
            targetLeanAngle = 15.0F; // Tilt right
            targetLeanOffset = -0.66F;
        } else {
            targetLeanAngle = 0.0F; // No tilt
            targetLeanOffset = 0.0F;
        }

        // Interpolate current lean values towards target values
        GlobalValues.currentLeanAngle += (targetLeanAngle - GlobalValues.currentLeanAngle) * LEAN_SPEED;
        GlobalValues.currentLeanOffset += (targetLeanOffset - GlobalValues.currentLeanOffset) * LEAN_SPEED;

        if (GlobalValues.currentLeanAngle != 0.0f) {
            Quaternionf quaternion = new Quaternionf().rotateZ((float) Math.toRadians(GlobalValues.currentLeanAngle));
            matrix.translate(GlobalValues.currentLeanOffset, 0, 0);
            matrix.multiply(quaternion);
        }
    }

}
