// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.rank.impl;

import me.quartz.hestia.HestiaAPI;
import java.util.UUID;
import me.tulio.yang.utilities.rank.IRank;

public class HestiaCore implements IRank
{
    @Override
    public String getRankSystem() {
        return "HestiaCore";
    }
    
    @Override
    public String getName(final UUID uuid) {
        return HestiaAPI.instance.getRank(uuid);
    }
    
    @Override
    public String getPrefix(final UUID uuid) {
        return HestiaAPI.instance.getRankPrefix(uuid);
    }
    
    @Override
    public String getSuffix(final UUID uuid) {
        return HestiaAPI.instance.getRankSuffix(uuid);
    }
    
    @Override
    public String getColor(final UUID uuid) {
        return "";
    }
    
    @Override
    public int getWeight(final UUID uuid) {
        return 0;
    }
}
