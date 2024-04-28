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
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor
public class KitHitDelayMenu extends Menu {

    private final Kit kit;

    {
        setUpdateAfterClick(true);
    }

    @Override
    public String getTitle(Player player) {
        return CC.GREEN + kit.getName() + " Hit Delay";
    }

    @Override
    public int getSize() {
        return 9*3;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(9, new DecrementButton(kit, 10));
        buttons.put(10, new DecrementButton(kit, 5));
        buttons.put(11, new DecrementButton(kit, 1));

        buttons.put(13, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.GOLD_SWORD)
                        .name(CC.GREEN + "Hit Delay: " + CC.GOLD + kit.getGameRules().getHitDelay())
                        .build();
            }
        });

        buttons.put(15, new IncrementButton(kit, 1));
        buttons.put(16, new IncrementButton(kit, 5));
        buttons.put(17, new IncrementButton(kit, 10));

        buttons.put(26, new BackButton(new KitEditRulesMenu(kit)));

        return buttons;
    }

    @RequiredArgsConstructor
    private static class DecrementButton extends Button {

        private final Kit kit;
        private final int amount;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.STAINED_GLASS)
                    .durability(14)
                    .name(CC.RED + "-" + amount)
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            kit.getGameRules().setHitDelay(kit.getGameRules().getHitDelay() - amount);
        }
    }

    @RequiredArgsConstructor
    private static class IncrementButton extends Button {

        private final Kit kit;
        private final int amount;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.STAINED_GLASS)
                    .durability(5)
                    .name(CC.RED + "+" + amount)
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            kit.getGameRules().setHitDelay(kit.getGameRules().getHitDelay() + amount);
        }
    }
}
