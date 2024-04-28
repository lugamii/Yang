// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.rank.impl;

import java.util.Optional;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.luckperms.api.cacheddata.CachedMetaData;
import java.util.Objects;
import net.luckperms.api.model.group.Group;
import java.util.UUID;
import org.bukkit.Bukkit;
import me.tulio.yang.utilities.rank.IRank;

public class LuckPerms implements IRank
{
    private final net.luckperms.api.LuckPerms luckPerms;
    
    public LuckPerms() {
        this.luckPerms = (net.luckperms.api.LuckPerms)Bukkit.getServicesManager().load(net.luckperms.api.LuckPerms.class);
    }
    
    @Override
    public String getRankSystem() {
        return "LuckPerms";
    }
    
    @Override
    public String getName(final UUID uuid) {
        return this.getMetaData(uuid).getPrimaryGroup();
    }
    
    @Override
    public String getPrefix(final UUID uuid) {
        return (this.getMetaData(uuid).getPrefix() == null) ? "" : this.getMetaData(uuid).getPrefix();
    }
    
    @Override
    public String getSuffix(final UUID uuid) {
        return (this.getMetaData(uuid).getSuffix() == null) ? "" : this.getMetaData(uuid).getSuffix();
    }
    
    @Override
    public String getColor(final UUID uuid) {
        return "";
    }
    
    @Override
    public int getWeight(final UUID uuid) {
        return Objects.requireNonNull(this.luckPerms.getGroupManager().getGroup(Objects.requireNonNull(this.getMetaData(uuid).getPrimaryGroup()))).getWeight().orElse(0);
    }
    
    private CachedMetaData getMetaData(final UUID uuid) {
        final User user = this.luckPerms.getUserManager().getUser(uuid);
        if (user == null) {
            throw new IllegalArgumentException("LuckPerms user could not be found");
        }
        final Optional<QueryOptions> queryOptions = this.luckPerms.getContextManager().getQueryOptions(user);
        if (!queryOptions.isPresent()) {
            throw new IllegalArgumentException("LuckPerms context could not be loaded");
        }
        return user.getCachedData().getMetaData(queryOptions.get());
    }
}
