package me.tulio.yang.match.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.tulio.yang.match.Match;
import me.tulio.yang.utilities.event.CustomEvent;

@AllArgsConstructor
@Getter
public class MatchEndEvent extends CustomEvent {
    private final Match match;
}