// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.string;

import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public final class TPSUtil
{
    public static String getTPS() {
        return ((getTicksPerSecond() > 18.0) ? ChatColor.GREEN : ((getTicksPerSecond() > 16.0) ? ChatColor.YELLOW : ChatColor.RED)) + ((getTicksPerSecond() > 20.0) ? "*" : "") + Math.min(Math.round(getTicksPerSecond() * 100.0) / 100.0, 20.0);
    }
    
    private static double getTicksPerSecond() {
        return Math.min(Math.round(Bukkit.spigot().getTPS()[0] * 100.0) / 100.0, 20.0);
    }
    
    public static String getCoolestTPS(final int bars) {
        return getProgressBar(getTicksPerSecond() * 100.0 / 20.0, 100, bars, '┃', ChatColor.GREEN, ChatColor.RED);
    }
    
    public static String getProgressBar(final double current, final int max, final int totalBars, final char symbol, final ChatColor completedColor, final ChatColor notCompletedColor) {
        final float percent = (float)(current / max);
        final int progressBars = (int)(totalBars * percent);
        return Strings.repeat("" + completedColor + symbol, progressBars) + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }
    
    private TPSUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
