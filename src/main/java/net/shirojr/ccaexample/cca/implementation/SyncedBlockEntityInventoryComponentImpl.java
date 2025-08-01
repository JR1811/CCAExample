package net.shirojr.ccaexample.cca.implementation;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.shirojr.ccaexample.CCAExampleComponents;
import net.shirojr.ccaexample.block.entity.ItemHolderBlockEntity;
import net.shirojr.ccaexample.cca.component.SyncedBlockInventoryComponent;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.Optional;
import java.util.function.Consumer;

public class SyncedBlockEntityInventoryComponentImpl implements SyncedBlockInventoryComponent, AutoSyncedComponent {
    public static final int CAPACITY = 5;
    public static final int DECAY_TICKS = 120;

    private final ItemHolderBlockEntity provider;
    private final DefaultedList<ItemStack> inventory;

    private long age;

    public SyncedBlockEntityInventoryComponentImpl(ItemHolderBlockEntity blockEntity) {
        // the provider is very specific with the BlockEntity here. You could write a more flexible system
        // see https://ladysnake.org/wiki/cardinal-components-api/modules/block#registration
        this.provider = blockEntity;
        this.inventory = DefaultedList.ofSize(CAPACITY, ItemStack.EMPTY);
    }

    @Override
    public long getAge() {
        return age;
    }

    /**
     * Use {@link #modifyInventory(Consumer, boolean) modifyInventory()} instead, to change content.
     */
    @Override
    public DefaultedList<ItemStack> getInventory() {
        return this.inventory;
    }

    @Override
    public void modifyInventory(Consumer<DefaultedList<ItemStack>> consumer, boolean shouldSync) {
        consumer.accept(this.inventory);
        if (shouldSync) {
            this.sync();
        }
    }

    @Override
    public boolean add(boolean shouldSync, ItemStack stack) {
        boolean success = false;
        if (!hasContent()) {
            this.age = 0;
        }
        for (int i = 0; i < this.inventory.size(); i++) {
            ItemStack entry = this.inventory.get(i);
            if (!entry.isEmpty()) continue;
            this.inventory.set(i, stack.copy());
            success = true;
            break;
        }
        if (success && shouldSync) {
            this.sync();
        }
        return success;
    }

    @Override
    public @Nullable ItemStack remove(boolean shouldSync) {
        ItemStack result = null;
        for (int i = this.inventory.size() - 1; i >= 0; i--) {
            ItemStack entry = this.inventory.get(i);
            if (entry.isEmpty()) continue;
            result = entry.copy();
            this.inventory.set(i, ItemStack.EMPTY);
            break;
        }
        if (result != null && shouldSync) {
            this.sync();
        }
        return result;
    }

    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        // just getting the inventory from NBT
        // it is easier if you make use of SimpleInventory instead of the DefaultedList<ItemStack>
        // this is used for persistent storage and networking
        if (nbt.contains("syncedInv")) {
            clear(false);
            NbtCompound syncedInvNbt = nbt.getCompound("syncedInv");
            modifyInventory(itemStacks -> {
                for (String indexKey : syncedInvNbt.getKeys()) {
                    Optional<ItemStack> optionalStack = ItemStack.fromNbt(wrapperLookup, syncedInvNbt.getCompound(indexKey));
                    if (optionalStack.isEmpty()) continue;
                    int index = Integer.parseInt(indexKey);
                    itemStacks.set(index, optionalStack.get());
                }
            }, false);
            this.sync();
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        // just adding the inventory to the NBT
        // it is easier if you make use of SimpleInventory instead of the DefaultedList<ItemStack>
        // this is used for persistent storage and networking
        NbtCompound syncedInvNbt = new NbtCompound();
        for (int i = 0; i < getInventory().size(); i++) {
            ItemStack stack = getInventory().get(i);
            if (stack.isEmpty()) continue;
            syncedInvNbt.put(String.valueOf(i), stack.encode(wrapperLookup));
        }
        nbt.put("syncedInv", syncedInvNbt);
    }

    private void sync() {
        CCAExampleComponents.SYNCED_BLOCK_INVENTORY.sync(this.provider);
    }

    @Override
    public void tick() {
        World world = this.provider.getWorld();
        this.age++;
        if (world instanceof ServerWorld && this.age % DECAY_TICKS == 0) {
            this.onDecayTick();
        }
    }

    private void onDecayTick() {
        if (!hasContent()) return;
        ItemStack stack = this.remove(true);
        if (stack == null || stack.isEmpty()) return;
        if (!(this.provider.getWorld() instanceof ServerWorld serverWorld)) return;
        BlockPos pos = this.provider.getPos();
        ItemScatterer.spawn(serverWorld, pos.getX(), pos.getY(), pos.getZ(), stack);
        serverWorld.playSound(null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS);
    }
}
