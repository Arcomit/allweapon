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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-20 12:54
 * @Description: 绿萝获取方法
 */
@Mod.EventBusSubscriber
public class LvLuoHandler {
    private static final StructurePattern STRUCTURE_PATTERN = new StructurePattern()
            .addBlock(new BlockPos(0,-1,0), blockState -> blockState.is(BlockTags.LEAVES));

    @SubscribeEvent
    public static void bladeStandOnHit(SlashBladeEvent.BladeStandAttackEvent event) {
        var bladeStand = event.getBladeStand();
        var level = bladeStand.level();

        var slashBladeDefinitionRegistry = SlashBlade.getSlashBladeDefinitionRegistry(level);
        if (!slashBladeDefinitionRegistry.containsKey(AwBlades.LVLUO)) {
            return;
        }

        var damageSource = event.getDamageSource();
        if (!damageSource.is(DamageTypeTags.IS_PROJECTILE)) {
            return;
        }

        if (damageSource.getEntity() instanceof ServerPlayer) {
            return;
        }

         if (!(damageSource.getDirectEntity() instanceof AbstractArrow)) {
             return;
         }

        var blade = event.getBlade();
        var in = SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                .refineCount(40)
                .build()
        );
        if (!in.test(blade)){
            return;
        }

        var standPos = bladeStand.getPos();
        var biome = level.getBiome(standPos);
        if (!biome.is(Biomes.PLAINS) && !biome.is(Biomes.SUNFLOWER_PLAINS)) {
            return;
        }

        Direction facing = STRUCTURE_PATTERN.check(level, standPos);
        if (facing != null) {
            ItemStack newBlade = Objects.requireNonNull(slashBladeDefinitionRegistry.get(AwBlades.LVLUO)).getBlade();
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
