package com.uddernetworks.tf2.arena;

import com.uddernetworks.tf2.main.Main;
import com.uddernetworks.tf2.utils.TeamEnum;
import net.minecraft.server.v1_9_R1.Entity;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Door;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TeamChooser implements Listener {

    Main main;

    static ArrayList<Player> players = new ArrayList<>();

    static HashMap<Player, Location> locs = new HashMap<>();

    static boolean blueDoor = true;
    static boolean redDoor = true;

    public TeamChooser(Main main) {
        this.main = main;
    }

    public void sendPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            locs.put(player, player.getLocation());
            Entity en = ((CraftPlayer)player).getHandle();
            en.setInvisible(true);
            player.teleport(main.teamChooseSpawn);
            players.add(player);
        }
    }

    @EventHandler
    public void onDoorClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if (block.getLocation().equals(main.blueDoor) || block.getLocation().equals(main.redDoor) || block.getLocation().equals(main.randomDoor)) {
                String doorName = event.getClickedBlock().getType().getData().getName();
                if (block.getType() == Material.DARK_OAK_DOOR || block.getType() == Material.SPRUCE_DOOR) {
                    player.sendMessage("You're clicking a door!! Tell rubba");
                    Door door = (Door) block.getState().getData();
                    if (door.isTopHalf()) {
                        block = block.getRelative(BlockFace.DOWN);
                    }
                }
                    if (!PlayerTeams.isSet(player)) {
                        event.setCancelled(true);
                        if (block.getLocation().serialize().equals(main.blueDoor.serialize())) {
                            if (blueDoor) {
                                player.sendMessage("blue door YEA red door is " + redDoor + " blue door is " + blueDoor);
                                PlayerTeams.addPlayer(player, TeamEnum.BLUE);
                            } else {
                                player.sendMessage(ChatColor.BLUE + "Joining this team would cause team unbalances");
                            }
                            reloadAbleDoors();
                        } else if (block.getLocation().serialize().equals(main.redDoor.serialize())) {
                            if (redDoor && !blueDoor) {
                                player.sendMessage("red door YEA red door is " + redDoor + " blue door is " + blueDoor);
                                PlayerTeams.addPlayer(player, TeamEnum.RED);
                            } else {
                                player.sendMessage(ChatColor.RED + "Joining this team would cause team unbalances");
                            }
                            reloadAbleDoors();
                        } else if (block.getLocation().serialize().equals(main.randomDoor.serialize())) {
                            Random random = new Random();
                            if (blueDoor && redDoor) {
                                if (random.nextBoolean()) {
                                    PlayerTeams.addPlayer(player, TeamEnum.BLUE);
                                } else {
                                    PlayerTeams.addPlayer(player, TeamEnum.RED);
                                }
                            } else if (blueDoor && !redDoor) {
                                PlayerTeams.addPlayer(player, TeamEnum.BLUE);
                            } else if (!blueDoor && redDoor) {
                                PlayerTeams.addPlayer(player, TeamEnum.RED);
                            }
                            reloadAbleDoors();
                        } else {
                            player.sendMessage("You didnt click a door");
                        }

                        Entity en = ((CraftPlayer) player).getHandle();
                        en.setInvisible(false);
                        player.teleport(locs.get(player));
                        changeSignText(main.blueSign, "", ChatColor.BLUE + "" + ChatColor.BOLD + "Blue", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                        changeSignText(main.redSign, "", ChatColor.BLUE + "" + ChatColor.RED + "Red", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
                    } else {
                        player.sendMessage("You already are set in the team list!");
                    }
                }
            }
        }

    public void reloadAbleDoors() {
        Bukkit.getPlayer("RubbaBoy").sendMessage("reloadAbleDoors");
        if (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() > PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()) {
            blueDoor = false;
            redDoor = true;
            Bukkit.getPlayer("RubbaBoy").sendMessage("2blue door: " + PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + " red door: " + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size());
            changeSignText(main.blueSign, "", ChatColor.STRIKETHROUGH + "" + ChatColor.BLUE + "" + ChatColor.BOLD + "Blue", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
        } else if (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() < PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()) {
            blueDoor = true;
            redDoor = false;
            Bukkit.getPlayer("RubbaBoy").sendMessage("blue door: " + PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + " red door: " + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size());
            changeSignText(main.redSign, "", ChatColor.STRIKETHROUGH + "" + ChatColor.BLUE + "" + ChatColor.RED + "Red", ChatColor.BOLD + "" + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size() + "/" + (PlayerTeams.getPlayersInTeam(TeamEnum.BLUE).size() + PlayerTeams.getPlayersInTeam(TeamEnum.RED).size()), "");
        } else {
            Bukkit.getPlayer("RubbaBoy").sendMessage("nahp");
            blueDoor = true;
            redDoor = true;
        }
    }

    public void changeSignText(Location location, String line1, String line2, String line3, String line4) {
        if (location.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) location.getBlock().getState();
            sign.setLine(0, line1);
            sign.setLine(1, line2);
            sign.setLine(2, line3);
            sign.setLine(3, line4);
            sign.update();
        } else {
            System.out.println("Location was not a sign!");
        }
    }


    public void toggleDoorState(Block block) {
        String doorName = block.getType().getData().getName();
        if (doorName.equalsIgnoreCase("org.bukkit.material.door")) {
            BlockState state = block.getState();
            Door door = (Door) state.getData();
            if (door.isOpen()) {
                door.setOpen(false);
            } else {
                door.setOpen(true);
            }
            state.update();
        }
    }

}