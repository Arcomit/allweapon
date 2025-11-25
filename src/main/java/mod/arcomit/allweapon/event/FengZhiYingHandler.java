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
import mods.flammpfeil.slashblade.registry.slashblade.EnchantmentDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-20 12:54
 * @Description: 风之影获取方法
 */
@Mod.EventBusSubscriber
public class FengZhiYingHandler {

    @SubscribeEvent
    public static void bladeStandOnHit(SlashBladeEvent.BladeStandAttackEvent event) {
        var bladeStand = event.getBladeStand();
        var level = bladeStand.level();

        var slashBladeDefinitionRegistry = SlashBlade.getSlashBladeDefinitionRegistry(level);
        if (!slashBladeDefinitionRegistry.containsKey(AwBlades.FENGZHIYING)) {
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
                .refineCount(100)
                .addEnchantment(
                        new EnchantmentDefinition(EnchantmentUtils.getEnchantmentID(Enchantments.FALL_PROTECTION), 4)
                )
                .build()
        );
        if (!in.test(blade)){
            return;
        }

        var standPos = bladeStand.getPos();
        if (standPos.getY() < 319){
            return;
        }

        ItemStack newBlade = Objects.requireNonNull(slashBladeDefinitionRegistry.get(AwBlades.FENGZHIYING)).getBlade();
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
