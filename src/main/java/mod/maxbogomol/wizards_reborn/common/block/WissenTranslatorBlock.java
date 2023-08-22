package mod.maxbogomol.wizards_reborn.common.block;

import mod.maxbogomol.wizards_reborn.common.tileentity.TileSimpleInventory;
import mod.maxbogomol.wizards_reborn.common.tileentity.WissenTranslatorTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WissenTranslatorBlock extends FaceAttachedHorizontalDirectionalBlock implements EntityBlock, SimpleWaterloggedBlock  {

    private static final VoxelShape SHAPE_D = Stream.of(
            Block.box(5, 0, 5, 11, 1, 11),
            Block.box(6, 1, 6, 10, 2, 10),
            Block.box(7, 2, 7, 9, 5, 9),
            Block.box(6, 5, 6, 10, 6, 10),
            Block.box(7, 6, 7, 9, 7, 9)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_T = Stream.of(
            Block.box(5, 15, 5, 11, 16, 11),
            Block.box(6, 14, 6, 10, 15, 10),
            Block.box(7, 11, 7, 9, 14, 9),
            Block.box(6, 10, 6, 10, 11, 10),
            Block.box(7, 9, 7, 9, 10, 9)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_N = Stream.of(
            Block.box(5, 5, 15, 11, 11, 16),
            Block.box(6, 6, 14, 10, 10, 15),
            Block.box(7, 7, 11, 9, 9, 14),
            Block.box(6, 6, 10, 10, 10, 11),
            Block.box(7, 7, 9, 9, 9, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_S = Stream.of(
            Block.box(5, 5, 0, 11, 11, 1),
            Block.box(6, 6, 1, 10, 10, 2),
            Block.box(7, 7, 2, 9, 9, 5),
            Block.box(6, 6, 5, 10, 10, 6),
            Block.box(7, 7, 6, 9, 9, 7)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_W = Stream.of(
            Block.box(15, 5, 5, 16, 11, 11),
            Block.box(14, 6, 6, 15, 10, 10),
            Block.box(11, 7, 7, 14, 9, 9),
            Block.box(10, 6, 6, 11, 10, 10),
            Block.box(9, 7, 7, 10, 9, 9)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_E = Stream.of(
            Block.box(0, 5, 5, 1, 11, 11),
            Block.box(1, 6, 6, 2, 10, 10),
            Block.box(2, 7, 7, 5, 9, 9),
            Block.box(5, 6, 6, 6, 10, 10),
            Block.box(6, 7, 7, 7, 9, 9)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public WissenTranslatorBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        switch (state.getValue(FACE)) {
            case FLOOR:
                return SHAPE_D;
            case WALL:
                switch (state.getValue(FACING)) {
                    case NORTH:
                        return SHAPE_N;
                    case SOUTH:
                        return SHAPE_S;
                    case WEST:
                        return SHAPE_W;
                    case EAST:
                        return SHAPE_E;
                    default:
                        return SHAPE_N;
                }
            case CEILING:
                return SHAPE_T;
            default:
                return SHAPE_D;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(FACE);
        builder.add(BlockStateProperties.WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        for(Direction direction : context.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockstate = this.defaultBlockState().setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(FACING, context.getHorizontalDirection()).setValue(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
            } else {
                blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction.getOpposite()).setValue(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
            }

            if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockstate;
            }
        }
        return this.defaultBlockState();
    }

    @Override
    public void onRemove(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof TileSimpleInventory) {
                net.minecraft.world.Containers.dropContents(world, pos, ((TileSimpleInventory) tile).getItemHandler());
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {


        return InteractionResult.PASS;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (pState.getValue(BlockStateProperties.WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int id, int param) {
        super.triggerEvent(state, world, pos, id, param);
        BlockEntity tileentity = world.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(id, param);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WissenTranslatorTileEntity(pPos, pState);
    }
}
