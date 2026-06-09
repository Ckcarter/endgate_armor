package rem.endgate_armor.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rem.endgate_armor.registry.ModBlockEntities;

public class CreativePowerSupplyBlockEntity extends BlockEntity {

    public static final int FE_PER_TICK_PER_SIDE = 100_000;

    private final IEnergyStorage creativeEnergy = new IEnergyStorage() {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return 0;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return Math.max(0, maxExtract);
        }

        @Override
        public int getEnergyStored() {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getMaxEnergyStored() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
    };

    private LazyOptional<IEnergyStorage> energyOptional = LazyOptional.of(() -> creativeEnergy);

    public CreativePowerSupplyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CREATIVE_POWER_SUPPLY.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CreativePowerSupplyBlockEntity powerSupply) {
        if (level.isClientSide) return;

        for (Direction direction : Direction.values()) {
            BlockEntity target = level.getBlockEntity(pos.relative(direction));
            if (target == null) continue;

            target.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(storage -> {
                if (storage.canReceive()) {
                    storage.receiveEnergy(FE_PER_TICK_PER_SIDE, false);
                }
            });
        }
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyOptional.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        energyOptional = LazyOptional.of(() -> creativeEnergy);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.ENERGY) {
            return energyOptional.cast();
        }
        return super.getCapability(capability, side);
    }
}
