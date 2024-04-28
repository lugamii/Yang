package me.tulio.yang.tournament;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TournamentState {

    STARTING("Starting"),
    IN_FIGHT("Fighting"),
    SELECTING_DUELS("Selecting Duels"),
    ENDED("Ended");

    private final String name;

}