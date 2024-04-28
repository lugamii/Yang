package me.tulio.yang.arena.command;

import me.tulio.yang.arena.Arena;
import me.tulio.yang.arena.cuboid.Cuboid;
import me.tulio.yang.arena.impl.StandaloneArena;
import me.tulio.yang.arena.selection.Selection;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class ArenaSetSpawnCommand extends BaseCommand {

	@Command(name = "arena.setspawn", permission = "yang.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length < 2) {
			player.sendMessage(CC.translate("&cPlease usage: /arena setspawn (arena) (a/b)"));
			return;
		}
		Arena arena = Arena.getByName(args[0]);
		String pos = args[1];

		if (arena != null) {
			if (pos.equalsIgnoreCase("a")) {
				arena.setSpawnA(player.getLocation());
			} else if (pos.equalsIgnoreCase("b")) {
				arena.setSpawnB(player.getLocation());
			}else if (pos.equalsIgnoreCase("red")) {
				if(!(arena instanceof StandaloneArena)){
					player.sendMessage("Only StandAloneArena allow this");
					return;
				}
				StandaloneArena standaloneArena = (StandaloneArena) arena;
				Selection selection = Selection.createOrGetSelection(player);
				if (!selection.isFullObject()) {
					player.sendMessage(CC.RED + "Your selection is incomplete.");
					return;
				}
				standaloneArena.setSpawnRed(new Cuboid(selection.getPoint1(), selection.getPoint2()));
			}else if (pos.equalsIgnoreCase("blue")) {
				if(!(arena instanceof StandaloneArena)){
					player.sendMessage("Only StandAloneArena allow this");
					return;
				}
				StandaloneArena standaloneArena = (StandaloneArena) arena;
				Selection selection = Selection.createOrGetSelection(player);
				if (!selection.isFullObject()){
					player.sendMessage(CC.RED + "Your selection is incomplete.");
					return;
				}
				standaloneArena.setSpawnBlue(new Cuboid(selection.getPoint1(), selection.getPoint2()));
			} else {
				player.sendMessage(CC.RED + "Invalid spawn point. Try \"a\" or \"b\".");
				return;
			}

			arena.save();

			player.sendMessage(CC.GOLD + "Updated spawn point \"" + pos + "\" for arena \"" + arena.getName() + "\"");
		} else {
			player.sendMessage(CC.RED + "An arena with that name doesn't exists.");
		}
	}
}
