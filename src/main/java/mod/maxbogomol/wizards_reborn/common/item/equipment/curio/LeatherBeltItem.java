package mod.maxbogomol.wizards_reborn.common.item.equipment.curio;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import mod.maxbogomol.wizards_reborn.WizardsReborn;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import java.util.UUID;

public class LeatherBeltItem extends Item implements ICurioItem {

    private static final ResourceLocation BELT_TEXTURE = new ResourceLocation(WizardsReborn.MOD_ID,"textures/entity/curio/leather_belt.png");

    public LeatherBeltItem(Item.Properties properties) {
        super(properties);
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slot, ItemStack stack) {
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext,
                                                                        UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> atts = LinkedHashMultimap.create();
        atts.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "bonus", 1, AttributeModifier.Operation.ADDITION));
        return atts;
    }
/*
    @Override
    public boolean canRender(String identifier, int index, LivingEntity living, ItemStack stack) {
        return true;
    }

    @Override
    public void render(String identifier, int index, PoseStack matrixStack,
                       MultiBufferSource renderTypeBuffer, int light, LivingEntity living,
                       float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch, ItemStack stack) {
        ICurio.RenderHelper.translateIfSneaking(matrixStack, living);
        ICurio.RenderHelper.rotateIfSneaking(matrixStack, living);

        BeltModel<?> model = new BeltModel<>();
        Vec3 rotate = RenderUtils.followBodyRotation(living);
        model.model.yRot = (float) rotate.y();

        VertexConsumer vertexBuilder = ItemRenderer
                .getFoilBuffer(renderTypeBuffer, model.renderType(BELT_TEXTURE), false,
                        false);
        model
                .renderToBuffer(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
                        1.0F);
    }*/
}
