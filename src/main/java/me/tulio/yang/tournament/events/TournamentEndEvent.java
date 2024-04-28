package me.tulio.yang.tournament.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.match.participant.MatchGamePlayer;
import me.tulio.yang.match.participant.GameParticipant;
import me.tulio.yang.utilities.event.CustomEvent;

@Getter @Setter
@AllArgsConstructor
public class TournamentEndEvent extends CustomEvent {

    private final GameParticipant<MatchGamePlayer> winner;
    private final boolean team;
    private final boolean clan;

}