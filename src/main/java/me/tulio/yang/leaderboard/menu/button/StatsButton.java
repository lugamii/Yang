package me.tulio.yang.leaderboard.menu.button;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import me.tulio.yang.Yang;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.elo.EloUtil;
import me.tulio.yang.utilities.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

@AllArgsConstructor
public class StatsButton extends Button {

    public Player target;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = Lists.newArrayList();
        Profile profile = Profile.get(target.getUniqueId());

        for (String s : Yang.get().getLeaderboardConfig().getStringList("CUSTOM_ITEMS.PERSONAL_STATS.DESCRIPTION")) {
            if (s.contains("{kits}")) {
                for (Kit kit : Kit.getKits()) {
                    if (!kit.getGameRules().isRanked()) continue;
                    lore.add(Yang.get().getLeaderboardConfig().getString("CUSTOM_ITEMS.PERSONAL_STATS.KITS_FORMAT")
                            .replace("{kit}", kit.getName())
                            .replace("{color}", profile.getColor())
                            .replace("{data}", String.valueOf(profile.getKitData().get(kit).getElo())));
                }
                continue;
            }
            lore.add(s
                    .replace("{bars}", CC.MENU_BAR)
                    .replace("{elo}", String.valueOf(EloUtil.getGlobalElo(profile))));
        }

        ItemStack item = new ItemBuilder(Material.SKULL_ITEM)
                .durability(3)
                .name(Yang.get().getLeaderboardConfig().getString("CUSTOM_ITEMS.PERSONAL_STATS.TITLE")
                        .replace("{color}", profile.getColor())
                        .replace("{name}", target.getName()))
                .lore(lore)
                .build();

        SkullMeta itemMeta = (SkullMeta)item.getItemMeta();
        itemMeta.setOwner(target.getName());
        item.setItemMeta(itemMeta);
        return item;
    }
}
