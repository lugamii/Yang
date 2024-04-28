package me.tulio.yang.leaderboard.variables;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import lombok.AllArgsConstructor;
import me.tulio.yang.leaderboard.Leaderboard;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.elo.EloUtil;

import java.util.List;

@AllArgsConstructor
public class TopGlobalElo implements PlaceholderReplacer {

    public int pos;

    @Override
    public String update() {
        try {
            if (Leaderboard.getLeaderboards().isEmpty()) return " ";
            List<Profile> test = Leaderboard.getLeaderboards();
            if (test.get(pos) == null) return " ";
            Profile profile = test.get(pos);
            return String.valueOf(EloUtil.getGlobalElo(profile));
        } catch (IndexOutOfBoundsException e) {
            return " ";
        }
    }
}
