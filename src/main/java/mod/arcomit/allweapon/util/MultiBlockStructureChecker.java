package mod.arcomit.allweapon.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.function.Predicate;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-17
 * @Description: 多方块结构检测工具类，支持四个方向的结构判断
 */
public class MultiBlockStructureChecker {

    /**
     * 检测结构是否匹配（自动检测所有四个方向）
     * @param level 世界
     * @param startPos 起始位置
     * @param pattern 结构模板
     * @return 匹配的方向，如果没有匹配则返回null
     */
    public static Direction checkStructureAllDirections(Level level, BlockPos startPos, StructurePattern pattern) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (checkStructure(level, startPos, pattern, direction)) {
                return direction;
            }
        }
        return null;
    }

    /**
     * 检测指定方向的结构是否匹配
     * @param level 世界
     * @param startPos 起始位置
     * @param pattern 结构模板
     * @param facing 面向方向
     * @return 是否匹配
     */
    public static boolean checkStructure(Level level, BlockPos startPos, StructurePattern pattern, Direction facing) {
        for (Map.Entry<BlockPos, Predicate<BlockState>> entry : pattern.getPattern().entrySet()) {
            BlockPos relativePos = entry.getKey();
            BlockPos worldPos = transformPosition(startPos, relativePos, facing);
            BlockState state = level.getBlockState(worldPos);

            if (!entry.getValue().test(state)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将相对坐标转换为世界坐标（根据朝向旋转）
     * @param origin 原点位置
     * @param relative 相对位置
     * @param facing 面向方向
     * @return 世界坐标
     */
    private static BlockPos transformPosition(BlockPos origin, BlockPos relative, Direction facing) {
        int x = relative.getX();
        int y = relative.getY();
        int z = relative.getZ();

        // 根据朝向旋转坐标
        // 默认朝向为北（NORTH），即 -Z 方向
        int transformedX, transformedZ;
        switch (facing) {
            case NORTH: // 北 (-Z)
                transformedX = x;
                transformedZ = z;
                break;
            case SOUTH: // 南 (+Z)
                transformedX = -x;
                transformedZ = -z;
                break;
            case WEST: // 西 (-X)
                transformedX = z;
                transformedZ = -x;
                break;
            case EAST: // 东 (+X)
                transformedX = -z;
                transformedZ = x;
                break;
            default:
                transformedX = x;
                transformedZ = z;
        }

        return origin.offset(transformedX, y, transformedZ);
    }

    /**
     * 获取结构中所有方块的世界坐标
     * @param startPos 起始位置
     * @param pattern 结构模板
     * @param facing 面向方向
     * @return 所有方块的世界坐标数组
     */
    public static BlockPos[] getStructurePositions(BlockPos startPos, StructurePattern pattern, Direction facing) {
        return pattern.getPattern().keySet().stream()
                .map(relativePos -> transformPosition(startPos, relativePos, facing))
                .toArray(BlockPos[]::new);
    }
}

