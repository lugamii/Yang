package me.tulio.yang.leaderboard.entry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tulio.yang.profile.Profile;

@Getter
@AllArgsConstructor
public class LeaderboardKitsEntry {

    private final Profile profile;
    private final int elo;

}
