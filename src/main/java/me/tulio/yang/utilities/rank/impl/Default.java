// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.rank.impl;

import java.util.UUID;
import me.tulio.yang.utilities.rank.IRank;

public class Default implements IRank
{
    @Override
    public String getRankSystem() {
        return "Default";
    }
    
    @Override
    public String getName(final UUID uuid) {
        return "";
    }
    
    @Override
    public String getPrefix(final UUID uuid) {
        return "";
    }
    
    @Override
    public String getSuffix(final UUID uuid) {
        return "";
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
