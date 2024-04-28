package me.tulio.yang.leaderboard.menu.button;

import com.google.common.collect.Lists;
import me.tulio.yang.Yang;
import me.tulio.yang.leaderboard.Leaderboard;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.elo.EloUtil;
import me.tulio.yang.utilities.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalStatsButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player){
        List<String> lore = Lists.newArrayList();
        List<Profile> profiles = new ArrayList<>();
        long limit = 10;
        for (Profile profile : Leaderboard.getLeaderboards()) {
            if (limit-- == 0) break;
            profiles.add(profile);
        }

        AtomicInteger pos = new AtomicInteger(0);

        lore.add(CC.MENU_BAR);
        for (Profile leaderboard : profiles) {
            pos.incrementAndGet();
            if (pos.get() == 1) {
                List<String> first = Yang.get().getLeaderboardConfig().getStringList("CUSTOM_ITEMS.GLOBAL_STATS.POSITIONS.1");
                for (String s : first) {
                    lore.add(s
                            .replace("{pos}", String.valueOf(pos.get()))
                            .replace("{name}", leaderboard.getName())
                            .replace("{color}", leaderboard.getColor())
                            .replace("{data}", String.valueOf(EloUtil.getGlobalElo(leaderboard)))
                            .replace("{bars}", CC.MENU_BAR));
                }
            }
            else if (pos.get() == 2) {
                List<String> second = Yang.get().getLeaderboardConfig().getStringList("CUSTOM_ITEMS.GLOBAL_STATS.POSITIONS.2");
                for (String s : second) {
                    lore.add(s
                            .replace("{pos}", String.valueOf(pos.get()))
                            .replace("{name}", leaderboard.getName())
                            .replace("{color}", leaderboard.getColor())
                            .replace("{data}", String.valueOf(EloUtil.getGlobalElo(leaderboard)))
                            .replace("{bars}", CC.MENU_BAR));
                }
            }
            else if (pos.get() == 3) {
                List<String> third = Yang.get().getLeaderboardConfig().getStringList("CUSTOM_ITEMS.GLOBAL_STATS.POSITIONS.3");
                for (String s : third) {
                    lore.add(s
                            .replace("{pos}", String.valueOf(pos.get()))
                            .replace("{name}", leaderboard.getName())
                            .replace("{color}", leaderboard.getColor())
                            .replace("{data}", String.valueOf(EloUtil.getGlobalElo(leaderboard)))
                            .replace("{bars}", CC.MENU_BAR));
                }
            }
            else {
                List<String> another = Yang.get().getLeaderboardConfig().getStringList("CUSTOM_ITEMS.GLOBAL_STATS.POSITIONS.ANOTHER");
                for (String s : another) {
                    lore.add(s
                            .replace("{pos}", String.valueOf(pos.get()))
                            .replace("{name}", leaderboard.getName())
                            .replace("{color}", leaderboard.getColor())
                            .replace("{data}", String.valueOf(EloUtil.getGlobalElo(leaderboard)))
                            .replace("{bars}", CC.MENU_BAR));
                }
            }
        }
        lore.add(CC.MENU_BAR);

        return new ItemBuilder(Material.NETHER_STAR)
                .name(Yang.get().getLeaderboardConfig().getString("CUSTOM_ITEMS.GLOBAL_STATS.TITLE"))
                .lore(lore)
                .build();
    }
}
