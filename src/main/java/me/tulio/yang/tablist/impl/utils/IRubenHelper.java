package me.tulio.yang.tablist.impl.utils;

import me.tulio.yang.tablist.impl.GhostlyTablist;
import org.bukkit.entity.Player;

import java.util.List;

public interface IRubenHelper {

    TabEntry createFakePlayer(GhostlyTablist zigguratTablist, String string, TabColumn column, Integer slot, Integer rawSlot);

    void recreatePlayer(GhostlyTablist tablist, Player player);

    void updateFakeName(GhostlyTablist zigguratTablist, TabEntry tabEntry, String text);

    void updateFakeLatency(GhostlyTablist zigguratTablist, TabEntry tabEntry, Integer latency);

    void updateFakeSkin(GhostlyTablist zigguratTablist, TabEntry tabEntry, SkinTexture skinTexture);

    void updateHeaderAndFooter(GhostlyTablist zigguratTablist, List<String> header, List<String> footer);

    void destoryFakePlayer(GhostlyTablist zigguratTablist, TabEntry tabEntry, String customName);

    // TODO some sort of inbuilt caching system on join???
    SkinTexture getTexture(Player player);

}
