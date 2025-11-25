package mod.arcomit.allweapon.event;

import mod.arcomit.allweapon.init.AwBlades;
import mod.arcomit.allweapon.util.EnchantmentUtils;
import mod.arcomit.allweapon.util.MultiBlockStructureChecker;
import mod.arcomit.allweapon.util.StructurePattern;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.recipe.RequestDefinition;
import mods.flammpfeil.slashblade.recipe.SlashBladeIngredient;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-20 12:54
 * @Description: 奉神刀获取方法
 */
@Mod.EventBusSubscriber
public class FengShenDaoHandler {
    private static final StructurePattern STRUCTURE_PATTERN = new StructurePattern()
            .addBlock(new BlockPos(0,-1,0), Blocks.BEACON)
            .addBlock(new BlockPos(0,-2,0), blockState -> blockState.is(BlockTags.BEACON_BASE_BLOCKS))
            .addBlock(new BlockPos(0,-2,1), blockState -> blockState.is(BlockTags.BEACON_BASE_BLOCKS))
            .addBlock(new BlockPos(0,-2,-1), blockState -> blockState.is(BlockTags.BEACON_BASE_BLOCKS))
            .addBlock(new BlockPos(1,-2,0), blockState -> blockState.is(BlockTags.BEACON_BASE_BLOCKS))
            .addBlock(new BlockPos(1,-2,1), blockState -> blockState.is(BlockTags.BEACON_BASE_BLOCKS))
            .addBlock(new BlockPos(1,-2,-1), blockState -> blockState.is(BlockTags.BEACON_BASE_BLOCKS))
            .addBlock(new BlockPos(-1,-2,0), blockState -> blockState.is(BlockTags.BEACON_BASE_BLOCKS))
            .addBlock(new BlockPos(-1,-2,1), blockState -> blockState.is(BlockTags.BEACON_BASE_BLOCKS))
            .addBlock(new BlockPos(-1,-2,-1), blockState -> blockState.is(BlockTags.BEACON_BASE_BLOCKS))
            .addBlock(new BlockPos(1,-1,1), Blocks.OBSIDIAN)
            .addBlock(new BlockPos(1,-1,-1), Blocks.OBSIDIAN)
            .addBlock(new BlockPos(-1,-1,1), Blocks.OBSIDIAN)
            .addBlock(new BlockPos(-1,-1,-1), Blocks.OBSIDIAN)
            .addBlock(new BlockPos(1,1,1), Blocks.OBSIDIAN)
            .addBlock(new BlockPos(1,1,-1), Blocks.OBSIDIAN)
            .addBlock(new BlockPos(-1,1,1), Blocks.OBSIDIAN)
            .addBlock(new BlockPos(-1,1,-1), Blocks.OBSIDIAN)
            .addBlock(new BlockPos(2,0,0), Blocks.QUARTZ_BLOCK)
            .addBlock(new BlockPos(-2,0,0), Blocks.QUARTZ_BLOCK)
            .addBlock(new BlockPos(0,0,2), Blocks.QUARTZ_BLOCK)
            .addBlock(new BlockPos(0,0,-2), Blocks.QUARTZ_BLOCK)
            .addBlock(new BlockPos(0,2,0), Blocks.GLASS);

    @SubscribeEvent
    public static void bladeStandOnHit(SlashBladeEvent.BladeStandAttackEvent event) {
        var bladeStand = event.getBladeStand();
        var level = bladeStand.level();

        var slashBladeDefinitionRegistry = SlashBlade.getSlashBladeDefinitionRegistry(level);
        if (!slashBladeDefinitionRegistry.containsKey(AwBlades.FENGSHEN)) {
            return;
        }

        if (!(event.getDamageSource().getEntity() instanceof ServerPlayer player)) {
            return;
        }

        var handStack = player.getMainHandItem();
        if (handStack.getItem() != SlashBladeItems.PROUDSOUL.get()) {
            return;
        }

        var blade = event.getBlade();
        var in = SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                .refineCount(500)
                .build()
        );
        if (!in.test(blade)){
            return;
        }

        var standPos = bladeStand.getPos();
        if (standPos.getY() > -58){
            return;
        }

        Direction facing = STRUCTURE_PATTERN.check(level, standPos);
        if (facing != null) {
            ItemStack newBlade = Objects.requireNonNull(slashBladeDefinitionRegistry.get(AwBlades.FENGSHEN)).getBlade();
            var newBladeState = newBlade.getCapability(ItemSlashBlade.BLADESTATE).orElseThrow(NullPointerException::new);
            var oldBladeState = event.getSlashBladeState();
            newBladeState.setProudSoulCount(oldBladeState.getProudSoulCount());
            newBladeState.setKillCount(oldBladeState.getKillCount());
            newBladeState.setRefine(oldBladeState.getRefine());
            newBlade.getOrCreateTag().put("bladeState", newBladeState.serializeNBT());
            EnchantmentUtils.updateEnchantment(newBlade, blade);
            bladeStand.setItem(newBlade);

            for (BlockPos pos : MultiBlockStructureChecker.getStructurePositions(standPos, STRUCTURE_PATTERN, facing)) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
            event.setCanceled(true);
        }
    }
}
