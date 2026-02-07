package rem.endgate_armor.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rem.endgate_armor.Endgate_armor;
import rem.endgate_armor.registry.ModItems;

/**
 * Full set bonus:
 * - Negates ender pearl impact damage.
 * - When hurt, small chance to "blink" (chorus-like teleport) to a nearby safe spot.
 */
@Mod.EventBusSubscriber(modid = Endgate_armor.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ModEvents {

    private ModEvents() {}

    private static boolean isWearingFullSet(Player player) {
        ItemStack boots = player.getInventory().armor.get(0);
        ItemStack legs  = player.getInventory().armor.get(1);
        ItemStack chest = player.getInventory().armor.get(2);
        ItemStack helm  = player.getInventory().armor.get(3);

        return !boots.isEmpty() && boots.is(ModItems.ENDGATE_BOOTS.get())
                && !legs.isEmpty() && legs.is(ModItems.ENDGATE_LEGGINGS.get())
                && !chest.isEmpty() && chest.is(ModItems.ENDGATE_CHESTPLATE.get())
                && !helm.isEmpty() && helm.is(ModItems.ENDGATE_HELMET.get());
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!isWearingFullSet(player)) return;

        DamageSource source = event.getSource();
        Entity direct = source.getDirectEntity();

        // Ender pearl impact damage is tiny, but this makes the set feel snappy.
        if (direct instanceof ThrownEnderpearl) {
            event.setCanceled(true);
            return;
        }

        // 15% chance to blink on any hit (server-side only)
        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) return;

        RandomSource rand = serverLevel.random;
        if (rand.nextFloat() > 0.15f) return;

        if (tryBlinkTeleport(serverLevel, player, rand)) {
            serverLevel.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    private static boolean tryBlinkTeleport(ServerLevel level, Player player, RandomSource rand) {
        BlockPos origin = player.blockPosition();

        for (int attempt = 0; attempt < 16; attempt++) {
            int dx = rand.nextIntBetweenInclusive(-8, 8);
            int dz = rand.nextIntBetweenInclusive(-8, 8);
            int dy = rand.nextIntBetweenInclusive(-4, 4);

            BlockPos pos = origin.offset(dx, dy, dz);

            // Find a nearby ground position (move downward a bit)
            BlockPos.MutableBlockPos mut = pos.mutable();
            for (int down = 0; down < 8; down++) {
                BlockState below = level.getBlockState(mut.below());
                if (below.isSolidRender(level, mut.below())) break;
                mut.move(0, -1, 0);
            }

            // Require two blocks of headroom
            BlockPos feet = mut.immutable();
            if (!level.getBlockState(feet).getCollisionShape(level, feet).isEmpty()) continue;
            if (!level.getBlockState(feet.above()).getCollisionShape(level, feet.above()).isEmpty()) continue;

            double x = feet.getX() + 0.5D;
            double y = feet.getY();
            double z = feet.getZ() + 0.5D;

            return player.randomTeleport(x, y, z, true);
        }

        return false;
    }
}
