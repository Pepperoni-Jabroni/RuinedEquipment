package pepjebs.ruined_equipment.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import pepjebs.ruined_equipment.RuinedEquipmentMod;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RuinedEquipmentUtils {
    public static void onSendEquipmentBreakStatusImpl(ServerPlayerEntity serverPlayer, ItemStack breakingStack) {
        for (Map.Entry<Item, Item> itemMap : RuinedEquipmentMod.VANILLA_ITEM_MAP.entrySet()) {
            if (isVanillaItemStackBreaking(breakingStack, itemMap.getValue())) {
                ItemStack ruinedStack = new ItemStack(itemMap.getKey());
                // Set enchantment NBT data
                CompoundTag enchantTag = getTagForEnchantments(breakingStack, ruinedStack);
                if (enchantTag != null) ruinedStack.setTag(enchantTag);
                // Handle Ruined item name
                MutableText breakingToolName = new LiteralText(breakingStack.getName().getString());
                if (breakingStack.hasCustomName()) {
                    if (ruinedStack.hasGlint()) {
                        breakingToolName = breakingToolName.formatted(Formatting.AQUA);
                    }
                    ruinedStack.setCustomName(breakingToolName);
                }
                // Set the item in the correct index
                serverPlayer.inventory.main.set(serverPlayer.inventory.getSwappableHotbarSlot(), ruinedStack);
            }
        }
    }

    public static CompoundTag getTagForEnchantments(ItemStack breakingStack, ItemStack ruinedStack) {
        Set<String> enchantmentStrings = new HashSet<>();
        for (Map.Entry<Enchantment, Integer> ench : EnchantmentHelper.get(breakingStack).entrySet()) {
            String enchantString = ench.getKey().getName(ench.getValue()).getString();
            enchantmentStrings.add(enchantString);
        }
        if (!enchantmentStrings.isEmpty()) {
            CompoundTag tag = ruinedStack.getTag();
            if (tag == null) tag = new CompoundTag();
            tag.putString("enchantments", String.join(",", enchantmentStrings));
            return tag;
        }
        return null;
    }

    public static boolean isVanillaItemStackBreaking(ItemStack breakingStack, Item vanillaItem) {
        return breakingStack.isItemEqualIgnoreDamage(new ItemStack(vanillaItem))
            && breakingStack.getMaxDamage() - breakingStack.getDamage() <= 0;
    }
}
