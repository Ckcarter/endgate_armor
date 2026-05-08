package rem.endgate_armor.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import rem.endgate_armor.registry.ModItems;

import java.util.function.Supplier;

public class TeleportAbilityPacket {
    private static final int COOLDOWN_TICKS = 100; // 5 seconds
    private static final double MAX_DISTANCE = 35.D;

    public static void encode(TeleportAbilityPacket packet, FriendlyByteBuf buffer) {
    }

    public static TeleportAbilityPacket decode(FriendlyByteBuf buffer) {
        return new TeleportAbilityPacket();
    }

    public static void handle(TeleportAbilityPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            if (!(player.level() instanceof ServerLevel level)) return;
            if (!isWearingFullSet(player)) return;

            ItemStack helmet = player.getInventory().armor.get(3);
            if (player.getCooldowns().isOnCooldown(helmet.getItem())) return;

            if (tryTeleportForward(level, player)) {
                player.getCooldowns().addCooldown(helmet.getItem(), COOLDOWN_TICKS);
            }
        });
        context.setPacketHandled(true);
    }

    private static boolean isWearingFullSet(Player player) {
        ItemStack boots = player.getInventory().armor.get(0);
        ItemStack legs = player.getInventory().armor.get(1);
        ItemStack chest = player.getInventory().armor.get(2);
        ItemStack helm = player.getInventory().armor.get(3);

        return !boots.isEmpty() && boots.is(ModItems.ENDGATE_BOOTS.get())
                && !legs.isEmpty() && legs.is(ModItems.ENDGATE_LEGGINGS.get())
                && !chest.isEmpty() && chest.is(ModItems.ENDGATE_CHESTPLATE.get())
                && !helm.isEmpty() && helm.is(ModItems.ENDGATE_HELMET.get());
    }

    private static boolean tryTeleportForward(ServerLevel level, ServerPlayer player) {
        Vec3 eye = player.getEyePosition();
        Vec3 end = eye.add(player.getLookAngle().normalize().scale(MAX_DISTANCE));

        BlockHitResult hit = level.clip(new ClipContext(eye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 target = hit.getType() == HitResult.Type.BLOCK
                ? hit.getLocation().subtract(player.getLookAngle().normalize().scale(1.0D))
                : end;

        BlockPos base = BlockPos.containing(target.x, target.y, target.z);

        for (int yOffset = 2; yOffset >= -8; yOffset--) {
            BlockPos feet = base.offset(0, yOffset, 0);
            if (!isSafeTeleportSpot(level, feet)) continue;

            double x = feet.getX() + 0.5D;
            double y = feet.getY();
            double z = feet.getZ() + 0.5D;

            level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
            boolean moved = player.randomTeleport(x, y, z, true);
            if (moved) {
                level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                player.fallDistance = 0.0F;
                return true;
            }
        }

        return false;
    }

    private static boolean isSafeTeleportSpot(ServerLevel level, BlockPos feet) {
        BlockState below = level.getBlockState(feet.below());
        if (!below.isSolidRender(level, feet.below())) return false;
        if (!level.getBlockState(feet).getCollisionShape(level, feet).isEmpty()) return false;
        return level.getBlockState(feet.above()).getCollisionShape(level, feet.above()).isEmpty();
    }
}
