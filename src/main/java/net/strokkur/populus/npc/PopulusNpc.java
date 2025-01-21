package net.strokkur.populus.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.jspecify.annotations.NullMarked;

import java.util.EnumSet;
import java.util.UUID;

@NullMarked
public class PopulusNpc extends ServerPlayer {
    
    public PopulusNpc(Location location, GameProfile gameProfile) {
        super(
            MinecraftServer.getServer(),
            ((CraftWorld) location.getWorld()).getHandle(),
            gameProfile,
            ClientInformation.createDefault()
        );

        setPos(location.x(), location.y(), location.z());
        setRot(location.getYaw(), location.getPitch());

        final ClientboundPlayerInfoUpdatePacket updatePacket = new ClientboundPlayerInfoUpdatePacket(
            EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER),
            new ClientboundPlayerInfoUpdatePacket.Entry(gameProfile.getId(), gameProfile, false, 0, GameType.DEFAULT_MODE, null, false, 0, null)
        );

        final ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(
            getId(), uuid, trackingPosition().x(), trackingPosition().y(), trackingPosition().z(),
            getXRot(), getYRot(), getType(), 0, Vec3.ZERO, getYHeadRot()
        );

        Bukkit.getOnlinePlayers().stream()
            .filter(player -> player.getWorld().equals(location.getWorld()))
            .map(player -> ((CraftPlayer) player).getHandle().connection)
            .forEach(connection -> {
                connection.send(updatePacket);
                connection.send(addEntityPacket);
            });
    }

    public PopulusNpc(Location location, String name, UUID uuid) {
        this(location, new GameProfile(uuid, name));
    }
}
