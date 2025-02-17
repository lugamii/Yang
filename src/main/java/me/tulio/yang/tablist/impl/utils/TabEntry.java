package me.tulio.yang.tablist.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.tablist.impl.GhostlyTablist;

import java.util.UUID;

@Getter @Setter @AllArgsConstructor
public class TabEntry {

    private String id;
    private UUID uuid;
    private String text;
    private GhostlyTablist tab;
    private SkinTexture texture;
    private TabColumn column;
    private int slot, rawSlot, latency;

}
