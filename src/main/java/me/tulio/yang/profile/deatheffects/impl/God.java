package me.tulio.yang.profile.deatheffects.impl;

import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.deatheffects.Data;
import me.tulio.yang.profile.deatheffects.util.ParticleEffect;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.MathsUtility;
import me.tulio.yang.utilities.TaskUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class God extends Data {

    private final ParticleEffect effect = ParticleEffect.REDSTONE;

    public God(Profile profile) {
        super(profile);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemBuilder(Material.DOUBLE_PLANT).build();
    }

    @Override
    public void apply() {
        Player player = getProfile().getPlayer();
        AtomicInteger stepX = new AtomicInteger();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = player.getLocation().add(0, 0.5, 0);
                for (double stepY = -60; stepY < 60; stepY += 120D / 6) {
                    double dx = -(Math.cos(((stepX.get() + stepY) / 50.0) * Math.PI * 2)) * 0.8;
                    double dy = stepY / 50.0 / 2D;
                    double dz = -(Math.sin(((stepX.get() + stepY) / 50.0) * Math.PI * 2)) * 0.8;
                    effect.display(0, 0, 0, 1, 4, loc.clone().add(dx, dy, dz), 20);
                }
                stepX.incrementAndGet();
            }
        };
        TaskUtil.runTimerAsync(runnable, 0L, TimeUnit.MILLISECONDS.toMillis(1L));
        setTaskId(runnable.getTaskId());
    }
}
