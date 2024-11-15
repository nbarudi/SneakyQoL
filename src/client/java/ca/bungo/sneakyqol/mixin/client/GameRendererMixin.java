package ca.bungo.sneakyqol.mixin.client;

import ca.bungo.sneakyqol.settings.GlobalValues;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    private static final float LEAN_SPEED = 0.1f;


    @Inject(method = "renderWorld", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/GameRenderer;tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V",
            shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void onRenderWorld(RenderTickCounter tickCounter, CallbackInfo ci, float f, boolean bl, Camera camera, Entity entity, float g, double d, Matrix4f matrix4f, MatrixStack matrixStack) {
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
            matrixStack.translate(GlobalValues.currentLeanOffset, 0, 0);
            matrixStack.multiply(quaternion);
        }
    }

}
