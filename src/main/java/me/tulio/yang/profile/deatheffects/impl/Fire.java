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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Fire extends Data {

    private final ParticleEffect effect = ParticleEffect.LAVA;

    public Fire(Profile profile) {
        super(profile);
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemBuilder(Material.FIREBALL).build();
    }

    @Override
    public void apply() {
        Player player = getProfile().getPlayer();
        AtomicInteger integer = new AtomicInteger();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                List<Location> loc = MathsUtility.getCircle(player.getLocation().add(0, 1.8, 0), 1.0F, 8);
                if (integer.get() == loc.size()) integer.set(0);
                effect.display(0, 0, 0, 1, 5, loc.get(integer.getAndIncrement()), 20);
            }
        };
        TaskUtil.runTimerAsync(runnable, 0L, 2L);
        setTaskId(runnable.getTaskId());
    }
}
