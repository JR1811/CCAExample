package net.shirojr.ccaexample.block.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.shirojr.ccaexample.block.entity.ItemHolderBlockEntity;
import net.shirojr.ccaexample.cca.component.SyncedBlockInventoryComponent;

public class ItemHolderBlockEntityRenderer<T extends ItemHolderBlockEntity> implements BlockEntityRenderer<T> {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final BlockEntityRendererFactory.Context context;

    public ItemHolderBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    @Override
    public void render(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null) return;
        SyncedBlockInventoryComponent syncedInventory = SyncedBlockInventoryComponent.fromBlockEntity(blockEntity);
        if (!syncedInventory.hasContent()) return;

        DefaultedList<ItemStack> inventory = syncedInventory.getInventory();
        float scale = 0.3f;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack entry = inventory.get(i);
            if (entry.isEmpty()) continue;
            int seed = (int) blockEntity.getPos().asLong() + i;

            matrices.push();
            matrices.translate(
                    getPosition(i, syncedInventory.getAge(), tickDelta).x,
                    getPosition(i, syncedInventory.getAge(), tickDelta).y,
                    getPosition(i, syncedInventory.getAge(), tickDelta).z
            );
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(syncedInventory.getAge() % 360));
            matrices.scale(scale, scale, scale);

            client.getItemRenderer().renderItem(entry, ModelTransformationMode.GUI, light, overlay, matrices, vertexConsumers, client.world, seed);
            matrices.pop();
        }
    }

    private static Vec3d getPosition(int index, long age, float tickDelta) {
        Vec3d result = Vec3d.ZERO;
        result = result.add(0, index == 0 ? 0.9 : 1.0, 0);
        result = switch (index) {
            case 0 -> result.add(0.5, 0, 0.5);
            case 1 -> result.add(1, 0, 0.5);
            case 2 -> result.add(0.5, 0, 0);
            case 3 -> result.add(0, 0, 0.5);
            case 4 -> result.add(0.5, 0, 1);
            default -> result;
        };
        double time = age + tickDelta;
        double cyclicHeight = Math.sin(time * 0.1);
        double floatingIntensity = 0.05;

        result = result.add(0, (cyclicHeight * floatingIntensity), 0);
        return result;
    }
}
