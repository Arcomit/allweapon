package mod.arcomit.allweapon.event;

import mod.arcomit.allweapon.init.AwBlades;
import mod.arcomit.allweapon.util.enchantment.EnchantmentUtils;
import mod.arcomit.allweapon.util.structure.MultiBlockStructureChecker;
import mod.arcomit.allweapon.util.structure.StructurePattern;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.recipe.RequestDefinition;
import mods.flammpfeil.slashblade.recipe.SlashBladeIngredient;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-20 12:54
 * @Description: 花之妖精获取方法
 */
@Mod.EventBusSubscriber
public class HuaMiHandler {
    private static final StructurePattern STRUCTURE_PATTERN = new StructurePattern()
            .addBlock(new BlockPos(0,0,0), blockState -> blockState.is(BlockTags.FLOWERS))
            .addBlock(new BlockPos(0,0,1), blockState -> blockState.is(BlockTags.FLOWERS))
            .addBlock(new BlockPos(0,0,-1), blockState -> blockState.is(BlockTags.FLOWERS))
            .addBlock(new BlockPos(1,0,0), blockState -> blockState.is(BlockTags.FLOWERS))
            .addBlock(new BlockPos(1,0,1), blockState -> blockState.is(BlockTags.FLOWERS))
            .addBlock(new BlockPos(1,0,-1), blockState -> blockState.is(BlockTags.FLOWERS))
            .addBlock(new BlockPos(-1,0,0), blockState -> blockState.is(BlockTags.FLOWERS))
            .addBlock(new BlockPos(-1,0,1), blockState -> blockState.is(BlockTags.FLOWERS))
            .addBlock(new BlockPos(-1,0,-1), blockState -> blockState.is(BlockTags.FLOWERS));

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void bladeStandOnHit(SlashBladeEvent.BladeStandAttackEvent event) {
        var bladeStand = event.getBladeStand();
        var level = bladeStand.level();

        var slashBladeDefinitionRegistry = SlashBlade.getSlashBladeDefinitionRegistry(level);
        if (!slashBladeDefinitionRegistry.containsKey(AwBlades.HUAMI)) {
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
                .refineCount(10)
                .build()
        );
        if (!in.test(blade)){
            return;
        }

        var standPos = bladeStand.getPos();
        var biome = level.getBiome(standPos);
        if (!biome.is(BiomeTags.IS_FOREST)) {
            return;
        }

        Direction facing = STRUCTURE_PATTERN.check(level, standPos);
        if (facing != null) {
            ItemStack newBlade = Objects.requireNonNull(slashBladeDefinitionRegistry.get(AwBlades.HUAMI)).getBlade();
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
