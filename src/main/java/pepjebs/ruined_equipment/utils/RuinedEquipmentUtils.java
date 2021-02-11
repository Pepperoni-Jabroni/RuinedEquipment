package pepjebs.ruined_equipment.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.item.RuinedEquipmentItems;
import pepjebs.ruined_equipment.recipe.RuinedEquipmentSmithingEmpowerRecipe;

import java.util.*;

public class RuinedEquipmentUtils {

    public static String RUINED_ENCHTS_TAG = "RuinedEnchantments";

    public static boolean ruinedItemHasEnchantment(ItemStack ruinedItem, Enchantment enchantment) {
        if (ruinedItem.getTag() == null) return false;
        String tagString = ruinedItem.getTag().getString(RUINED_ENCHTS_TAG);
        Map<Enchantment, Integer> enchantMap = RuinedEquipmentUtils.processEncodedEnchantments(tagString);
        if (enchantMap == null) return false;
        for (Enchantment e : enchantMap.keySet()) {
            if (e == enchantment) return true;
        }
        return false;
    }

    public static Item getEmpowermentApplicationItem() {
        if (RuinedEquipmentMod.CONFIG != null) {
            try {
                String[] itemId = RuinedEquipmentMod.CONFIG.empowermentSmithingItem.split(":");
                return Registry.ITEM.get(new Identifier(itemId[0], itemId[1]));
            } catch (Exception e) {
                RuinedEquipmentMod.LOGGER.warn(e.getMessage());
            }
        }
        return Items.NETHERITE_SCRAP;
    }

    public static int compareItemsById(Item i1, Item i2) {
        return Registry.ITEM.getId(i1).compareTo(Registry.ITEM.getId(i2));
    }

    public static int generateRepairLevelCost(ItemStack repaired, int maxLevel) {
        int targetLevel = maxLevel * (repaired.getMaxDamage() - repaired.getDamage()) / repaired.getMaxDamage();
        return Math.max(targetLevel, 1);
    }

    public static ItemStack generateRepairedItemForAnvilByFraction(
            ItemStack leftStack,
            double damageFraction) {
        int maxDamage = new ItemStack(RuinedEquipmentItems.getVanillaItemMap().get(leftStack.getItem())).getMaxDamage();
        return generateRepairedItemForAnvilByDamage(leftStack, (int) (damageFraction * (double) maxDamage));
    }

    public static ItemStack generateRepairedItemForAnvilByDamage(
            ItemStack leftStack,
            int targetDamage){
        boolean isMaxEnchant = leftStack.getTag() != null &&
                leftStack.getTag().contains(RuinedEquipmentSmithingEmpowerRecipe.RUINED_MAX_ENCHT_TAG)
                && leftStack.getTag().getBoolean(RuinedEquipmentSmithingEmpowerRecipe.RUINED_MAX_ENCHT_TAG);

        ItemStack repaired = new ItemStack(RuinedEquipmentItems.getVanillaItemMap().get(leftStack.getItem()));
        CompoundTag tag = leftStack.getOrCreateTag();
        String encodedEnch = tag.getString(RUINED_ENCHTS_TAG);
        if (!encodedEnch.isEmpty()) tag.remove(RUINED_ENCHTS_TAG);
        Map<Enchantment, Integer> enchantMap = RuinedEquipmentUtils.processEncodedEnchantments(encodedEnch);
        if (enchantMap != null) {
            for (Map.Entry<Enchantment, Integer> enchant : enchantMap.entrySet()) {
                if (isMaxEnchant) {
                    repaired.addEnchantment(enchant.getKey(), enchant.getKey().getMaxLevel());
                } else {
                    repaired.addEnchantment(enchant.getKey(), enchant.getValue());
                }
            }
        }
        repaired.setTag(repaired.getOrCreateTag().copyFrom(tag));
        repaired.setDamage(targetDamage);
        return repaired;
    }

    public static Map<Enchantment, Integer> processEncodedEnchantments(String encodedEnchants) {
        if (encodedEnchants.isEmpty()) return null;
        Map<Enchantment, Integer> enchants = new HashMap<>();
        for (String encodedEnchant : encodedEnchants.split(",")) {
            String[] enchantItem = encodedEnchant.split(">");
            String[] enchantKey = enchantItem[0].split(":");
            int enchantLevel = Integer.parseInt(enchantItem[1]);
            enchants.put(Registry.ENCHANTMENT.get(new Identifier(enchantKey[0], enchantKey[1])), enchantLevel);
        }
        return enchants.isEmpty() ? null : enchants;
    }

    public static void onSendEquipmentBreakStatusImpl(
            ServerPlayerEntity serverPlayer,
            ItemStack breakingStack,
            boolean forceSet) {
        for (Map.Entry<Item, Item> itemMap : RuinedEquipmentItems.getVanillaItemMap().entrySet()) {
            if (isVanillaItemStackBreaking(breakingStack, itemMap.getValue())) {
                // Directly copy over breaking Item's NBT, removing specific fields
                ItemStack ruinedStack = new ItemStack(itemMap.getKey());
                CompoundTag breakingNBT = breakingStack.getOrCreateTag();
                if (breakingNBT.contains("Damage")) breakingNBT.remove("Damage");
                if (breakingNBT.contains("RepairCost")) breakingNBT.remove("RepairCost");
                // Set enchantment NBT data
                CompoundTag enchantTag = getTagForEnchantments(breakingStack, ruinedStack);
                if (enchantTag != null) breakingNBT.copyFrom(enchantTag);
                if (breakingNBT.contains("Enchantments")) breakingNBT.remove("Enchantments");
                ruinedStack.setTag(breakingNBT);
                // Force set will place the Ruined item in hand
                if (forceSet) {
                    int idx = 0;
                    if (serverPlayer.inventory.offHand.get(0).toString().compareTo(breakingStack.toString()) != 0)
                        idx = serverPlayer.inventory.selectedSlot + 1;
                    RuinedEquipmentMod.ruinedEquipmentSetter.put(
                            serverPlayer.getName().getString(),
                            new Pair<>(idx, ruinedStack));
                    RuinedEquipmentMod.LOGGER.info("ruinedEquipmentSetter.put: " + serverPlayer.getName().getString());
                } else {
                    serverPlayer.inventory.offerOrDrop(serverPlayer.world, ruinedStack);
                }
            }
        }
    }

    public static CompoundTag getTagForEnchantments(ItemStack breakingStack, ItemStack ruinedStack) {
        Set<String> enchantmentStrings = new HashSet<>();
        for (Map.Entry<Enchantment, Integer> ench : EnchantmentHelper.get(breakingStack).entrySet()) {
            String enchantString = Registry.ENCHANTMENT.getId(ench.getKey())+">"+ench.getValue();
            enchantmentStrings.add(enchantString);
        }
        if (!enchantmentStrings.isEmpty()) {
            CompoundTag tag = ruinedStack.getTag();
            if (tag == null) tag = new CompoundTag();
            tag.putString(RUINED_ENCHTS_TAG, String.join(",", enchantmentStrings));
            return tag;
        }
        return null;
    }

    public static boolean isVanillaItemStackBreaking(ItemStack breakingStack, Item vanillaItem) {
        return breakingStack.isItemEqualIgnoreDamage(new ItemStack(vanillaItem))
            && breakingStack.getMaxDamage() - breakingStack.getDamage() <= 0;
    }
}
