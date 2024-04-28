package me.tulio.yang.party.classes.bard;

import lombok.Getter;
import me.tulio.yang.Yang;
import me.tulio.yang.party.classes.HCFClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BardEnergyTask implements Runnable {

    @Getter private static final Map<UUID, Float> energy = new ConcurrentHashMap<>();
    public static final float MAX_ENERGY = 100;
    public static final float ENERGY_REGEN_PER_SECOND = 1;

    @Override
    public void run() {
        for (Player player : Yang.get().getServer().getOnlinePlayers()) {
            if (!HCFClass.BARD.isApply(player)) continue;

            if (energy.containsKey(player.getUniqueId())) {
                if (energy.get(player.getUniqueId()) == MAX_ENERGY) continue;

                energy.put(player.getUniqueId(), Math.min(MAX_ENERGY, energy.get(player.getUniqueId()) + ENERGY_REGEN_PER_SECOND));
            } else
                energy.put(player.getUniqueId(), 0F);

            int manaInt = energy.get(player.getUniqueId()).intValue();

            if (manaInt % 10 == 0)
                player.sendMessage(ChatColor.AQUA + "Bard Energy: " + ChatColor.GREEN + manaInt);
        }
    }
}
