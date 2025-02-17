package me.tulio.yang.leaderboard;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.tulio.yang.Yang;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.elo.EloUtil;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public @NotNull String getIdentifier() {
        return "Yang";
    }

    @Override
    public @NotNull String getAuthor() {
        return Yang.get().getDescription().getAuthors().toString().replace("[", "").replace("]", "");
    }

    @Override
    public @NotNull String getVersion() {
        return Yang.get().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equals("yang_player_elo")) return String.valueOf(EloUtil.getGlobalElo(Profile.get(player.getUniqueId())));

        if (params.contains("globaltop_1_name")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(0) == null) return " ";
                Profile profile = test.get(0);
                return CC.translate(profile.getColor()) + profile.getName();
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_2_name")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(1) == null) return " ";
                Profile profile = test.get(1);
                return CC.translate(profile.getColor()) + profile.getName();
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_3_name")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(2) == null) return " ";
                Profile profile = test.get(2);
                return CC.translate(profile.getColor()) + profile.getName();
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_4_name")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(3) == null) return " ";
                Profile profile = test.get(3);
                return CC.translate(profile.getColor()) + profile.getName();
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_5_name")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(4) == null) return " ";
                Profile profile = test.get(4);
                return CC.translate(profile.getColor()) + profile.getName();
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_6_name")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(5) == null) return " ";
                Profile profile = test.get(5);
                return CC.translate(profile.getColor()) + profile.getName();
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_7_name")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(6) == null) return " ";
                Profile profile = test.get(6);
                return CC.translate(profile.getColor()) + profile.getName();
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_8_name")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(7) == null) return " ";
                Profile profile = test.get(7);
                return CC.translate(profile.getColor()) + profile.getName();
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_9_name")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(8) == null) return " ";
                Profile profile = test.get(8);
                return CC.translate(profile.getColor()) + profile.getName();
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_10_name")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(9) == null) return " ";
                Profile profile = test.get(9);
                return CC.translate(profile.getColor()) + profile.getName();
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_1_elo")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(0) == null) return " ";
                Profile profile = test.get(0);
                return String.valueOf(EloUtil.getGlobalElo(profile));
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_2_elo")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(1) == null) return " ";
                Profile profile = test.get(1);
                return String.valueOf(EloUtil.getGlobalElo(profile));
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_3_elo")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(2) == null) return " ";
                Profile profile = test.get(2);
                return String.valueOf(EloUtil.getGlobalElo(profile));
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_4_elo")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(3) == null) return " ";
                Profile profile = test.get(3);
                return String.valueOf(EloUtil.getGlobalElo(profile));
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_5_elo")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(4) == null) return " ";
                Profile profile = test.get(4);
                return String.valueOf(EloUtil.getGlobalElo(profile));
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_6_elo")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(5) == null) return " ";
                Profile profile = test.get(5);
                return String.valueOf(EloUtil.getGlobalElo(profile));
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_7_elo")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(6) == null) return " ";
                Profile profile = test.get(6);
                return String.valueOf(EloUtil.getGlobalElo(profile));
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_8_elo")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(7) == null) return " ";
                Profile profile = test.get(7);
                return String.valueOf(EloUtil.getGlobalElo(profile));
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_9_elo")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(8) == null) return " ";
                Profile profile = test.get(8);
                return String.valueOf(EloUtil.getGlobalElo(profile));
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        if (params.contains("globaltop_10_elo")) {
            try {
                if (Leaderboard.getLeaderboards().isEmpty()) return " ";
                List<Profile> test = Leaderboard.getLeaderboards();
                if (test.get(9) == null) return " ";
                Profile profile = test.get(9);
                return String.valueOf(EloUtil.getGlobalElo(profile));
            } catch (IndexOutOfBoundsException e) {
                return " ";
            }
        }

        return null;
    }
}
