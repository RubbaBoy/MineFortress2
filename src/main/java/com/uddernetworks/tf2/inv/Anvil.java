package com.uddernetworks.tf2.inv;

import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.AnvilChooser;
import com.uddernetworks.tf2.utils.HashMap3;
import net.minecraft.server.v1_10_R1.ChatMessage;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.PacketPlayOutOpenWindow;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Anvil implements Listener {

    private static HashMap3<Player, Inventory, Throwable> anvils = new HashMap3<>();
    private static ArrayList<Player> closing = new ArrayList<>();

    public void openAnvilInventory(final Player player, Throwable throwable) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        FakeAnvil fakeAnvil = new FakeAnvil(entityPlayer);
        int containerId = entityPlayer.nextContainerCounter();

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId, "minecraft:anvil", new ChatMessage("Repairing", new Object[]{}), 0));

        entityPlayer.activeContainer = fakeAnvil;
        entityPlayer.activeContainer.windowId = containerId;
        entityPlayer.activeContainer.addSlotListener(entityPlayer);
        entityPlayer.activeContainer = fakeAnvil;
        entityPlayer.activeContainer.windowId = containerId;

        anvils.put(player, entityPlayer.activeContainer.getBukkitView().getTopInventory());

        Inventory inv = fakeAnvil.getBukkitView().getTopInventory();
        ItemStack stack = new ItemStack(Material.PAPER);
        ItemMeta meta = stack.getItemMeta();
        if (!Main.getPlugin().anonError()) {
            if (AnvilChooser.getPlayer(player) == 0) {
                meta.setDisplayName("your email");
                anvils.setT(player, throwable);
            } else {
                meta.setDisplayName("what led to this");
            }
        } else {
            anvils.setT(player, throwable);
            meta.setDisplayName("what led to this");
        }
        stack.setItemMeta(meta);
        inv.setItem(0, stack);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        try {
            if ((event.getWhoClicked() instanceof Player)) {
                Player player = (Player) event.getWhoClicked();
                if (anvils.containsKey(player) && event.getInventory().equals(anvils.get(player))) {
                    event.setCancelled(true);
                    int slot = event.getRawSlot();
                    String name = "";
                    if (slot == 2) {
                        ItemMeta meta = event.getCurrentItem().getItemMeta();
                        if (meta.hasDisplayName()) {
                            name = meta.getDisplayName();
                            AnvilChooser.setThrow(player, anvils.getT(player));
                            closing.add(player);
                            player.closeInventory();
                            anvils.remove(player);
                            new AnvilChooser(player, name);
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if ((event.getPlayer() instanceof Player)) {
            Player player = (Player) event.getPlayer();
            Inventory inv = event.getInventory();
            if (inv.equals(anvils.get(player))) {
                if (closing.contains(player)) {
                    closing.remove(player);
                } else {
                    inv.clear();
                    anvils.remove(event.getPlayer());
                    AnvilChooser.clear(player);
                }
            }
        }
    }

}