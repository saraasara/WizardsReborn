package mod.maxbogomol.wizards_reborn.common.item;

import mod.maxbogomol.wizards_reborn.client.arcanemicon.ArcanemiconGui;
import mod.maxbogomol.wizards_reborn.common.block.ArcanePedestalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ArcanemiconItem extends Item {
    public ArcanemiconItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();

        if (player.isShiftKeyDown()) {
            if (stack.getItem() instanceof ArcanemiconItem) {
                Block block = ArcanePedestalBlock.blocksList.get(world.getBlockState(blockpos).getBlock());
                if (block != null) {
                    world.setBlockAndUpdate(blockpos, block.defaultBlockState());
                    player.getInventory().removeItem(player.getItemInHand(context.getHand()));
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.awardStat(Stats.ITEM_USED.get(this));

        if (world.isClientSide) {
            openGui();
        }

        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
    }

    @OnlyIn(Dist.CLIENT)
    public void openGui() {
        Minecraft.getInstance().player.playNotifySound(SoundEvents.BOOK_PAGE_TURN, SoundSource.NEUTRAL, 1.0f, 1.0f);
        Minecraft.getInstance().setScreen(new ArcanemiconGui());
    }
}
