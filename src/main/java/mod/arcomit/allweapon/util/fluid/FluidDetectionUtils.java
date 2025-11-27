package mod.arcomit.allweapon.util.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

/**
 * 流体检测帮助类
 * 用于检测实体是否在特定流体中
 * 特别适用于物品展示框等特殊实体的流体检测
 */
public class FluidDetectionUtils {

    /**
     * 检查实体是否在岩浆中
     * 使用多种检测方法确保准确性
     *
     * @param entity 要检测的实体
     * @return 如果实体在岩浆中返回 true，否则返回 false
     */
    public static boolean isInLava(Entity entity) {
        return isInFluid(entity, Fluids.LAVA, Fluids.FLOWING_LAVA) ||
               isInBlock(entity, Blocks.LAVA);
    }

    /**
     * 检查实体是否在水中
     * 使用多种检测方法确保准确性
     *
     * @param entity 要检测的实体
     * @return 如果实体在水中返回 true，否则返回 false
     */
    public static boolean isInWater(Entity entity) {
        return isInFluid(entity, Fluids.WATER, Fluids.FLOWING_WATER) ||
               isInBlock(entity, Blocks.WATER);
    }

    /**
     * 检查实体是否在指定的流体中
     *
     * @param entity 要检测的实体
     * @param fluids 要检测的流体类型（可变参数）
     * @return 如果实体在任意一种指定流体中返回 true，否则返回 false
     */
    public static boolean isInFluid(Entity entity, Fluid... fluids) {
        if (entity == null || fluids == null || fluids.length == 0) {
            return false;
        }

        Level level = entity.level();
        BlockPos pos = entity.blockPosition();

        // 方法1: 检查实体位置的流体状态
        var fluidState = level.getFluidState(pos);
        for (Fluid fluid : fluids) {
            if (fluidState.is(fluid)) {
                return true;
            }
        }

        // 方法2: 检查实体碰撞箱范围内的流体
        AABB boundingBox = entity.getBoundingBox();
        int minX = Mth.floor(boundingBox.minX);
        int minY = Mth.floor(boundingBox.minY);
        int minZ = Mth.floor(boundingBox.minZ);
        int maxX = Mth.floor(boundingBox.maxX);
        int maxY = Mth.floor(boundingBox.maxY);
        int maxZ = Mth.floor(boundingBox.maxZ);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos checkPos = new BlockPos(x, y, z);
                    var checkFluidState = level.getFluidState(checkPos);
                    for (Fluid fluid : fluids) {
                        if (checkFluidState.is(fluid)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * 检查实体是否在指定的方块中
     *
     * @param entity 要检测的实体
     * @param blocks 要检测的方块类型（可变参数）
     * @return 如果实体在任意一种指定方块中返回 true，否则返回 false
     */
    public static boolean isInBlock(Entity entity, net.minecraft.world.level.block.Block... blocks) {
        if (entity == null || blocks == null || blocks.length == 0) {
            return false;
        }

        Level level = entity.level();
        BlockPos pos = entity.blockPosition();

        // 方法1: 检查实体位置的方块
        var blockState = level.getBlockState(pos);
        for (var block : blocks) {
            if (blockState.is(block)) {
                return true;
            }
        }

        // 方法2: 检查实体碰撞箱范围内的方块
        AABB boundingBox = entity.getBoundingBox();
        int minX = Mth.floor(boundingBox.minX);
        int minY = Mth.floor(boundingBox.minY);
        int minZ = Mth.floor(boundingBox.minZ);
        int maxX = Mth.floor(boundingBox.maxX);
        int maxY = Mth.floor(boundingBox.maxY);
        int maxZ = Mth.floor(boundingBox.maxZ);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos checkPos = new BlockPos(x, y, z);
                    var checkBlockState = level.getBlockState(checkPos);
                    for (var block : blocks) {
                        if (checkBlockState.is(block)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * 获取实体所在位置的流体深度（0.0 到 1.0）
     *
     * @param entity 要检测的实体
     * @param fluid 要检测的流体类型
     * @return 流体深度，0.0 表示没有流体，1.0 表示完全浸没
     */
    public static float getFluidDepth(Entity entity, Fluid fluid) {
        if (entity == null || fluid == null) {
            return 0.0f;
        }

        Level level = entity.level();
        BlockPos pos = entity.blockPosition();
        var fluidState = level.getFluidState(pos);

        if (!fluidState.is(fluid)) {
            return 0.0f;
        }

        return fluidState.getHeight(level, pos);
    }

    /**
     * 检查实体是否完全浸没在流体中
     *
     * @param entity 要检测的实体
     * @param fluids 要检测的流体类型（可变参数）
     * @return 如果实体完全浸没在流体中返回 true，否则返回 false
     */
    public static boolean isFullySubmerged(Entity entity, Fluid... fluids) {
        if (entity == null || fluids == null || fluids.length == 0) {
            return false;
        }

        Level level = entity.level();
        AABB boundingBox = entity.getBoundingBox();

        // 检查实体上方是否也有流体
        BlockPos topPos = new BlockPos(
            Mth.floor(entity.getX()),
            Mth.floor(boundingBox.maxY),
            Mth.floor(entity.getZ())
        );

        var topFluidState = level.getFluidState(topPos);
        for (Fluid fluid : fluids) {
            if (topFluidState.is(fluid)) {
                return true;
            }
        }

        return false;
    }
}

