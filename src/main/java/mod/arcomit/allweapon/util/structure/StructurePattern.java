package mod.arcomit.allweapon.util.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-20 12:45
 * @Description: 结构模板，使用相对坐标定义，坐标格式：(x, y, z) 相对于起始方块的偏移量
 */
public class StructurePattern {
    private final Map<BlockPos, Predicate<BlockState>> pattern;

    public StructurePattern() {
        this.pattern = new HashMap<>();
    }

    /**
     * 添加方块检测条件
     *
     * @param relativePos 相对位置
     * @param predicate   方块状态判断条件
     */
    public StructurePattern addBlock(BlockPos relativePos, Predicate<BlockState> predicate) {
        pattern.put(relativePos, predicate);
        return this;
    }

    /**
     * 添加指定方块类型的检测
     *
     * @param relativePos 相对位置
     * @param block       目标方块
     */
    public StructurePattern addBlock(BlockPos relativePos, Block block) {
        return addBlock(relativePos, state -> state.is(block));
    }

    /**
     * 添加多个方块类型的检测（满足其一即可）
     *
     * @param relativePos 相对位置
     * @param blocks      目标方块列表
     */
    public StructurePattern addBlocks(BlockPos relativePos, Block... blocks) {
        return addBlock(relativePos, state -> {
            for (Block block : blocks) {
                if (state.is(block)) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * 添加空气方块检测
     * @param relativePos 相对位置
     */
    public StructurePattern addAir(BlockPos relativePos) {
        return addBlock(relativePos, BlockState::isAir);
    }

    public Map<BlockPos, Predicate<BlockState>> getPattern() {
        return pattern;
    }

    // ============ 便捷方法（语法糖） ============
    // 以下方法直接委托给 MultiBlockStructureChecker，提供更简洁的调用方式

    /**
     * 检测结构是否匹配（自动检测所有四个水平方向）
     * <p>
     * 便捷方法，等价于 MultiBlockStructureChecker.checkStructureAllDirections(level, startPos, this)
     *
     * @param level 世界
     * @param startPos 起始位置
     * @return 匹配的方向，如果没有匹配则返回null
     */
    public Direction check(Level level, BlockPos startPos) {
        return MultiBlockStructureChecker.checkStructureAllDirections(level, startPos, this);
    }

    /**
     * 检测指定方向的结构是否匹配
     * <p>
     * 便捷方法，等价于 MultiBlockStructureChecker.checkStructure(level, startPos, this, facing)
     *
     * @param level 世界
     * @param startPos 起始位置
     * @param facing 面向方向
     * @return 是否匹配
     */
    public boolean check(Level level, BlockPos startPos, Direction facing) {
        return MultiBlockStructureChecker.checkStructure(level, startPos, this, facing);
    }
}
