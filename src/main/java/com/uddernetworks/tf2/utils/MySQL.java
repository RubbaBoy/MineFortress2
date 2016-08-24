package com.uddernetworks.tf2.utils;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.uddernetworks.tf2.main.Main;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.*;

public class MySQL {

    private static boolean loggedIn = false;

    private MysqlDataSource dataSource;

    public Main main;

    public MySQL(Main main) {
        this.main = main;
    }

    private Connection connect() {
        String url = "";
        try {
            url = "jdbc:sqlite:" + new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile() + "\\TF2\\Loadouts.db";
            System.out.println("url: " + url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void query(String query) {

        Connection conn = this.connect();
        try {
            Statement stmt = conn.createStatement();
            try {
                stmt.setQueryTimeout(30);
                System.out.println("Querying" + query);
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

//    public ResultSet queryReturnable(String query) {
//        Connection conn = this.connect();
//        try {
//            Statement stmt = conn.createStatement();
//            try {
//                stmt.setQueryTimeout(30);
//                System.out.println("Query returnable:" + query);
//                return stmt.executeQuery(query);
//            } finally {
//                try { stmt.close(); } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try { conn.close(); } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("Returning null!");
//        return null;
//    }



    public QueryResults queryReturnable(String query) {
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(query)){
            QueryResults results = new QueryResults();

            // loop through the result set
            while (rs.next()) {
                results.addResult(new QueryResult(rs.getString("UUID"), rs.getString("CLASS"), rs.getString("TYPE"), rs.getInt("ID"), rs.getString("NAME")));
            }
            return results;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


//    }



//        try (Connection conn = this.connect();
//             PreparedStatement pstmt = conn.prepareStatement(query)) {
//
//            System.out.println("QUERING: " + query);
//            for (int i = 0; i < args.length; i++) {
//                pstmt.setObject(i + 1, args[i]);
//            }
//            return pstmt.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }

//    }

}
