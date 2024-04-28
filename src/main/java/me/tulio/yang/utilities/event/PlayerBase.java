package me.tulio.yang.utilities.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public class PlayerBase extends CustomEvent {

    private Player player;
}
