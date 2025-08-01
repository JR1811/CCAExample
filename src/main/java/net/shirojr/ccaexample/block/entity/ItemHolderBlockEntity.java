package net.shirojr.ccaexample.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.shirojr.ccaexample.init.CCAExampleBlockEntities;

public class ItemHolderBlockEntity extends BlockEntity {
    public ItemHolderBlockEntity(BlockPos pos, BlockState state) {
        super(CCAExampleBlockEntities.ITEM_HOLDER, pos, state);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        // this super call is important for the CCA component!
        super.readNbt(nbt, registryLookup);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        // this super call is important for the CCA component!
        super.writeNbt(nbt, registryLookup);
    }
}
