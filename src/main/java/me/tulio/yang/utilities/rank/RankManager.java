// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.rank;

import java.util.Iterator;
import me.tulio.yang.utilities.rank.impl.Default;
import org.bukkit.Bukkit;
import me.tulio.yang.utilities.ClassHelper;

public class RankManager
{
    private IRank rank;
    
    public RankManager() {
        this.verifyRank();
    }
    
    public void verifyRank() {
        boolean foundRank = false;
        for (final Class<?> aClass : ClassHelper.getClassesInPackage("me.tulio.yang.utilities.rank.impl")) {
            final String className = aClass.getSimpleName();
            if (Bukkit.getPluginManager().getPlugin(className) != null) {
                try {
                    this.rank = (IRank)aClass.newInstance();
                    foundRank = true;
                    break;
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (!foundRank) {
            this.rank = new Default();
        }
    }
    
    public IRank getRank() {
        return this.rank;
    }
}
