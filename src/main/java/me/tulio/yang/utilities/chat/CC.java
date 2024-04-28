package me.tulio.yang.utilities.chat;

import me.tulio.yang.Yang;
import me.tulio.yang.arena.Arena;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.tablist.impl.TabList;
import me.tulio.yang.utilities.file.languaje.Lang;
import me.tulio.yang.utilities.license.License;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.*;

public class CC {

    private static final Map<String, ChatColor> MAP;

    public static final String BLUE;
    public static final String AQUA;
    public static final String YELLOW;
    public static final String RED;
    public static final String GRAY;
    public static final String GOLD;
    public static final String GREEN;
    public static final String WHITE;
    public static final String BLACK;
    public static final String BOLD;
    public static final String ITALIC;
    public static final String UNDER_LINE;
    public static final String STRIKE_THROUGH;
    public static final String RESET;
    public static final String MAGIC;
    public static final String DARK_BLUE;
    public static final String DARK_AQUA;
    public static final String DARK_GRAY;
    public static final String DARK_GREEN;
    public static final String DARK_PURPLE;
    public static final String DARK_RED;
    public static final String PINK;
    public static final String MENU_BAR;
    public static final String CHAT_BAR;
    public static final String SB_BAR;
    public static final String TAB_BAR;

    static {
        MAP = new HashMap<>();
        MAP.put("pink", ChatColor.LIGHT_PURPLE);
        MAP.put("orange", ChatColor.GOLD);
        MAP.put("purple", ChatColor.DARK_PURPLE);

        for (ChatColor chatColor : ChatColor.values()) {
            MAP.put(chatColor.name().toLowerCase().replace("_", ""), chatColor);
        }

        BLUE = ChatColor.BLUE.toString();
        AQUA = ChatColor.AQUA.toString();
        YELLOW = ChatColor.YELLOW.toString();
        RED = ChatColor.RED.toString();
        GRAY = ChatColor.GRAY.toString();
        GOLD = ChatColor.GOLD.toString();
        GREEN = ChatColor.GREEN.toString();
        WHITE = ChatColor.WHITE.toString();
        BLACK = ChatColor.BLACK.toString();
        BOLD = ChatColor.BOLD.toString();
        ITALIC = ChatColor.ITALIC.toString();
        UNDER_LINE = ChatColor.UNDERLINE.toString();
        STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
        RESET = ChatColor.RESET.toString();
        MAGIC = ChatColor.MAGIC.toString();
        DARK_BLUE = ChatColor.DARK_BLUE.toString();
        DARK_AQUA = ChatColor.DARK_AQUA.toString();
        DARK_GRAY = ChatColor.DARK_GRAY.toString();
        DARK_GREEN = ChatColor.DARK_GREEN.toString();
        DARK_PURPLE = ChatColor.DARK_PURPLE.toString();
        DARK_RED = ChatColor.DARK_RED.toString();
        PINK = ChatColor.LIGHT_PURPLE.toString();
        MENU_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------";
        CHAT_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------------";
        SB_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----------------------";
        TAB_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------";
    }

    public static Set<String> getColorNames() {
        return MAP.keySet();
    }

    public static ChatColor getColorFromName(String name) {
        if (MAP.containsKey(name.trim().toLowerCase())) {
            return MAP.get(name.trim().toLowerCase());
        }

        ChatColor color;

        try {
            color = ChatColor.valueOf(name.toUpperCase().replace(" ", "_"));
        } catch (Exception e) {
            return null;
        }

        return color;
    }

    public static String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public static List<String> translate(List<String> lines) {
        List<String> toReturn = new ArrayList<>();

        for (String line : lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return toReturn;
    }

    public static String[] translate(String[] lines) {
        List<String> toReturn = new ArrayList<>();

        for (String line : lines) {
            if (line != null) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }

        return toReturn.toArray(new String[toReturn.size()]);
    }

    public static String strip(String message) {
        return ChatColor.stripColor(message);
    }

    public static ChatColor getByName(String name) {
        for (ChatColor chatColor : ChatColor.values()) {
            if (chatColor.name().equalsIgnoreCase(name)) {
                return chatColor;
            }
        }
        return null;
    }

    public static boolean isColor(net.md_5.bungee.api.ChatColor color) {
        return color != null &&
                (color != net.md_5.bungee.api.ChatColor.STRIKETHROUGH && color != net.md_5.bungee.api.ChatColor.MAGIC
                        && color != net.md_5.bungee.api.ChatColor.BOLD && color != net.md_5.bungee.api.ChatColor.ITALIC
                        && color != net.md_5.bungee.api.ChatColor.UNDERLINE && color != net.md_5.bungee.api.ChatColor.RESET);
    }

    public static void failedLicense() {
        Bukkit.getConsoleSender().sendMessage(CHAT_BAR);
        Bukkit.getConsoleSender().sendMessage(translate("&2Checking license..."));
        Bukkit.getConsoleSender().sendMessage(CHAT_BAR);
        Bukkit.getConsoleSender().sendMessage(translate("&cInvalid License."));
        Bukkit.getConsoleSender().sendMessage(translate(" "));
        Bukkit.getConsoleSender().sendMessage(translate("&cJoin our discord server for support."));
        Bukkit.getConsoleSender().sendMessage(translate("&chttps://discord.io/Panda-Community"));
        Bukkit.getConsoleSender().sendMessage(CHAT_BAR);
    }

    public static void checkLicense(License license) {
        Bukkit.getConsoleSender().sendMessage(CHAT_BAR);
        Bukkit.getConsoleSender().sendMessage(translate("&2Checking license..."));
        Bukkit.getConsoleSender().sendMessage(CHAT_BAR);
        Bukkit.getConsoleSender().sendMessage(translate("&aSuccessfully loaded license."));
        Bukkit.getConsoleSender().sendMessage(translate("&aYang 2.3.8 Cracked & Bugfixed by Lugami."));
        Bukkit.getConsoleSender().sendMessage(translate(" "));
        Bukkit.getConsoleSender().sendMessage(translate("&2Thanks for purchase in Panda Community."));
        Bukkit.getConsoleSender().sendMessage(translate("&2https://discord.io/Panda-Community"));
        Bukkit.getConsoleSender().sendMessage(CHAT_BAR);
    }

    public static void successfullyLicense() {
        Bukkit.getConsoleSender().sendMessage(translate("&aLoading plugin..."));
    }

    public static void loadPlugin() {
        Bukkit.getConsoleSender().sendMessage(CHAT_BAR);
        Bukkit.getConsoleSender().sendMessage(translate("     &a\u2618 &2&l" + Yang.get().getName() + " &a\u2618"));
        Bukkit.getConsoleSender().sendMessage(translate(""));
        Bukkit.getConsoleSender().sendMessage(translate(" &f\u27A5 &aAuthor&7: &f" + Yang.get().getDescription()
                .getAuthors().toString().replace("[", "").replace("]", "")));
        Bukkit.getConsoleSender().sendMessage(translate(" &f\u27A5 &aVersion&7: &f" + Yang.get().getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(translate(" &f\u27A5 &aRank System&7: &f" + Yang.get().getRankManager().getRank().getRankSystem()));
        Bukkit.getConsoleSender().sendMessage(translate(""));
        Bukkit.getConsoleSender().sendMessage(translate(" &2General Info"));
        Bukkit.getConsoleSender().sendMessage(translate("  &f\u27A5 &aLanguage Default&7: &f" +
                Lang.getByAbbreviation(Yang.get().getMainConfig().getString("DEFAULT_LANG")).name()));
        Bukkit.getConsoleSender().sendMessage(translate("  &f\u27A5 &aSave Method&7: &f" + Yang.get().getMainConfig()
                .getString("SAVE_METHOD").toUpperCase()));
        Bukkit.getConsoleSender().sendMessage(translate("  &f\u27A5 &aTablist Default Mode&7: &f" + TabList.DEFAULT_TAB_TYPE.getFormat().toUpperCase()));
        Bukkit.getConsoleSender().sendMessage(translate(" "));
        Bukkit.getConsoleSender().sendMessage(translate(" &2Loaded Info"));
        Bukkit.getConsoleSender().sendMessage(translate("  &f\u27A5 &aArenas&7: &f" + Arena.getArenas().size()));
        Bukkit.getConsoleSender().sendMessage(translate("  &f\u27A5 &aKits&7: &f" + Kit.getKits().size()));
        int count = 0;
        for (Kit kit : Kit.getKits()) {
            if (kit.getGameRules().isRanked()) {
                count++;
            }
        }
        Bukkit.getConsoleSender().sendMessage(translate("  &f\u27A5 &aKits Ranked&7: &f" + count));
        Bukkit.getConsoleSender().sendMessage(translate("  &f\u27A5 &aClans&7: &f" + Clan.getClans().size()));
        Bukkit.getConsoleSender().sendMessage(CHAT_BAR);
    }
}
