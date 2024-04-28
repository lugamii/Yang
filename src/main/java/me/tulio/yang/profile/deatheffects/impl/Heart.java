package me.tulio.yang.profile.deatheffects.impl;

import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.deatheffects.Data;
import me.tulio.yang.profile.deatheffects.util.ParticleEffect;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.TaskUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Heart extends Data {

    private final ParticleEffect effect = ParticleEffect.HEART;

    public Heart(Profile profile) {
        super(profile);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemBuilder(Material.APPLE).build();
    }

    @Override
    public void apply() {
        Player player = getProfile().getPlayer();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = player.getLocation();
                loc.add(0, 1.4, 0);
                loc.add(loc.getDirection().multiply(0.5));
                effect.display(0, 0, 0, 1, 5, loc, 20);
            }
        };
        TaskUtil.runTimerAsync(runnable, 0L, 4L);
        setTaskId(runnable.getTaskId());
    }
}
