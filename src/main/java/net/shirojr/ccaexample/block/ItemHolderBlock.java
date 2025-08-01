package net.shirojr.ccaexample.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.shirojr.ccaexample.block.entity.ItemHolderBlockEntity;
import net.shirojr.ccaexample.cca.component.SyncedBlockInventoryComponent;
import net.shirojr.ccaexample.init.CCAExampleTags;
import org.jetbrains.annotations.Nullable;

public class ItemHolderBlock extends BlockWithEntity {
    public static final MapCodec<ItemHolderBlock> CODEC = createCodec(ItemHolderBlock::new);

    public ItemHolderBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return ItemHolderBlock.createCuboidShape(3, 0, 3, 13, 11, 13);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ItemHolderBlockEntity(pos, state);
    }


    @Nullable
    public static ItemHolderBlockEntity getBlockEntity(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ItemHolderBlockEntity itemHolderBlockEntity) {
            return itemHolderBlockEntity;
        }
        return null;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemHolderBlockEntity blockEntity = getBlockEntity(world, pos);
        if (blockEntity == null) {
            return super.onUse(state, world, pos, player, hit);
        }

        SyncedBlockInventoryComponent syncedInventory = SyncedBlockInventoryComponent.fromBlockEntity(blockEntity);
        if (player.isSneaking() && syncedInventory.hasContent()) {
            ItemStack removedStack = syncedInventory.remove(true);
            if (removedStack != null && !removedStack.isEmpty()) {
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.BLOCKS);
                    if (!player.isCreative()) {
                        ItemScatterer.spawn(serverWorld, pos.getX(), pos.up().getY(), pos.getZ(), removedStack);
                    }
                }
                return ActionResult.SUCCESS;
            }
        }

        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stackInHand, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemHolderBlockEntity blockEntity = getBlockEntity(world, pos);
        if (!stackInHand.isIn(CCAExampleTags.ItemTags.DISPLAYABLE) || blockEntity == null) {
            return super.onUseWithItem(stackInHand, state, world, pos, player, hand, hit);
        }

        SyncedBlockInventoryComponent syncedInventory = SyncedBlockInventoryComponent.fromBlockEntity(blockEntity);
        if (!syncedInventory.isFull()) {
            boolean addedSuccessfully = syncedInventory.add(true, stackInHand);
            if (addedSuccessfully) {
                if (!player.isCreative()) {
                    stackInHand.decrement(stackInHand.getCount());
                }
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_PLACE, SoundCategory.BLOCKS);
                }
                return ItemActionResult.SUCCESS;
            }
        }
        return super.onUseWithItem(stackInHand, state, world, pos, player, hand, hit);
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (newState.isOf(this) || world.isClient()) {
            super.onStateReplaced(state, world, pos, newState, moved);
            return;
        }
        if (getBlockEntity(world, pos) instanceof ItemHolderBlockEntity blockEntity) {
            SyncedBlockInventoryComponent syncedInventory = SyncedBlockInventoryComponent.fromBlockEntity(blockEntity);
            if (syncedInventory.hasContent()) {
                ItemScatterer.spawn(world, pos.up(), syncedInventory.getInventory());
                syncedInventory.clear(true);
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }
}
