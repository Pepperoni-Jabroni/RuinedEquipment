package pepjebs.ruined_equipment.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.ruined_equipment.RuinedEquipmentMod;
import pepjebs.ruined_equipment.utils.RuinedEquipmentUtils;

import java.util.ArrayList;
import java.util.List;

@Mixin(AnvilScreenHandler.class)
public abstract class RuinedAnvilScreenHandlerMixin extends ForgingScreenHandler {

    private static final double REPAIR_MODIFIER = 0.25;

    @Shadow
    @Final
    private Property levelCost;

    @Shadow
    private int repairItemUsage;

    @Shadow
    private String newItemName;


    public RuinedAnvilScreenHandlerMixin(
            @Nullable ScreenHandlerType<?> type,
            int syncId,
            PlayerInventory playerInventory,
            ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "updateResult", at = @At(value = "RETURN"))
    private void updateRuinedRepair(CallbackInfo ci) {
        ItemStack leftStack = this.input.getStack(0).copy();
        ItemStack rightStack = this.input.getStack(1).copy();
        if (RuinedEquipmentUtils.isRuinedItem(leftStack.getItem())) {
            Item vanillaItem = RuinedEquipmentUtils.getRepairItemForItemStack(leftStack);
            Item empowermentItem = RuinedEquipmentUtils.getEmpowermentApplicationItem();
            Identifier vanillaItemId = Registries.ITEM.getId(vanillaItem);
            int vanillaMaxDamage = vanillaItem.getMaxDamage() - 1;
            // Check right stack for matching repair item
            Ingredient repairIngredient = null;
            var ashesRepairItems = RuinedEquipmentUtils.getParsedRuinedItemsAshesRepairItems();
            if (vanillaItemId.getNamespace().compareTo("minecraft") != 0 && ashesRepairItems != null) {
                Identifier repairingItemId = ashesRepairItems.get(vanillaItemId);
                if (repairingItemId == null) return;
                repairIngredient = Ingredient.ofItems(Registries.ITEM.get(repairingItemId));
            } else if (vanillaItem instanceof ArmorItem) {
                repairIngredient = ((ArmorItem) vanillaItem).getMaterial().getRepairIngredient();
            } else if (vanillaItem instanceof ToolItem) {
                repairIngredient = ((ToolItem) vanillaItem).getMaterial().getRepairIngredient();
            } else if (vanillaItem == Items.SHIELD) {
                repairIngredient = Ingredient.fromTag(ItemTags.PLANKS);
            }

            ItemStack output = ItemStack.EMPTY;
            int maxLevel = 4;
            if (repairIngredient != null && repairIngredient.test(rightStack)) {
                if (RuinedEquipmentMod.CONFIG != null &&
                        !RuinedEquipmentMod.CONFIG.enableAnvilRuinedRepair) return;
                double targetFraction = 1.0 - (rightStack.getCount() * REPAIR_MODIFIER);
                output = RuinedEquipmentUtils.generateRepairedItemForAnvilByFraction(
                        leftStack,
                        Math.min(targetFraction, 1.0));
                this.repairItemUsage = 4;
            } else if (rightStack.getItem() == vanillaItem) {
                if (RuinedEquipmentMod.CONFIG != null &&
                        !RuinedEquipmentMod.CONFIG.enableAnvilRuinedRepair) return;
                // Check right stack for corresponding vanilla item
                int targetDamage = rightStack.getDamage() - (int) (REPAIR_MODIFIER * rightStack.getMaxDamage());
                output = RuinedEquipmentUtils.generateRepairedItemForAnvilByDamage(
                        leftStack,
                        Math.min(targetDamage, vanillaMaxDamage));
                maxLevel = 2;
                this.repairItemUsage = 0;
            } else if (rightStack.getItem() == empowermentItem &&
                    (RuinedEquipmentMod.CONFIG == null || RuinedEquipmentMod.CONFIG.enableSmithingRuinedEmpowered)) {
                var ruinedItem = leftStack.copy();
                NbtCompound tag = ruinedItem.getNbt();
                if (tag == null) tag = new NbtCompound();
                if (tag.contains(RuinedEquipmentMod.RUINED_MAX_ENCHT_TAG))
                    tag.remove(RuinedEquipmentMod.RUINED_MAX_ENCHT_TAG);
                tag.putBoolean(RuinedEquipmentMod.RUINED_MAX_ENCHT_TAG, true);
                ruinedItem.setNbt(tag);
                this.repairItemUsage = 1;
                output = ruinedItem;
            } else if (rightStack.getItem() == Items.NAME_TAG) {
                if (RuinedEquipmentMod.CONFIG != null && !RuinedEquipmentMod.CONFIG.enableLoreSetWithNameTag) {
                    return;
                }
                var lores = new NbtList();
                List<String> nameTagLoreLines = new ArrayList<>();
                try {
                    var nameTagNbt = rightStack.getNbt();
                    var nameTagTextNbt = nameTagNbt.getCompound("display").getString("Name");
                    var nameTagText = NbtHelper.fromNbtProviderString(nameTagTextNbt).getString("text");
                    var splitNameTagText = nameTagText.split(" ");
                    nameTagLoreLines = chunkStringArray(splitNameTagText, 25);
                    lores.addAll(
                            nameTagLoreLines.stream()
                                    .map(s -> {
                                        NbtCompound c = new NbtCompound();
                                        c.putString("text", s);
                                        return NbtString.of(NbtHelper.toFormattedString(c));
                                    })
                                    .toList());
                } catch (Exception _e) {
                    // Doesnt work :)
                }
                if (!lores.isEmpty()) {
                    var existingDisplay =
                            leftStack.getNbt() != null && leftStack.getSubNbt("display") != null
                                    ? leftStack.getSubNbt("display") : null;
                    if (existingDisplay == null) {
                        existingDisplay = new NbtCompound();
                    } else {
                        var existingLores = existingDisplay.contains("Lore") ?
                                existingDisplay.getList("Lore", NbtElement.STRING_TYPE)
                                : null;
                        if (existingLores != null) {
                            existingLores.addAll(lores);
                            lores = existingLores;
                        }
                    }
                    if (nameTagLoreLines.stream().anyMatch(t -> t.compareTo("clear") == 0)) {
                        lores = new NbtList();
                    }
                    existingDisplay.put("Lore", lores);
                    leftStack.setSubNbt("display", existingDisplay);
                }
                output = leftStack;
                this.repairItemUsage = 1;
            }
            var setName =  (newItemName != null && newItemName.compareTo("") != 0);
            if (setName) {
                output = (output == ItemStack.EMPTY) ? leftStack.copy() : output;
                output.setCustomName(Text.of(newItemName));
            }
            // Set the output
            if (!output.isEmpty()) {
                int levelCost = RuinedEquipmentUtils.generateAnvilLevelCost(output, maxLevel);
                this.levelCost.set(setName ? levelCost + 1 : levelCost);
            }
            this.output.setStack(0, output);
            this.sendContentUpdates();
        }
    }

    // Thanks ChatGPT I'm so lazy
    private static List<String> chunkStringArray(String[] inputArray, int maxLength) {
        List<String> chunkedList = new ArrayList<>();
        StringBuilder currentChunk = new StringBuilder();

        for (String word : inputArray) {
            if (currentChunk.length() + word.length() <= maxLength) {
                // Append word and a space to the current chunk
                currentChunk.append(word).append(" ");
            } else {
                // Add the current chunk (excluding the last space) to the chunked list
                chunkedList.add(currentChunk.toString().trim());
                currentChunk = new StringBuilder(word).append(" ");
            }
        }

        // Add the remaining chunk to the chunked list
        chunkedList.add(currentChunk.toString().trim());

        return chunkedList;
    }
}
