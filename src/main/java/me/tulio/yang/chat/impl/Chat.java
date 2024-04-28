package me.tulio.yang.chat.impl;

import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Yang;
import me.tulio.yang.chat.YangChatFormat;
import me.tulio.yang.chat.impl.filter.ChatFilter;
import me.tulio.yang.chat.impl.format.DefaultChatFormat;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.Cooldown;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Chat {

	private final Yang plugin = Yang.get();

	@Getter @Setter private static int delayTime = 3;
	@Getter private static boolean publicChatMuted = false;
	@Getter private static boolean publicChatDelayed = false;
	@Getter private static final List<ChatFilter> filters = new ArrayList<>();
	@Getter private static final List<String> filteredPhrases = new ArrayList<>();
	@Getter private static final List<String> linkWhitelist = new ArrayList<>();
	@Getter @Setter public static ChatFormat chatFormat = new DefaultChatFormat();

	public static void togglePublicChatMute() {
		publicChatMuted = !publicChatMuted;
	}

	public static void togglePublicChatDelay() {
		publicChatDelayed = !publicChatDelayed;
	}

	public static void init() {
		if (Yang.get().getMainConfig().getBoolean("CHAT.ACTIVE")) {
			Yang.get().getServer().getPluginManager().registerEvents(new ChatListener(), Yang.get());
			setChatFormat(new YangChatFormat());
			System.out.println("[Yang] Chat is active.");
		}
	}

	public static ChatAttempt attemptChatMessage(Player player, String message) {
		Profile profile = Profile.get(player.getUniqueId());

		if (publicChatMuted && !player.hasPermission("yang.staff")) {
			return new ChatAttempt(ChatAttempt.Response.CHAT_MUTED);
		}

		if (publicChatDelayed && !profile.getChatCooldown().hasExpired() && !player.hasPermission("yang.staff")) {
			ChatAttempt attempt = new ChatAttempt(ChatAttempt.Response.CHAT_DELAYED);
			attempt.setValue(profile.getChatCooldown().getRemaining());
			return attempt;
		}

		String msg = message.toLowerCase()
		                    .replace("3", "e")
		                    .replace("1", "i")
		                    .replace("!", "i")
		                    .replace("@", "a")
		                    .replace("7", "t")
		                    .replace("0", "o")
		                    .replace("5", "s")
		                    .replace("8", "b")
		                    .replaceAll("\\p{Punct}|\\d", "").trim();

		String[] words = msg.trim().split(" ");

		for (ChatFilter chatFilter : filters) {
			if (chatFilter.isFiltered(msg, words)) {
				return new ChatAttempt(ChatAttempt.Response.MESSAGE_FILTERED);
			}
		}

		if (publicChatDelayed) {
			profile.setChatCooldown(new Cooldown(delayTime * 1000L));
		}

		return new ChatAttempt(ChatAttempt.Response.ALLOWED);
	}

}
