package me.tulio.yang.leaderboard.variables;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import lombok.AllArgsConstructor;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.leaderboard.Leaderboard;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TopClanName implements PlaceholderReplacer {

    public int pos;

    @Override
    public String update() {
        try {
            if (Leaderboard.getClanLeaderboards().keySet().isEmpty()) return " ";
            List<String> names = new ArrayList<>(Leaderboard.getClanLeaderboards().keySet());
            if (names.get(pos) == null) return " ";
            if (Clan.getByName(names.get(pos)) == null) return " ";
            return Clan.getByName(names.get(pos)).getColoredName();
        } catch (IndexOutOfBoundsException e) {
            return " ";
        }
    }
}
