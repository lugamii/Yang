package me.tulio.yang.queue.menu;

import com.google.common.collect.Maps;
import me.tulio.yang.Yang;
import me.tulio.yang.utilities.ItemBuilder;
import me.tulio.yang.utilities.enchantment.Glow;
import me.tulio.yang.utilities.menu.Button;
import me.tulio.yang.utilities.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class QueuesListMenu extends Menu {

    {
        setPlaceholder(Yang.get().getQueueConfig().getBoolean("QUEUE.QUEUES_INVENTORY.FILL_VOID_SLOTS"));
    }

    @Override
    public String getTitle(Player player) {
        return Yang.get().getQueueConfig().getString("QUEUE.QUEUES_INVENTORY.TITLE");
    }

    @Override
    public int getSize() {
        return 9*Yang.get().getQueueConfig().getInteger("QUEUE.QUEUES_INVENTORY.ROWS");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        // Unranked Button
        buttons.put(Yang.get().getQueueConfig().getInteger("QUEUE.QUEUES_INVENTORY.UNRANKED.SLOT"),
                new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        List<String> lore = Yang.get().getQueueConfig().getStringList("QUEUE.QUEUES_INVENTORY.UNRANKED.LORE");
                        lore.replaceAll(s -> s
                                .replace("{players}", String.valueOf(Yang.get().getInFightsUnRanked())));
                        return new ItemBuilder(
                                Material.valueOf(Yang.get().getQueueConfig().getString("QUEUE.QUEUES_INVENTORY.UNRANKED.MATERIAL")))
                                .name(Yang.get().getQueueConfig().getString("QUEUE.QUEUES_INVENTORY.UNRANKED.NAME"))
                                .lore(lore)
                                .durability(Yang.get().getQueueConfig().getInteger("QUEUE.QUEUES_INVENTORY.UNRANKED.DATA"))
                                .enchantment(Yang.get().getQueueConfig().getBoolean("QUEUE.QUEUES_INVENTORY.UNRANKED.GLOW")
                                        ? new Glow(0) : null)
                                .build();
                    }

                    @Override
                    public void clicked(Player player, ClickType clickType) {
                        new QueueSelectKitMenu(false).openMenu(player);
                    }
                });

        // Ranked Button
        buttons.put(Yang.get().getQueueConfig().getInteger("QUEUE.QUEUES_INVENTORY.RANKED.SLOT"),
                new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        List<String> lore = Yang.get().getQueueConfig().getStringList("QUEUE.QUEUES_INVENTORY.RANKED.LORE");
                        lore.replaceAll(s -> s
                                .replace("{players}", String.valueOf(Yang.get().getInFightsRanked())));
                        return new ItemBuilder(
                                Material.valueOf(Yang.get().getQueueConfig().getString("QUEUE.QUEUES_INVENTORY.RANKED.MATERIAL")))
                                .name(Yang.get().getQueueConfig().getString("QUEUE.QUEUES_INVENTORY.RANKED.NAME"))
                                .lore(lore)
                                .durability(Yang.get().getQueueConfig().getInteger("QUEUE.QUEUES_INVENTORY.RANKED.DATA"))
                                .enchantment(Yang.get().getQueueConfig().getBoolean("QUEUE.QUEUES_INVENTORY.RANKED.GLOW")
                                        ? new Glow(0) : null)
                                .build();
                    }

                    @Override
                    public void clicked(Player player, ClickType clickType) {
                        new QueueSelectKitMenu(true).openMenu(player);
                    }
                });
        return buttons;
    }
}
