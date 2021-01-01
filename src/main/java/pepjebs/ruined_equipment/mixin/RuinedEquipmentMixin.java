package pepjebs.ruined_equipment.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.ruined_equipment.RuinedEquipmentMod;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(LivingEntity.class)
public class RuinedEquipmentMixin {

    private LivingEntity livingEntity = ((LivingEntity) (Object) this);

    @Inject(method = "sendEquipmentBreakStatus", at = @At("RETURN"))
    private void onSendEquipmentBreakStatus(EquipmentSlot slot, CallbackInfo ci) {
        if (livingEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) livingEntity;
            switch(slot) {
                case MAINHAND:
                    ItemStack breakingToolStack = serverPlayer.getMainHandStack();
                    for (Map.Entry<Item, Item> itemMap : RuinedEquipmentMod.VANILLA_ITEM_MAP.entrySet()) {
                        if (breakingToolStack.isItemEqualIgnoreDamage(new ItemStack(itemMap.getValue()))) {
                            if (breakingToolStack.getMaxDamage() - breakingToolStack.getDamage() <= 0) {
                                ItemStack ruinedPick = new ItemStack(itemMap.getKey());
                                Set<String> enchantmentStrings = new HashSet<>();
                                for (Map.Entry<Enchantment, Integer> ench : EnchantmentHelper.get(breakingToolStack).entrySet()) {
                                    String enchantString = ench.getKey().getName(ench.getValue()).getString();
                                    enchantmentStrings.add(enchantString);
                                    RuinedEquipmentMod.LOGGER.info("sendEquipmentBreakStatus: Adding enchantment: \""
                                            + enchantString + "\"");
                                }
                                if (!enchantmentStrings.isEmpty()) {
                                    CompoundTag tag = ruinedPick.getTag();
                                    if (tag == null) tag = new CompoundTag();
                                    tag.putString("enchantments", String.join(",", enchantmentStrings));
                                    ruinedPick.setTag(tag);
                                }
                                MutableText breakingToolName = new LiteralText(breakingToolStack.getName().getString());
                                if (breakingToolStack.hasCustomName()) {
                                    if (ruinedPick.hasGlint()) {
                                        breakingToolName = breakingToolName.formatted(Formatting.AQUA);
                                    }
                                    ruinedPick.setCustomName(breakingToolName);
                                }
                                // Set the item in the correct index
                                serverPlayer.inventory.main.set(serverPlayer.inventory.getSwappableHotbarSlot(),
                                        ruinedPick);
                            }
                        }
                    }
                    break;
                default:
                    RuinedEquipmentMod.LOGGER.warn("No valid slot found in 'onSendEquipmentBreakStatus'.");
            }
        }
    }
}
