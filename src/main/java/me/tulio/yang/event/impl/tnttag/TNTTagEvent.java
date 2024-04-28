package me.tulio.yang.event.impl.tnttag;

import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Yang;
import me.tulio.yang.event.Event;
import me.tulio.yang.event.game.EventGame;
import me.tulio.yang.event.game.EventGameLogic;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.LocationUtil;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.file.type.BasicConfigurationFile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TNTTagEvent implements Event {

    @Setter private Location lobbyLocation;
    @Getter private final List<String> allowedMaps;

    public TNTTagEvent() {
        BasicConfigurationFile config = Yang.get().getEventsConfig();

        lobbyLocation = LocationUtil.deserialize(config.getString("EVENTS.TNTTAG.LOBBY_LOCATION"));

        allowedMaps = new ArrayList<>();
        allowedMaps.addAll(config.getStringList("EVENTS.TNTTAG.ALLOWED_MAPS"));
    }

    @Override
    public String getName() {
        return "TNTTag";
    }

    @Override
    public String getDisplayName() {
        return CC.translate(Yang.get().getEventsConfig().getString("EVENTS.TNTTAG.DISPLAYNAME"));
    }

    @Override
    public List<String> getDescription() {
        return Yang.get().getEventsConfig().getStringList("EVENTS.TNTTAG.DESCRIPTION");
    }

    @Override
    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemBuilder(Material.valueOf(Yang.get().getEventsConfig().getString("EVENTS.TNTTAG.ICON"))).build();
    }

    @Override
    public boolean canHost(Player player) {
        return player.hasPermission("yang.event.host.tnttag");
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.emptyList();
    }

    @Override
    public List<Object> getCommands() {
        return Collections.emptyList();
    }

    @Override
    public EventGameLogic start(EventGame game) {
        return new TNTTagGameLogic(game);
    }

    @Override
    public void save() {
        FileConfiguration config = Yang.get().getEventsConfig().getConfiguration();
        config.set("EVENTS.TNTTAG.LOBBY_LOCATION", LocationUtil.serialize(lobbyLocation));
        config.set("EVENTS.TNTTAG.ALLOWED_MAPS", allowedMaps);

        try {
            config.save(Yang.get().getEventsConfig().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}