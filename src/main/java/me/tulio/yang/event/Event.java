package me.tulio.yang.event;

import me.tulio.yang.Yang;
import me.tulio.yang.event.game.EventGame;
import me.tulio.yang.event.game.EventGameLogic;
import me.tulio.yang.event.impl.gulag.GulagEvent;
import me.tulio.yang.event.impl.spleef.SpleefEvent;
import me.tulio.yang.event.impl.sumo.SumoEvent;
import me.tulio.yang.event.impl.tntrun.TNTRunEvent;
import me.tulio.yang.event.impl.tnttag.TNTTagEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public interface Event {

	List<Event> events = new ArrayList<>();

	static void init() {
		add(new SumoEvent());
		add(new SpleefEvent());
		add(new TNTRunEvent());
		add(new GulagEvent());
		add(new TNTTagEvent());
	}

	static void add(Event event) {
		events.add(event);
		for (Listener listener : event.getListeners()) {
			Yang.get().getServer().getPluginManager().registerEvents(listener, Yang.get());
		}
	}

	static <T extends Event> T getEvent(Class<? extends Event> clazz) {
		for (Event event : events) {
			if (event.getClass() == clazz) {
				return (T) clazz.cast(event);
			}
		}

		return null;
	}

	static Event getByName(String name) {
		for (Event event : events) {
			if (event.getName().equalsIgnoreCase(name)) {
				return event;
			}
		}
		return null;
	}

	String getName();

	String getDisplayName();

	List<String> getDescription();

	Location getLobbyLocation();

	void setLobbyLocation(Location location);

	ItemStack getIcon();

	boolean canHost(Player player);

	List<String> getAllowedMaps();

	List<Listener> getListeners();

	default List<Object> getCommands() {
		return new ArrayList<>();
	}

	EventGameLogic start(EventGame game);

	void save();

}
