// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.rank.impl;

import com.broustudio.MizuAPI.MizuAPI;
import java.util.UUID;
import me.tulio.yang.utilities.rank.RankObject;
import me.tulio.yang.utilities.rank.IRank;

public class Mizu implements IRank, RankObject
{
    @Override
    public String getRankSystem() {
        return "MizuCore";
    }
    
    @Override
    public String getName(final UUID uuid) {
        return this.getRank(uuid);
    }
    
    @Override
    public String getPrefix(final UUID uuid) {
        return MizuAPI.getAPI().getRankPrefix(this.getRank(uuid));
    }
    
    @Override
    public String getSuffix(final UUID uuid) {
        return MizuAPI.getAPI().getRankSuffix(this.getRank(uuid));
    }
    
    @Override
    public String getColor(final UUID uuid) {
        return MizuAPI.getAPI().getRankColor(this.getRank(uuid));
    }
    
    @Override
    public int getWeight(final UUID uuid) {
        return 0;
    }
    
    @Override
    public String getRank(final UUID uuid) {
        return MizuAPI.getAPI().getRank(uuid);
    }
}
