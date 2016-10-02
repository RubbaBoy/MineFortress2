package com.uddernetworks.tf2.utils;

import com.uddernetworks.errorreport.Game;
import com.uddernetworks.errorreport.Report;
import com.uddernetworks.tf2.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ErrorHandler {

    public ErrorHandler(Player player, String email, String message, String thrown) {
        Report report = new Report(Game.MF2, email, Main.getPlugin().getDescription().getVersion(), message, thrown);
        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Sending error report to server...");
        report.send();

        String beginning = ChatColor.RED + "" + ChatColor.BOLD + "Error sending report: " + ChatColor.RESET;
        if (report.getStatusCode() == 200) {
            if (report.getFieldStatus() == 1) {
                player.sendMessage(beginning + "Not all fields present");
            } else if (report.getFieldStatus() == 2) {
                player.sendMessage(beginning + "Invalid game (Contact rubbaboy@uddernetworks.com)");
            } else if (report.getFieldStatus() == 3) {
                player.sendMessage(beginning + "Invalid email");
            } else if (report.getFieldStatus() == 4) {
                player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Sucessfully sent error message. Please expect an email shortly stating the error is being looked into.");
            }
        } else {
            player.sendMessage(beginning + "Something happened server-side, please try again later if you encounter the problem again.");
            System.out.println("Received status code " + report.getStatusCode() + " from server while uploading error. Try again later if the problem occurs.");
        }
    }

}