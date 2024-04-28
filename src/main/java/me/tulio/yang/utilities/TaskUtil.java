package me.tulio.yang.utilities;

import lombok.experimental.UtilityClass;
import me.tulio.yang.Yang;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.scheduler.BukkitRunnable;

@UtilityClass
public class TaskUtil {

    public void run(Runnable runnable) {
        Yang.get().getServer().getScheduler().runTask(Yang.get(), runnable);
    }

    public void runAsync(Runnable runnable) {
        try {
            Yang.get().getServer().getScheduler().runTaskAsynchronously(Yang.get(), runnable);
        } catch (IllegalStateException e) {
            Yang.get().getServer().getScheduler().runTask(Yang.get(), runnable);
        } catch (IllegalPluginAccessException e) {
            new Thread(runnable).start();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void runTimer(Runnable runnable, long delay, long timer) {
        Yang.get().getServer().getScheduler().runTaskTimer(Yang.get(), runnable, delay, timer);
    }

    public int runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(Yang.get(), delay, timer);
        return runnable.getTaskId();
    }

    public void runLater(Runnable runnable, long delay) {
        Yang.get().getServer().getScheduler().runTaskLater(Yang.get(), runnable, delay);
    }

    public void runLaterAsync(Runnable runnable, long delay) {
        try {
            Yang.get().getServer().getScheduler().runTaskLaterAsynchronously(Yang.get(), runnable, delay);
        } catch (IllegalStateException e) {
            Yang.get().getServer().getScheduler().runTaskLater(Yang.get(), runnable, delay);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void runTimerAsync(Runnable runnable, long delay, long timer) {
        try {
            Yang.get().getServer().getScheduler().runTaskTimerAsynchronously(Yang.get(), runnable, delay, timer);
        } catch (IllegalStateException e) {
            Yang.get().getServer().getScheduler().runTaskTimer(Yang.get(), runnable, delay, timer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimerAsynchronously(Yang.get(), delay, timer);
    }

}
