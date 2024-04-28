package me.tulio.yang.kit.menu;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import me.tulio.yang.utilities.menu.button.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Map;

@RequiredArgsConstructor
public class KitStatusMenu extends Menu {

    private final Kit kit;

    @Override
    public String getTitle(Player player) {
        return CC.GREEN + kit.getName() + " Status";
    }

    @Override
    public int getSize() {
        return 9*3;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(13, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                StringBuilder effects = new StringBuilder();
                for (PotionEffect effect : kit.getGameRules().getEffects()) {
                    effects.append(CC.DARK_GREEN).append(effect.getType().getName()).append(CC.GRAY).append(":")
                            .append(CC.DARK_PURPLE).append(effect.getAmplifier()).append(CC.GRAY).append(":")
                            .append(CC.AQUA).append(effect.getDuration()).append(CC.WHITE).append(", ");
                }
                return new ItemBuilder(Material.EMPTY_MAP)
                        .name("&6&lStatus &7(&a" + kit.getName() + "&7)")
                        .lore(
                                "&aRanked&7: " + (kit.getGameRules().isRanked() ? "&aYes" : "&cNo"),
                                "&aBuild&7: " + (kit.getGameRules().isBuild() ? "&aYes" : "&cNo"),
                                "&aSpleef&7: " + (kit.getGameRules().isSpleef() ? "&aYes" : "&cNo"),
                                "&aSumo&7: " + (kit.getGameRules().isSumo() ? "&aYes" : "&cNo"),
                                "&aParkour&7: " + (kit.getGameRules().isParkour() ? "&aYes" : "&cNo"),
                                "&aHCF&7: " + (kit.getGameRules().isHcf() ? "&aYes" : "&cNo"),
                                "&aBridge&7: " + (kit.getGameRules().isBridge() ? "&aYes" : "&cNo"),
                                "&aBoxing&7: " + (kit.getGameRules().isBoxing() ? "&aYes" : "&cNo"),
                                "&aSkyWars&7: " + (kit.getGameRules().isSkywars() ? "&aYes" : "&cNo"),
                                "&aHCF Trap&7: " + (kit.getGameRules().isHcfTrap() ? "&aYes" : "&cNo"),
                                "&aHealth Regeneration&7: " + (kit.getGameRules().isHealthRegeneration() ? "&aYes" : "&cNo"),
                                "&aShow Health&7: " + (kit.getGameRules().isShowHealth() ? "&aYes" : "&cNo"),
                                "&aHit Delay&7: &f" + kit.getGameRules().getHitDelay(),
                                "&aKB Profile&7: &f" + kit.getGameRules().getKbProfile(),
                                "&aEffects &7(&2Effect&7:&5Amplifier&7:&dDuration&7) : "
                                        + (kit.getGameRules().getEffects().isEmpty() ? "Null" : effects.substring(0, effects.toString().length() - 2))
                        )
                        .build();
            }
        });


        buttons.put(26, new BackButton(new KitEditorMenu(kit)));
        return buttons;
    }
}
