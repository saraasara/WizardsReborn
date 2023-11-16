package mod.maxbogomol.wizards_reborn.common.tileentity;

import mod.maxbogomol.wizards_reborn.WizardsReborn;
import mod.maxbogomol.wizards_reborn.client.particle.Particles;
import mod.maxbogomol.wizards_reborn.utils.PacketUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public class FluidPipeTileEntity extends FluidPipeBaseTileEntity {
    IFluidHandler[] sideHandlers;

    @Override
    protected void initFluidTank() {
        super.initFluidTank();
        sideHandlers = new IFluidHandler[Direction.values().length];
        for (Direction facing : Direction.values()) {
            sideHandlers[facing.get3DDataValue()] = new IFluidHandler() {

                @Override
                public int fill(FluidStack resource, FluidAction action) {
                    if(action.execute())
                        setFrom(facing, true);
                    return tank.fill(resource, action);
                }

                @Nullable
                @Override
                public FluidStack drain(FluidStack resource, FluidAction action) {
                    return tank.drain(resource, action);
                }

                @Nullable
                @Override
                public FluidStack drain(int maxDrain, FluidAction action) {
                    return tank.drain(maxDrain, action);
                }

                @Override
                public int getTanks() {
                    return tank.getTanks();
                }

                @Override
                public @NotNull FluidStack getFluidInTank(int tankNum) {
                    return tank.getFluidInTank(tankNum);
                }

                @Override
                public int getTankCapacity(int tankNum) {
                    return tank.getTankCapacity(tankNum);
                }

                @Override
                public boolean isFluidValid(int tankNum, @NotNull FluidStack stack) {
                    return tank.isFluidValid(tankNum, stack);
                }
            };
        }
    }

    public FluidPipeTileEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        initFluidTank();
    }

    public FluidPipeTileEntity(BlockPos pos, BlockState state) {
        this(WizardsReborn.FLUID_PIPE_TILE_ENTITY.get(), pos, state);
        initFluidTank();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == ForgeCapabilities.FLUID_HANDLER) {
            return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, holder);
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide) {
            PacketUtils.SUpdateTileEntityPacket(this);
        }
    }

    @Override
    public int getCapacity() {
        return 350;
    }
}
