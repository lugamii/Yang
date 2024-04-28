// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.rank.impl;

import org.bukkit.entity.Player;
import java.util.UUID;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.Bukkit;
import net.milkbowl.vault.chat.Chat;
import me.tulio.yang.utilities.rank.IRank;

public class PermissionsEx implements IRank
{
    private Chat chat;
    
    public PermissionsEx() {
        final RegisteredServiceProvider<Chat> rsp = (RegisteredServiceProvider<Chat>)Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp != null) {
            this.chat = (Chat)rsp.getProvider();
        }
    }
    
    @Override
    public String getRankSystem() {
        return "PermissionsEx";
    }
    
    @Override
    public String getName(final UUID uuid) {
        return this.chat.getPrimaryGroup(this.getPlayer(uuid));
    }
    
    @Override
    public String getPrefix(final UUID uuid) {
        return this.chat.getPlayerPrefix(this.getPlayer(uuid));
    }
    
    @Override
    public String getSuffix(final UUID uuid) {
        return this.chat.getPlayerSuffix(this.getPlayer(uuid));
    }
    
    @Override
    public String getColor(final UUID uuid) {
        return "";
    }
    
    @Override
    public int getWeight(final UUID uuid) {
        return 0;
    }
    
    private Player getPlayer(final UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }
}
