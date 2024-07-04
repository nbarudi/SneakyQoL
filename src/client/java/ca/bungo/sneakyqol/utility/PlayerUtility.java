package ca.bungo.sneakyqol.utility;

import ca.bungo.sneakyqol.SneakyQOL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PlayerUtility {

    public static @Nullable String fetchPlayerSkin(@NotNull  PlayerEntity player) {
        GameProfile playerProfile = player.getGameProfile();

        Map<String, Collection<Property>> collection = playerProfile.getProperties().asMap();
        if (collection.containsKey("textures")) {
            Collection<Property> textures = collection.get("textures");
            Property textureProperty = null;
            for (Property property : textures) {
                if (property.name().equalsIgnoreCase("textures")) {
                    textureProperty = property;
                    break;
                }
            }

            if (textureProperty != null) {
                String jsonResult = new String(Base64.getDecoder().decode(textureProperty.value()));
                JsonObject jsonObject = JsonParser.parseString(jsonResult).getAsJsonObject();
                if (!jsonObject.has("textures")) return null;
                JsonObject texturesJson = jsonObject.get("textures").getAsJsonObject();
                if (!texturesJson.has("SKIN")) return null;
                JsonObject skinJson = texturesJson.get("SKIN").getAsJsonObject();
                if (!skinJson.has("url")) return null;
                return skinJson.get("url").getAsString();
            }
        }
        return null;
    }

    public static PlayerEntity getTargetPlayer(MinecraftClient client, double range) {
        if(client.player == null || client.world == null)
            return null;

        Vec3d cameraPos = client.player.getCameraPosVec(1.0F);
        Vec3d lookVec = client.player.getRotationVec(1.0F).multiply(range);
        Vec3d endVec = cameraPos.add(lookVec);
        Box box = new Box(cameraPos, endVec).expand(1.0, 1.0, 1.0);

        List<Entity> entities = client.world.getEntitiesByClass(Entity.class, box, entity -> entity instanceof PlayerEntity && entity != client.player);
        EntityHitResult entityHitResult = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : entities) {
            Box entityBox = entity.getBoundingBox().expand(0.1);
            Vec3d hitResult = entityBox.raycast(cameraPos, endVec).isPresent() ? entityBox.raycast(cameraPos, endVec).get() : null;
            if(hitResult == null) continue;
            double distance = hitResult.squaredDistanceTo(cameraPos);
            if (distance < closestDistance) {
                closestDistance = distance;
                entityHitResult = new EntityHitResult(entity, hitResult);
            }
        }

        if(entityHitResult == null) return null;

        if(entityHitResult.getType() == HitResult.Type.ENTITY){
            Entity entity = entityHitResult.getEntity();
            if(entity instanceof PlayerEntity player){
                return player;
            }
        }

        return null;
    }

    public static List<PlayerEntity> getNearbyPlayers(MinecraftClient client, PlayerEntity player, double radius) {
        if (client.world == null) {
            return List.of();
        }
        Box box = new Box(player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius);
        return client.world.getEntitiesByClass(PlayerEntity.class, box, p -> p != player);
    }

    public static List<PlayerEntity> getAllPlayers(MinecraftClient client) {
        if (client.world == null) {
            return List.of();
        }
        return client.world.getPlayers().stream().map(s -> (PlayerEntity)s).toList();
    }

}
