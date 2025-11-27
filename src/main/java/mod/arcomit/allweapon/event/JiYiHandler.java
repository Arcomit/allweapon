package mod.arcomit.allweapon.event;

import mod.arcomit.allweapon.init.AwBlades;
import mod.arcomit.allweapon.util.enchantment.EnchantmentUtils;
import mod.arcomit.allweapon.util.fluid.FluidDetectionUtils;
import mod.arcomit.allweapon.util.structure.StructurePattern;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.recipe.RequestDefinition;
import mods.flammpfeil.slashblade.recipe.SlashBladeIngredient;
import mods.flammpfeil.slashblade.registry.slashblade.EnchantmentDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-20 12:54
 * @Description: 极意获取方法
 */
@Mod.EventBusSubscriber
public class JiYiHandler {
    private static final StructurePattern STRUCTURE_PATTERN = new StructurePattern()
            .addBlock(new BlockPos(0,-1,0), blockState -> blockState.is(BlockTags.SOUL_SPEED_BLOCKS))
            .addBlock(new BlockPos(-1,-1,-1), Blocks.ICE)
            .addBlock(new BlockPos(1,-1,1), Blocks.ICE)
            .addBlock(new BlockPos(1,-1,-1), Blocks.FIRE)
            .addBlock(new BlockPos(-1,-1,1), Blocks.FIRE);

    @SubscribeEvent
    public static void bladeStandOnHit(SlashBladeEvent.BladeStandAttackEvent event) {
        var bladeStand = event.getBladeStand();

        if (!FluidDetectionUtils.isInLava(bladeStand)){
            return;
        }
        var level = bladeStand.level();

        var slashBladeDefinitionRegistry = SlashBlade.getSlashBladeDefinitionRegistry(level);
        if (!slashBladeDefinitionRegistry.containsKey(AwBlades.JIYI)) {
            return;
        }

        var damageSource = event.getDamageSource();
        if (!(damageSource.getEntity() instanceof Creeper)) {
            return;
        }

        if (!damageSource.is(DamageTypeTags.IS_EXPLOSION)) {
            return;
        }

        var blade = event.getBlade();
        var in = SlashBladeIngredient.of(RequestDefinition.Builder.newInstance()
                .refineCount(50)
                .addEnchantment(
                        new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.UNBREAKING), 3)
                )
                .build()
        );
        if (!in.test(blade)){
            return;
        }

        var standPos = bladeStand.getPos();

        if (STRUCTURE_PATTERN.check(level, standPos) != null) {
            ItemStack newBlade = Objects.requireNonNull(slashBladeDefinitionRegistry.get(AwBlades.JIYI)).getBlade();
            var newBladeState = newBlade.getCapability(ItemSlashBlade.BLADESTATE).orElseThrow(NullPointerException::new);
            var oldBladeState = event.getSlashBladeState();
            newBladeState.setProudSoulCount(oldBladeState.getProudSoulCount());
            newBladeState.setKillCount(oldBladeState.getKillCount());
            newBladeState.setRefine(oldBladeState.getRefine());
            newBlade.getOrCreateTag().put("bladeState", newBladeState.serializeNBT());
            EnchantmentUtils.updateEnchantment(newBlade, blade);
            bladeStand.setItem(newBlade);

            event.setCanceled(true);
        }
    }
}
