package me.tulio.yang.tablist.impl.utils.ping.impl;

import me.tulio.yang.tablist.impl.utils.ping.IPingProvider;
import org.bukkit.entity.Player;

public class LunarPingImpl implements IPingProvider {

    @Override
    public int getDefaultPing(Player player) {
        return 0;
    }

}
