// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.string;

import java.util.Collections;
import me.tulio.yang.Yang;
import org.bukkit.ChatColor;

public class BridgeUtils
{
    public static String getStringPoint(final int points, final ChatColor color, final int pointsToWin) {
        final String x = Yang.get().getLangConfig().getString("MATCH.BRIDGE_SYMBOL");
        return color + String.join("", Collections.nCopies(pointsToWin - (pointsToWin - points), x)) + "&f" + String.join("", Collections.nCopies(pointsToWin - points, x));
    }
}
