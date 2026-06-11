package rem.endgate_armor.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rem.endgate_armor.menu.ItemGeneratorMenu;
import rem.endgate_armor.registry.ModBlockEntities;
import rem.endgate_armor.registry.ModItems;

public class ItemGeneratorBlockEntity extends BlockEntity implements MenuProvider {

    // 5 minutes at 20 ticks per second.
    public static final int GENERATE_TIME_TICKS = 5 * 60 * 20;

    public static final int ENERGY_CAPACITY = 1_000;
    public static final int MAX_RECEIVE = 1_000;
    public static final int ENERGY_PER_GOLD = ENERGY_CAPACITY / 20; // 5% of full bar per ingot

    private int progress = 0;
    private int energyUsedThisCycle = 0;

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    };

    /**
     * Input-only FE storage for outside blocks/cables, with a private internal drain
     * method for the generator.
     *
     * Important: EnergyStorage.extractEnergy(...) obeys maxExtract. This machine was
     * created with maxExtract = 0 so cables could not pull power back out, but that
     * also made the machine's own drain extract 0 FE. consumeEnergy(...) bypasses
     * maxExtract only for this block entity's crafting cost.
     */
    private final InternalEnergyStorage energy = new InternalEnergyStorage(ENERGY_CAPACITY, MAX_RECEIVE);

    private class InternalEnergyStorage extends EnergyStorage {
        private InternalEnergyStorage(int capacity, int maxReceive) {
            super(capacity, maxReceive, 0);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate) {
                setChanged();
            }
            return received;
        }

        /** Keep the block input-only to outside FE cables/items. */
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        private int consumeEnergy(int amount, boolean simulate) {
            int extracted = Math.min(this.energy, Math.max(0, amount));
            if (extracted > 0 && !simulate) {
                this.energy -= extracted;
                setChanged();
            }
            return extracted;
        }
    }

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case ItemGeneratorMenu.DATA_PROGRESS -> progress;
                case ItemGeneratorMenu.DATA_MAX_PROGRESS -> GENERATE_TIME_TICKS;
                case ItemGeneratorMenu.DATA_ENERGY -> energy.getEnergyStored();
                case ItemGeneratorMenu.DATA_MAX_ENERGY -> energy.getMaxEnergyStored();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == ItemGeneratorMenu.DATA_PROGRESS) {
                progress = value;
                if (progress <= 0) {
                    energyUsedThisCycle = 0;
                }
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    private LazyOptional<IEnergyStorage> energyOptional = LazyOptional.of(() -> energy);
    private LazyOptional<IItemHandler> itemOptional = LazyOptional.of(() -> itemHandler);

    public ItemGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ITEM_GENERATOR.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ItemGeneratorBlockEntity generator) {
        if (level.isClientSide) return;

        if (!generator.canOutputGold()) {
            return;
        }

        int nextProgress = generator.progress + 1;

        // Drain the 10,000 FE smoothly across the full 5-minute cycle.
        // This makes the FE/usage bar lower while the machine is running,
        // instead of dropping all at once when the ingot appears.
        int targetEnergyUsed = (int) ((long) ENERGY_PER_GOLD * nextProgress / GENERATE_TIME_TICKS);
        int energyToDrainThisTick = targetEnergyUsed - generator.energyUsedThisCycle;

        if (energyToDrainThisTick > 0) {
            if (generator.energy.getEnergyStored() < energyToDrainThisTick) {
                return;
            }

            generator.energy.consumeEnergy(energyToDrainThisTick, false);
            generator.energyUsedThisCycle += energyToDrainThisTick;
        }

        generator.progress = nextProgress;

        if (generator.progress >= GENERATE_TIME_TICKS) {
            int remainingCost = ENERGY_PER_GOLD - generator.energyUsedThisCycle;

            if (remainingCost > 0) {
                if (generator.energy.getEnergyStored() < remainingCost) {
                    return;
                }

                generator.energy.consumeEnergy(remainingCost, false);
            }

            generator.progress = 0;
            generator.energyUsedThisCycle = 0;
            generator.insertGold();
        }

        generator.setChanged();
    }

    private boolean canOutputGold() {
        ItemStack current = itemHandler.getStackInSlot(0);
        return current.isEmpty() || (current.is(ModItems.COSMIC_DUST.get()) && current.getCount() < current.getMaxStackSize());
    }

    private void insertGold() {
        ItemStack current = itemHandler.getStackInSlot(0);
        if (current.isEmpty()) {
            itemHandler.setStackInSlot(0, new ItemStack(ModItems.COSMIC_DUST.get(), 1));
        } else if (current.is(ModItems.COSMIC_DUST.get()) && current.getCount() < current.getMaxStackSize()) {
            current.grow(1);
            itemHandler.setStackInSlot(0, current);
        }
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return GENERATE_TIME_TICKS;
    }

    public int getEnergyStored() {
        return energy.getEnergyStored();
    }

    public int getMaxEnergyStored() {
        return energy.getMaxEnergyStored();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.endgate_armor.item_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ItemGeneratorMenu(containerId, inventory, this, data);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("Progress");
        energyUsedThisCycle = tag.getInt("EnergyUsedThisCycle");
        if (tag.contains("Energy")) {
            energy.deserializeNBT(tag.get("Energy"));
        }
        if (tag.contains("Inventory")) {
            itemHandler.deserializeNBT(tag.getCompound("Inventory"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Progress", progress);
        tag.putInt("EnergyUsedThisCycle", energyUsedThisCycle);
        tag.put("Energy", energy.serializeNBT());
        tag.put("Inventory", itemHandler.serializeNBT());
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        energyOptional.invalidate();
        itemOptional.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        energyOptional = LazyOptional.of(() -> energy);
        itemOptional = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.ENERGY) {
            return energyOptional.cast();
        }
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return itemOptional.cast();
        }
        return super.getCapability(capability, side);
    }
}
