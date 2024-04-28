package me.tulio.yang.profile.menu;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.tulio.yang.match.mongo.MatchInfo;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@RequiredArgsConstructor
public class ViewMatchMenu extends PaginatedMenu {

    private final Profile profile;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&6&oMatches of &f" + profile.getName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        for (MatchInfo match : profile.getMatches()) {
            buttons.put(buttons.size(), new MatchButton(match));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private static class MatchButton extends Button{

        private final MatchInfo matchInfo;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                .name(matchInfo.getWinningParticipant() + " &6vs&f " + matchInfo.getLosingParticipant())
                .lore("&aDate&7:&b " + matchInfo.getDate())
                .lore("&aDuration&7:&b " + matchInfo.getDuration())
                .lore("&aKit&7:&b " + matchInfo.getKit().getName())
                .lore("&aWinner&7:&b " + matchInfo.getWinningParticipant() + "&7(&a+" + matchInfo.getNewWinnerElo() + "&7)")
                .lore("&aLoser&7:&b " + matchInfo.getLosingParticipant() + "&7(&c-" + matchInfo.getNewLoserElo() + "&7)")
                .build();
        }
    }

}