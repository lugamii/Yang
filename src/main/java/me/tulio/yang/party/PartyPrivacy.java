package me.tulio.yang.party;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PartyPrivacy {

	OPEN("Open"),
	CLOSED("Closed");

	private final String readable;

}
