// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.string;

import java.util.List;
import me.tulio.yang.utilities.TaskUtil;
import java.util.concurrent.atomic.AtomicInteger;
import me.tulio.yang.Yang;

public class Animation
{
    public static String title;
    public static String footer;
    
    public static void init() {
        final List<String> titles = Yang.get().getScoreboardConfig().getStringList("TITLE");
        final AtomicInteger p = new AtomicInteger();
        TaskUtil.runTimerAsync(() -> {
            if (p.get() == titles.size()) {
                p.set(0);
            }
            Animation.title = titles.get(p.getAndIncrement());
            return;
        }, 0L, (long)(Yang.get().getScoreboardConfig().getDouble("TITLE_TASK") * 20.0));
        if (Yang.get().getScoreboardConfig().getBoolean("FOOTER_ENABLED")) {
            final List<String> footers = Yang.get().getScoreboardConfig().getStringList("FOOTER");
            final AtomicInteger b = new AtomicInteger();
            TaskUtil.runTimerAsync(() -> {
                if (b.get() == footers.size()) {
                    b.set(0);
                }
                Animation.footer = footers.get(b.getAndIncrement());
            }, 0L, (long)(Yang.get().getScoreboardConfig().getDouble("FOOTER_TASK") * 20.0));
        }
    }
    
    public static String getScoreboardTitle() {
        return Animation.title;
    }
    
    public static String getScoreboardFooter() {
        return Animation.footer;
    }
}
