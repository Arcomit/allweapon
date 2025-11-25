package mod.arcomit.allweapon.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import mod.arcomit.allweapon.AllWeapon;
import mod.arcomit.allweapon.init.AwItems;
import mods.flammpfeil.slashblade.compat.jei.JEICompat;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.resources.ResourceLocation;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-08-02 16:48
 * @Description: JEI合成表兼容(JEI Compat)
 */
@SuppressWarnings("removal")
@JeiPlugin
public class AsbJeiCompat implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(AllWeapon.MODID, "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        AwItems.ITEMS.getEntries().forEach(item -> {
            if (item.get() instanceof ItemSlashBlade blade) {
                registration.registerSubtypeInterpreter(blade, JEICompat::syncSlashBlade);
            }
        });
    }
}
