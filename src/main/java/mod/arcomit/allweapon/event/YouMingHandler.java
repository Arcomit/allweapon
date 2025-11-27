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
import mods.flammpfeil.slashblade.registry.slashblade.EnchantmentDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-20 12:54
 * @Description: 幽冥获取方法
 */
@Mod.EventBusSubscriber
public class YouMingHandler {
    private static final StructurePattern STRUCTURE_PATTERN = new StructurePattern()
            .addBlock(new BlockPos(1,0,0), blockState -> blockState.is(BlockTags.SOUL_SPEED_BLOCKS))
            .addBlock(new BlockPos(1,1,0), blockState -> blockState.is(BlockTags.SOUL_SPEED_BLOCKS))
            .addBlock(new BlockPos(1,1,-1), blockState -> blockState.is(BlockTags.SOUL_SPEED_BLOCKS))
            .addBlock(new BlockPos(1,1,1), blockState -> blockState.is(BlockTags.SOUL_SPEED_BLOCKS))
            .addBlock(new BlockPos(1,2,0), blockState -> blockState.is(Blocks.PUMPKIN) || blockState.is(Blocks.CARVED_PUMPKIN) || blockState.is(Blocks.JACK_O_LANTERN));

    @SubscribeEvent
    public static void bladeStandOnHit(SlashBladeEvent.BladeStandAttackEvent event) {
        var bladeStand = event.getBladeStand();
        var level = bladeStand.level();

        var slashBladeDefinitionRegistry = SlashBlade.getSlashBladeDefinitionRegistry(level);
        if (!slashBladeDefinitionRegistry.containsKey(AwBlades.YOUMING)) {
            return;
        }

        if (!(event.getDamageSource().getEntity() instanceof Ghast)) {
            return;
        }

        var blade = event.getBlade();
        var in = SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                .refineCount(100)
                .addEnchantment(
                        new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.SMITE), 4),
                        new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.MOB_LOOTING), 3)
                )
                .build()
        );
        if (!in.test(blade)){
            return;
        }

        var standPos = bladeStand.getPos();
        var biome = level.getBiome(standPos);
        if (!biome.is(Biomes.SOUL_SAND_VALLEY)) {
            return;
        }

        Direction facing = STRUCTURE_PATTERN.check(level, standPos);
        if (facing != null) {
            ItemStack newBlade = Objects.requireNonNull(slashBladeDefinitionRegistry.get(AwBlades.YOUMING)).getBlade();
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
