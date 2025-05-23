package me.tulio.yang.event.game;

import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Locale;
import me.tulio.yang.event.impl.tnttag.TNTTagGameLogic;
import me.tulio.yang.utilities.string.MessageFormat;
import me.tulio.yang.utilities.string.TimeUtil;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class EventGameLogicTask extends BukkitRunnable {

	private final EventGame game;
	private int totalTicked;
	@Getter @Setter private int nextAction = 30;
	@Getter private long nextActionMillis;

	public EventGameLogicTask(EventGame game) {
		this.game = game;
	}

	public void run() {
		if (!game.equals(EventGame.getActiveGame())) {
			this.cancel();
			return;
		}

		totalTicked++;
		nextAction--;

		if (game.getGameState() == EventGameState.WAITING_FOR_PLAYERS) {
			// TODO: replace default 2 with getPlayersToStart method
			if (game.getGameLogic().canStartEvent()) {
				nextAction = 31;
				nextActionMillis = System.currentTimeMillis() + 31_000L;
				game.setGameState(EventGameState.STARTING_EVENT);
			} else {
				if (totalTicked >= 300) {
					game.getGameLogic().cancelEvent();
				}

				if (nextAction <= 0) {
					game.broadcastJoinMessage();
					nextAction = 30;
					nextActionMillis = System.currentTimeMillis() + 30_000L;
				}
			}
		} else if (game.getGameState() == EventGameState.STARTING_EVENT) {
			// TODO: replace default 2 with getPlayersToStart method
			if (game.getParticipants().size() < 2) {
				game.setGameState(EventGameState.WAITING_FOR_PLAYERS);
				return;
			}

			if (nextAction == 0) {
				game.getGameLogic().startEvent();
				game.getGameLogic().preStartRound();
				game.setGameState(EventGameState.STARTING_ROUND);
				nextAction = 4;
				nextActionMillis = System.currentTimeMillis() + 4_000L;
			}
		} else if (game.getGameState() == EventGameState.STARTING_ROUND) {
			if (nextAction == 0) {
				game.getGameLogic().startRound();
				game.setGameState(EventGameState.PLAYING_ROUND);
			} else {
				game.sendMessage(Locale.EVENT_ROUND_START_TIMER, new MessageFormat()
					.add("{round}", String.valueOf(game.getGameLogic().getRoundNumber()))
					.add("{time}", String.valueOf(nextAction))
					.add("{context}", nextAction == 1 ? "second" : "seconds")
				);

				game.sendSound(Sound.ORB_PICKUP, 1.0F, 1.0F);
			}
		} else if (game.getGameState() == EventGameState.ENDING_ROUND) {
			if (nextAction == 0) {
				if (!(game.getGameLogic() instanceof TNTTagGameLogic)) {
					game.getGameLogic().endRound();
				}

				if (game.getGameLogic().canStartRound()) {
					game.getGameLogic().preStartRound();
					game.setGameState(EventGameState.STARTING_ROUND);
					nextAction = 4;
					nextActionMillis = System.currentTimeMillis() + 4_000L;
				} else if (game.getGameLogic().canEndEvent()) {
					game.getGameLogic().preEndEvent();
					game.setGameState(EventGameState.ENDING_EVENT);
					nextAction = 3;
					nextActionMillis = System.currentTimeMillis() + 3_000L;
				}
			}
		} else if (game.getGameState() == EventGameState.ENDING_EVENT) {
			if (nextAction == 0) {
				if (game.getGameLogic().canEndEvent()) {
					this.cancel();
					game.getGameLogic().endEvent();
				}
			}
		}
	}

	public String getNextActionTime() {
		return TimeUtil.millisToSeconds(nextActionMillis - System.currentTimeMillis()) + "s";
	}

}
