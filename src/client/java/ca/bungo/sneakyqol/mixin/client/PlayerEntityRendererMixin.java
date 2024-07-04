package ca.bungo.sneakyqol.mixin.client;


import ca.bungo.sneakyqol.settings.keybindings.BetterSneakKeybind;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
    private void onRender(AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci){
        if(abstractClientPlayerEntity instanceof ClientPlayerEntity && BetterSneakKeybind.isFakeSneaking){
            abstractClientPlayerEntity.setPose(EntityPose.CROUCHING);
        }
    }

    @Inject(method = "setModelPose", at=@At("TAIL"))
    private void setModelPose(AbstractClientPlayerEntity player, CallbackInfo ci) {
        PlayerEntityModel model = ((PlayerEntityRenderer) (Object) this).getModel();
        if(BetterSneakKeybind.isFakeSneaking
                && player.getUuid().toString().equals(MinecraftClient.getInstance().player.getUuid().toString())){
            model.sneaking = true;
        }
    }

}
