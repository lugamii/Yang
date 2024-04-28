package me.tulio.yang.event.impl.tnttag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tulio.yang.Locale;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.match.participant.GameParticipant;
import me.tulio.yang.match.participant.GamePlayer;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

@AllArgsConstructor
public class MatchTask extends BukkitRunnable {

    public final TNTTagGameLogic tntTagGameLogic;
    @Getter public int seconds;

    @Override
    public void run() {
        if (Arrays.asList(30, 15, 10, 5, 4, 3, 2, 1).contains(seconds)) {
            for (GameParticipant<GamePlayer> participant : tntTagGameLogic.getParticipants()) {
                new MessageFormat(Locale.EVENT_MATCH_REMAINING.format(Profile.get(participant.getLeader().getUuid()).getLocale()))
                        .add("{seconds}", String.valueOf(seconds))
                        .add("{context}", seconds == 1 ? "" : "s")
                        .send(participant.getLeader().getPlayer());
            }
        }
        if (seconds < 1) {
            tntTagGameLogic.endRound();
            cancel();
        }

        seconds--;
    }
}
