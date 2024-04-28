package me.tulio.yang.leaderboard;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import me.tulio.yang.Yang;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.leaderboard.entry.LeaderboardKitsEntry;
import me.tulio.yang.leaderboard.variables.*;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.TaskUtil;
import me.tulio.yang.utilities.elo.EloUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Leaderboard {

    @Getter public static Map<String, List<LeaderboardKitsEntry>> kitLeaderboards = Maps.newHashMap();
    @Getter public static List<Profile> leaderboards = Lists.newArrayList();
    @Getter public static Map<String, Integer> clanLeaderboards = Maps.newHashMap();

    public static void init() {
        Leaderboard.initHologramsVariables();
        TaskUtil.runTimerAsync(() -> {

            leaderboards.clear();
            leaderboards.addAll(Profile.getProfiles().values());

            kitLeaderboards.clear();
            for (Kit kit : Kit.getKits()) {
                if (!kit.getGameRules().isRanked()) continue;
                List<LeaderboardKitsEntry> entry = Lists.newArrayList();
                for (Profile profile : Profile.getProfiles().values()) {
                    entry.add(new LeaderboardKitsEntry(profile, profile.getKitData().get(kit).getElo()));
                }
                entry.sort((o1, o2) -> Integer.compare(o2.getElo(), o1.getElo()));
                kitLeaderboards.put(kit.getName(), entry.subList(0, Math.min(entry.size(), 10)));
            }

            clanLeaderboards.clear();
            for (String s : Clan.getClans().keySet()) {
                Clan clan = Clan.getClans().get(s);
                clanLeaderboards.put(clan.getName(), clan.getPoints());
            }
            
            Comparator<Map.Entry<String, Integer>> comparator = Map.Entry.comparingByValue();

            List<Map.Entry<String, Integer>> sorted = Lists.newArrayList();
            sorted.addAll(clanLeaderboards.entrySet());
            sorted.sort(comparator.reversed());
            Map<String, Integer> sortedMap = Maps.newHashMap();
            int timesClan = 1;
            for (Map.Entry<String, Integer> stringIntegerEntry : sorted) {
                if (timesClan == 10) break;
                sortedMap.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
                timesClan++;
            }
            clanLeaderboards = sortedMap;

            List<Profile> profiles = Lists.newArrayList();
            profiles.addAll(leaderboards);
            profiles.sort(Comparator.comparingInt(EloUtil::getGlobalElo).reversed());
            List<Profile> sortedProfiles = Lists.newArrayList();
            int timesProfile = 1;
            for (Profile profile : profiles) {
                if (timesProfile == 10) break;
                sortedProfiles.add(profile);
                timesProfile++;
            }
            leaderboards = sortedProfiles;

        }, 100L, 600L);
    }

    public static void initHologramsVariables() {
        // Register Leaderboards Hologram Placeholders
        for (int i = 0; i < 10; i++) {
            HologramsAPI.registerPlaceholder(Yang.get(), "{globaltop" + i + "_elo}", 30,
                    new TopGlobalElo(i));
            HologramsAPI.registerPlaceholder(Yang.get(), "{globaltop" + i + "_name}", 30,
                    new TopGlobalName(i));
        }

        for (int i = 0; i < 5; i++) {
            HologramsAPI.registerPlaceholder(Yang.get(), "{clantop" + i + "_points}", 30,
                    new TopClanPoints(i));
            HologramsAPI.registerPlaceholder(Yang.get(), "{clantop" + i + "_name}", 30,
                    new TopClanName(i));
        }

        for (Kit kit : Kit.getKits()) {
            if (kit.getGameRules().isRanked()) {
                HologramsAPI.registerPlaceholder(Yang.get(), "{top" + kit.getName().toLowerCase() + "_elo}",
                        30, new TopKitElo(kit.getName()));
                HologramsAPI.registerPlaceholder(Yang.get(), "{top" + kit.getName().toLowerCase() + "_name}",
                        30, new TopKitName(kit.getName()));
            }
        }
    }

}
