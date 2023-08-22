package mod.maxbogomol.wizards_reborn.common.block;

import mod.maxbogomol.wizards_reborn.common.tileentity.ArcaneWoodSignTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public class ArcaneWoodStandingSignBlock extends StandingSignBlock {
    public ArcaneWoodStandingSignBlock(Properties properties, WoodType type) {
        super(properties, type);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ArcaneWoodSignTileEntity(pPos, pState);
    }
}
