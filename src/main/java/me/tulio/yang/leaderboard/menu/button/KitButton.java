package me.tulio.yang.leaderboard.menu.button;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import me.tulio.yang.Yang;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.leaderboard.Leaderboard;
import me.tulio.yang.leaderboard.entry.LeaderboardKitsEntry;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.meta.ProfileKitData;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class KitButton extends Button {

    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = Lists.newArrayList();
        List<LeaderboardKitsEntry> leaderboard = new ArrayList<>();
        long limit = 10;
        for (LeaderboardKitsEntry kitsEntry : Leaderboard.getKitLeaderboards().get(kit.getName())) {
            if (limit-- == 0) break;
            leaderboard.add(kitsEntry);
        }

        int pos = 0;

        lore.add(CC.MENU_BAR);
        for (LeaderboardKitsEntry leaderboardKitsEntry : leaderboard) {
            pos++;
            Profile profile = leaderboardKitsEntry.getProfile();
            if (profile != null) {
                if (pos == 1) {
                    List<String> first = Yang.get().getLeaderboardConfig().getStringList("CUSTOM_ITEMS.KIT.POSITIONS.1");
                    for (String s : first) {
                        lore.add(s
                                .replace("{pos}", String.valueOf(pos))
                                .replace("{name}", profile.getName())
                                .replace("{color}", profile.getColor())
                                .replace("{data}", String.valueOf(profile.getKitData().getOrDefault(kit, new ProfileKitData()).getElo()))
                                .replace("{bars}", CC.MENU_BAR));
                    }
                } else if (pos == 2) {
                    List<String> second = Yang.get().getLeaderboardConfig().getStringList("CUSTOM_ITEMS.KIT.POSITIONS.2");
                    for (String s : second) {
                        lore.add(s
                                .replace("{pos}", String.valueOf(pos))
                                .replace("{name}", profile.getName())
                                .replace("{color}", profile.getColor())
                                .replace("{data}", String.valueOf(profile.getKitData().getOrDefault(kit, new ProfileKitData()).getElo()))
                                .replace("{bars}", CC.MENU_BAR));
                    }
                } else if (pos == 3) {
                    List<String> third = Yang.get().getLeaderboardConfig().getStringList("CUSTOM_ITEMS.KIT.POSITIONS.3");
                    for (String s : third) {
                        lore.add(s
                                .replace("{pos}", String.valueOf(pos))
                                .replace("{name}", profile.getName())
                                .replace("{color}", profile.getColor())
                                .replace("{data}", String.valueOf(profile.getKitData().getOrDefault(kit, new ProfileKitData()).getElo()))
                                .replace("{bars}", CC.MENU_BAR));
                    }
                } else {
                    List<String> another = Yang.get().getLeaderboardConfig().getStringList("CUSTOM_ITEMS.KIT.POSITIONS.ANOTHER");
                    for (String s : another) {
                        lore.add(s
                                .replace("{pos}", String.valueOf(pos))
                                .replace("{name}", profile.getName())
                                .replace("{color}", profile.getColor())
                                .replace("{data}", String.valueOf(profile.getKitData().getOrDefault(kit, new ProfileKitData()).getElo()))
                                .replace("{bars}", CC.MENU_BAR));
                    }
                }
            }
        }
        lore.add(CC.MENU_BAR);

        return new ItemBuilder(kit.getDisplayIcon().getType())
                .name(Yang.get().getLeaderboardConfig().getString("CUSTOM_ITEMS.KIT.TITLE").replace("{kit}", kit.getName()))
                .durability(kit.getDisplayIcon().getDurability())
                .lore(lore)
                .build();
    }
}
