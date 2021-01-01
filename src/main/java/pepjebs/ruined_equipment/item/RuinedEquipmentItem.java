package pepjebs.ruined_equipment.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pepjebs.ruined_equipment.RuinedEquipmentMod;

import java.util.List;
import java.util.Set;

public class RuinedEquipmentItem extends Item {

    public RuinedEquipmentItem(Settings settings) {
        super(settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if (stack.getTag() == null) return;
        String tagString = stack.getTag().getString("enchantments");
        String[] tagStrings = tagString.split(",");
        if (tagStrings.length == 0) return;

        MutableText newT0 = tooltip.get(0).shallowCopy();
        if (stack.hasGlint()) {
            newT0 = new LiteralText(newT0.getString()).formatted(Formatting.AQUA);
            if (stack.hasCustomName()) {
                newT0 = newT0.formatted(Formatting.ITALIC);
            }
            tooltip.set(0, newT0);
        }

        for (String str : tagStrings) {
            tooltip.add(new LiteralText(str).formatted(Formatting.GRAY));
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack != null && stack.getTag() != null && stack.getTag().getString("enchantments") != null
                && !stack.getTag().getString("enchantments").isEmpty();
    }
}
