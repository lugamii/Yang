package me.tulio.yang.leaderboard.menu;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import me.tulio.yang.Yang;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.leaderboard.menu.button.GlobalStatsButton;
import me.tulio.yang.leaderboard.menu.button.KitButton;
import me.tulio.yang.leaderboard.menu.button.StatsButton;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@AllArgsConstructor
public class LeaderBoardMenu extends Menu {

    private final Player target;

    @Override
    public String getTitle(Player player) {
        return "&2&lStatistics";
    }

    @Override
    public int getSize() {
        if (Yang.get().getLeaderboardConfig().getBoolean("DEFAULT_MODE")) {
            return super.getSize();
        } else {
            return Yang.get().getLeaderboardConfig().getInteger("CUSTOM_SECTION.INVENTORY.ROWS")*9;
        }
    }

    @Override
    public Map<Integer, Button> getButtons(Player player){
        Map<Integer, Button> buttons = Maps.newHashMap();
        Button empty = new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.STAINED_GLASS_PANE)
                        .durability(15)
                        .name("")
                        .build();
            }
        };

        if (Yang.get().getLeaderboardConfig().getBoolean("DEFAULT_MODE")) {
            buttons.put(0, new StatsButton(target));
            buttons.put(9, new GlobalStatsButton());

            buttons.put(1, empty);
            buttons.put(10, empty);

            int pos = 0;
            for (Kit kit : Kit.getKits()) {
                if (kit.isEnabled() && kit.getGameRules().isRanked() && kit.getDisplayIcon() != null) {
                    pos++;
                     if (1 + pos == 9) pos++;
                     if (1 + pos == 10) pos++;
                    buttons.put(1 + pos, new KitButton(kit));
                }
            }
        } else {
            for (String key : Yang.get().getLeaderboardConfig().getConfiguration().getConfigurationSection("CUSTOM_SECTION.INVENTORY.ITEMS")
                    .getKeys(false)) {
                Button button = null;
                String string = Yang.get().getLeaderboardConfig().getString("CUSTOM_SECTION.INVENTORY.ITEMS." + key);
                if (string.equals("{global-stats}")) {
                    button = new GlobalStatsButton();
                } else if (string.equals("{personal-stats}")) {
                    button = new StatsButton(target);
                } else if (string.startsWith("{kit-")) {
                    String kitName = string.replace("{kit-", "").replace("}", "");
                    Kit kit = Kit.getByName(kitName);
                    if (kit != null) {
                        button = new KitButton(kit);
                    }
                }
                buttons.put(Integer.parseInt(key)-1, button);
            }

            if (Yang.get().getLeaderboardConfig().getBoolean("CUSTOM_SECTION.INVENTORY.FILL_EMPTY_SLOTS")) {
                for (int i = 0; i < getSize(); i++) {
                    if (!buttons.containsKey(i)) {
                        buttons.put(i, empty);
                    }
                }
            }
        }

        return buttons;
    }
}
