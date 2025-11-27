package mod.arcomit.allweapon.util.enchantment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-20 15:36
 * @Description: TODO
 */
public class EnchantmentUtils {
    public static ResourceLocation getEnchantmentID(Enchantment enchantment) {
        return ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
    }

    /**
     * 将 ingredient 物品的附魔合并到 result 物品上
     * 类似于铁砧合并逻辑：相同附魔取高等级
     *
     * @param result 目标物品（接收附魔）
     * @param ingredient 源物品（提供附魔）
     */
    public static void updateEnchantment(ItemStack result, ItemStack ingredient) {
        var resultEnchants = result.getAllEnchantments();
        var ingredientEnchants = ingredient.getAllEnchantments();

        for (var entry : ingredientEnchants.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int ingredientLevel = entry.getValue();
            int resultLevel = resultEnchants.getOrDefault(enchantment, 0);

            int finalLevel = Math.min(
                Math.max(ingredientLevel, resultLevel),
                enchantment.getMaxLevel()
            );

            resultEnchants.put(enchantment, finalLevel);
        }

        EnchantmentHelper.setEnchantments(resultEnchants, result);
    }
}
