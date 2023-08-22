package mod.maxbogomol.wizards_reborn.client.render.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.maxbogomol.wizards_reborn.client.event.ClientTickHandler;
import mod.maxbogomol.wizards_reborn.common.tileentity.WissenCrystallizerTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemDisplayContext;

public class WissenCrystallizerTileEntityRenderer implements BlockEntityRenderer<WissenCrystallizerTileEntity> {

    public WissenCrystallizerTileEntityRenderer() {}

    @Override
    public void render(WissenCrystallizerTileEntity сrystallizer, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();

        double ticks = (ClientTickHandler.ticksInGame + partialTicks) * 2;
        double ticksUp = (ClientTickHandler.ticksInGame + partialTicks) * 4;
        ticksUp = (ticksUp) % 360;

        ms.pushPose();
        ms.translate(0.5F, 1.25F, 0.5F);
        ms.translate(0F, (float) (Math.sin(Math.toRadians(ticksUp)) * 0.03125F), 0F);
        ms.mulPose(Axis.YP.rotationDegrees((float) ticks));
        ms.scale(0.5F, 0.5F, 0.5F);
        mc.getItemRenderer().renderStatic(сrystallizer.getItemHandler().getItem(0), ItemDisplayContext.FIXED, light, overlay, ms, buffers, сrystallizer.getLevel(), 0);
        ms.popPose();

        int size = сrystallizer.getInventorySize();
        float rotate = 360f / (size - 1);

        if (size > 1) {
            for (int i = 0; i < size - 1; i++) {
                ms.pushPose();
                ms.translate(0.5F, 1.125F, 0.5F);
                ms.translate(0F, (float) Math.sin(Math.toRadians(ticksUp + (rotate * i))) * 0.0625F, 0F);
                ms.mulPose(Axis.YP.rotationDegrees((float) -ticks + ((i - 1) * rotate)));
                ms.translate(0.5F, 0F, 0F);
                ms.mulPose(Axis.YP.rotationDegrees(90f));
                ms.scale(0.25F, 0.25F, 0.25F);
                mc.getItemRenderer().renderStatic(сrystallizer.getItemHandler().getItem(i + 1), ItemDisplayContext.FIXED, light, overlay, ms, buffers, сrystallizer.getLevel(), 0);
                ms.popPose();
            }
        }
    }
}
