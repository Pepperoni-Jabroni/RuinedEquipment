package pepjebs.ruined_equipment.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class RuinedEquipmentUtils {
    public static void onSendEquipmentBreakStatusImpl(
            ServerPlayerEntity serverPlayer,
            ItemStack breakingStack,
            boolean forceSet) {
        for (Map.Entry<Item, Item> itemMap : RuinedEquipmentItems.VANILLA_ITEM_MAP.entrySet()) {
            if (isVanillaItemStackBreaking(breakingStack, itemMap.getValue())) {
                // Crossbows don't play nicely for some reason...
                if (breakingStack.isItemEqualIgnoreDamage(new ItemStack(Items.CROSSBOW))) forceSet = false;
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
                // Handle Leather Armors color
                if (breakingStack.getItem() instanceof DyeableItem) {
                    int breakingColor = ((DyeableItem) breakingStack.getItem()).getColor(breakingStack);
                    ((DyeableItem) ruinedStack.getItem()).setColor(ruinedStack, breakingColor);
                    DyeableItem.blendAndSetColor(ruinedStack, new LinkedList<>());
                }
                // Force set will place the Ruined item in hand
                if (forceSet) {
                    serverPlayer.inventory.setStack(serverPlayer.inventory.selectedSlot, ruinedStack);
                } else {
                    serverPlayer.inventory.insertStack(ruinedStack);
                }
            }
        }
    }

    public static CompoundTag getTagForEnchantments(ItemStack breakingStack, ItemStack ruinedStack) {
        Set<String> enchantmentStrings = new HashSet<>();
        for (Map.Entry<Enchantment, Integer> ench : EnchantmentHelper.get(breakingStack).entrySet()) {
            String enchantString = Registry.ENCHANTMENT.getId(ench.getKey())+"_"+ench.getValue();
            RuinedEquipmentMod.LOGGER.info("Adding enchantment: " + enchantString);
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
