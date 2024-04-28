package me.tulio.yang.leaderboard.variables;

import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import lombok.AllArgsConstructor;
import me.tulio.yang.leaderboard.Leaderboard;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TopClanPoints implements PlaceholderReplacer {

    public int pos;

    @Override
    public String update() {
        try {
            if (Leaderboard.getClanLeaderboards().values().isEmpty()) return " ";
            List<Integer> points = new ArrayList<>(Leaderboard.getClanLeaderboards().values());
            if (points.get(pos) == null) return " ";
            return String.valueOf(points.get(pos));
        } catch (IndexOutOfBoundsException e) {
            return " ";
        }
    }
}
