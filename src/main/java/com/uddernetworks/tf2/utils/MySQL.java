package com.uddernetworks.tf2.utils;

import com.uddernetworks.tf2.exception.ExceptionReporter;
import com.uddernetworks.tf2.main.Main;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;

public class MySQL {

    public Main main;

    public MySQL(Main main) {
        this.main = main;
    }

    private Connection connect() {
        String url;
        try {
            File file = new File(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile() + File.separator + "MF2" + File.separator + "Loadouts.db");

            if (!file.exists()) {
                System.out.println("The database file doesn't exist!");
                Bukkit.getPluginManager().disablePlugin(Main.plugin);
            }
            Class.forName("org.sqlite.JDBC");
            url = "jdbc:sqlite:" + file.getPath();


            return DriverManager.getConnection(url);
        } catch (Throwable throwable) {
            new ExceptionReporter(throwable);
            return null;
        }
    }

    public void query(String query) {
        Connection conn = this.connect();
        try {
            Statement stmt = conn.createStatement();
            try {
                stmt.setQueryTimeout(30);
                stmt.executeUpdate(query);
            } finally {
                try { stmt.close(); } catch (Exception ignore) {}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { conn.close(); } catch (Exception ignore) {}
        }
    }

    public QueryResults queryReturnable(String query) {
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(query)){
            QueryResults results = new QueryResults();

            while (rs.next()) {
                results.addResult(new QueryResult(rs.getString("UUID"), rs.getString("CLASS"), rs.getString("TYPE"), rs.getInt("ID"), rs.getString("NAME")));
            }
            return results;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}