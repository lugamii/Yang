package me.tulio.yang.kit.command;

import me.tulio.yang.kit.Kit;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class KitStatusCommand extends BaseCommand {

	@Command(name = "kit.status", permission = "yang.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit status (kit)");
			return;
		}

		Kit kit = Kit.getByName(args[0]);
		if (kit == null) {
			player.sendMessage(CC.RED + "A kit with that name does not exist.");
			return;
		}
		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate("&6&lKits Status &7(" + (kit.isEnabled() ? "&a" : "&c") + kit.getName() + "&7)"));
		player.sendMessage("");
		player.sendMessage(CC.translate("&aRanked&7: " + (kit.getGameRules().isRanked() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aBuild&7: " + (kit.getGameRules().isBuild() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aSpleef&7: " + (kit.getGameRules().isSpleef() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aSumo&7: " + (kit.getGameRules().isSumo() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aParkour&7: " + (kit.getGameRules().isParkour() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aHCF&7: " + (kit.getGameRules().isHcf() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aBridge&7: " + (kit.getGameRules().isBridge() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aBoxing&7: " + (kit.getGameRules().isBoxing() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aSkyWars&7: " + (kit.getGameRules().isSkywars() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aHCFTrap&7: " + (kit.getGameRules().isHcfTrap() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aNo Fall Damage&7: " + (kit.getGameRules().isNoFallDamage() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aNo Food&7: " + (kit.getGameRules().isNoFood() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aSoup&7: " + (kit.getGameRules().isSoup() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aHealth Regeneration&7: " + (kit.getGameRules().isHealthRegeneration() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aShow Health&7: " + (kit.getGameRules().isShowHealth() ? "&a\u2713" : "&c\u2717")));
		player.sendMessage(CC.translate("&aHit Delay&7: " + kit.getGameRules().getHitDelay()));
		player.sendMessage(CC.translate("&aKb Profile&7: " + kit.getGameRules().getKbProfile()));
		player.sendMessage(CC.translate("&aEffects&7: "));
		for (PotionEffect effect : kit.getGameRules().getEffects()) {
			player.sendMessage(CC.translate(" &7- &a" + effect.getType().getName() + " &7| " + effect.getDuration() + "s"));
		}
		player.sendMessage(CC.CHAT_BAR);
	}
}
