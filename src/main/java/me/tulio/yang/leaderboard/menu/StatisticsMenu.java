package me.tulio.yang.leaderboard.menu;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.tulio.yang.Yang;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.meta.ProfileKitData;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
This Proyect has been created
by TulioTriviño#6969
*/
@RequiredArgsConstructor
public class StatisticsMenu extends Menu {

    private final Profile target;

    @Override
    public String getTitle(Player player) {
        return Yang.get().getMainConfig().getString("STATS_MENU.TITLE")
            .replace("{player}", this.target.getName());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        int pos = 0;
        for (Kit kit : Kit.getKits()) {
            if (kit.getGameRules().isRanked()) buttons.put(pos++, new KitsItems(kit));
        }
        return buttons;
    }

    @AllArgsConstructor
    private class KitsItems extends Button {

        Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            ProfileKitData kitData = target.getKitData().get(kit);
            List<String> lore = new ArrayList<>();
            for (String s : Yang.get().getMainConfig().getStringList("STATS_MENU.DESCRIPTION")) {
                lore.add(s.replace("{bars}", CC.MENU_BAR)
                        .replace("{elo}", String.valueOf(kitData.getElo()))
                        .replace("{wins}", String.valueOf(kitData.getWon()))
                        .replace("{losses}", String.valueOf(kitData.getLost())));
            }
            return new ItemBuilder(kit.getDisplayIcon())
                    .name(Yang.get().getMainConfig().getString("STATS_MENU.ITEM_NAME").replace("{kit}", kit.getName()))
                    .lore(lore)
                    .build();
        }
    }
}
