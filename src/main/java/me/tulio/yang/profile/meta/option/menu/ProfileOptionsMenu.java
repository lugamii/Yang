package me.tulio.yang.profile.meta.option.menu;

import me.tulio.yang.Yang;
import me.tulio.yang.profile.meta.option.button.*;
import me.tulio.yang.utilities.file.type.BasicConfigurationFile;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ProfileOptionsMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&6&lOptions";
	}

	{
		setPlaceholder(Yang.get().getOptionsConfig().getBoolean("FILL_EMPTY_SLOTS"));
	}

	@Override
	public int getSize() {
		return Yang.get().getOptionsConfig().getInteger("ROWS")*9;
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		BasicConfigurationFile config = Yang.get().getOptionsConfig();
		for (String item : config.getConfiguration().getConfigurationSection("SLOTS").getKeys(false)) {
			Button button = null;
			switch (item) {
				case "PUBLIC_CHAT_OPTION": {
					button = new PublicChatOptionButton();
					break;
				}
				case "PRIVATE_CHAT_OPTION": {
					button = new PrivateChatOptionButton();
					break;
				}
				case "CHANGE_TAB_TYPE_OPTION": {
					button = new ChangeTabTypeOptionButton();
					break;
				}
				case "PRIVATE_CHAT_SOUND_OPTION": {
					button = new PrivateChatSoundsOptionButton();
					break;
				}
				case "SHOW_SCOREBOARD_OPTION": {
					button = new ShowScoreboardOptionButton();
					break;
				}
				case "ALLOW_SPECTATORS_OPTION": {
					button = new AllowSpectatorsOptionButton();
					break;
				}
				case "DUEL_REQUESTS_OPTION": {
					button = new DuelRequestsOptionButton();
					break;
				}
			}
			buttons.put(config.getInteger("SLOTS." + item)-1, button);
		}
		return buttons;
	}

}
