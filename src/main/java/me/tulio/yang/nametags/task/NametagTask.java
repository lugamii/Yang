package me.tulio.yang.nametags.task;

import lombok.Getter;
import me.tulio.yang.nametags.NameTag;
import me.tulio.yang.nametags.NametagUpdate;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NametagTask implements Runnable {

    @Getter private static final Map<NametagUpdate, Boolean> pendingUpdates = new ConcurrentHashMap<>();

    @Override
    public void run() {
        Iterator<NametagUpdate> pendingUpdatesIterator = pendingUpdates.keySet().iterator();

        while (pendingUpdatesIterator.hasNext()) {
            NametagUpdate pendingUpdate = pendingUpdatesIterator.next();

            try {
                NameTag.applyUpdate(pendingUpdate);
                pendingUpdatesIterator.remove();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
