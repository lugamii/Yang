package me.tulio.yang.essentials;

import com.google.common.collect.ImmutableList;
import me.tulio.yang.Yang;
import me.tulio.yang.utilities.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;

public class EssentialsListener implements Listener {

	private final Yang plugin = Yang.get();

	private final ImmutableList<String> BLOCKED_COMMANDS = ImmutableList.of(
			"//calc",
			"//eval",
			"//solve",
			"/bukkit:",
			"/me",
			"/bukkit:me",
			"/minecraft:",
			"/minecraft:me"
	);

	@EventHandler(priority = EventPriority.LOWEST)
	public final void onCommandProcess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String message = (event.getMessage().startsWith("/") ? "" : "/") + event.getMessage();

		for (String blockedCommand : BLOCKED_COMMANDS) {
			if (message.startsWith(blockedCommand)) {
				if (message.equalsIgnoreCase("/version") || message.equalsIgnoreCase("/ver")) {
					if (event.getPlayer().isOp()) {
						return;
					}
				}

				player.sendMessage(CC.RED + "You cannot perform this command.");
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public final void onPingServer(ServerListPingEvent event){
		List<String> modt = plugin.getEssentials().getMotd();
		if(modt != null && !modt.isEmpty()){
			String first = CC.translate(modt.get(0));
			if(modt.size() >= 2){
				String second = CC.translate(modt.get(1));
				event.setMotd(first + System.lineSeparator() + second);
				return;
			}
			event.setMotd(first);
		}
	}

}
