package com.company;

import com.company.dataBase.DataBase;
import com.company.dataBase.MySqlDataBase;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class ParseConfig {
    static class ConfigDB {
        private String serverName;
        private int port;
        private String nameDB;
        private String username;
        private String userPassword;

        public String getServerName() {
            return serverName;
        }

        public int getPort() {
            return port;
        }

        public String getNameDB() {
            return nameDB;
        }

        public String getUsername() {
            return username;
        }

        public String getUserPassword() {
            return userPassword;
        }

    }


    private static String fileToString(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public static DataBase createDataBase(String[] args) throws SQLException, IOException {
        String nameConfigPath = "config.json";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-config")) {
                nameConfigPath = args[i + 1];
            }
        }

        ConfigDB configDB = new Gson().fromJson(fileToString(nameConfigPath), ConfigDB.class);

        return new MySqlDataBase(
                configDB.getServerName(),
                String.valueOf(configDB.getPort()),
                configDB.getNameDB(),
                configDB.getUsername(),
                configDB.getUserPassword());
    }
}