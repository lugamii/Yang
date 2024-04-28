// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.rank.impl;

import me.abhi.core.profile.CoreProfile;
import me.abhi.core.CorePlugin;
import me.abhi.core.rank.Rank;
import java.util.UUID;
import me.tulio.yang.utilities.rank.RankObject;
import me.tulio.yang.utilities.rank.IRank;

public class mCore implements IRank, RankObject
{
    @Override
    public String getRankSystem() {
        return "mCore";
    }
    
    @Override
    public String getName(final UUID uuid) {
        return (this.getRank(uuid) == null) ? "None" : this.getRank(uuid).getName();
    }
    
    @Override
    public String getPrefix(final UUID uuid) {
        return (this.getRank(uuid) == null) ? "None" : this.getRank(uuid).getPrefix();
    }
    
    @Override
    public String getSuffix(final UUID uuid) {
        return (this.getRank(uuid) == null) ? "None" : this.getRank(uuid).getSuffix();
    }
    
    @Override
    public String getColor(final UUID uuid) {
        return (this.getRank(uuid) == null) ? "None" : "";
    }
    
    @Override
    public int getWeight(final UUID uuid) {
        return (this.getRank(uuid) == null) ? 0 : this.getRank(uuid).getWeight();
    }
    
    @Override
    public Rank getRank(final UUID uuid) {
        final CoreProfile coreProfile = CorePlugin.getInstance().getProfileHandler().getCoreProfile(uuid);
        try {
            return coreProfile.getRank();
        }
        catch (final Exception ex) {
            return null;
        }
    }
}
