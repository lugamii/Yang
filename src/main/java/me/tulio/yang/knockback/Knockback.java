package me.tulio.yang.knockback;

import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Yang;
import me.tulio.yang.knockback.impl.Default;
import me.tulio.yang.knockback.impl.FoxSpigot;
import me.tulio.yang.knockback.impl.InsanePaperSpigot;

public class Knockback {

    @Getter @Setter public static KnockbackProfiler knockbackProfiler;

    public static void init() {
        switch (Yang.get().getServer().getName()) {
            case "FoxSpigot":
                knockbackProfiler = new FoxSpigot();
                break;
            case "InsanePaper":
                knockbackProfiler = new InsanePaperSpigot();
                break;
            default:
                knockbackProfiler = new Default();
                System.out.print("You don't have a spigot compatible with Yang's Knockbacks");
                System.out.print("You don't have a spigot compatible with Yang's Knockbacks");
                System.out.print("You don't have a spigot compatible with Yang's Knockbacks");
                break;
        }
    }
}
