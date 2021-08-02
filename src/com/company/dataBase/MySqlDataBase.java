package com.company.dataBase;

import java.sql.*;
import java.util.Locale;

public class MySqlDataBase implements DataBase {
    private final Connection connects;

    /**
     * @param serverName
     * @param port
     * @param nameDB
     * @param userName
     * @param userPassword
     * @throws SQLException
     */
    public MySqlDataBase(String serverName, String port, String nameDB, String userName, String userPassword) throws SQLException {
        String url = String.format("jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=true&useSSL=false", serverName, port, nameDB);

        connects = DriverManager.getConnection(url, userName, userPassword);
    }

    @Override
    public boolean isRealAccount(String username, String password) {
        try {
            String query = "select * from Users where username = ?;";
            PreparedStatement st = connects.prepareStatement(query);
            st.setString(1, username);
            ResultSet resultSet = st.executeQuery();

            boolean isFound = false;
            while (resultSet.next()) {
                isFound = true;
                String passwordDB = resultSet.getString(2);
                if (!passwordDB.equals(password)) {
                    return false;
                }
            }
            if (isFound) {
                createAccount(username, password);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private void createAccount(String username, String password) {
        try {
            String query = "insert into Users (username, password) values (?, ?);";
            PreparedStatement st = connects.prepareStatement(query);
            st.setString(1, username);
            st.setString(2, password.toLowerCase(Locale.ROOT));
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getRecorde() {
        try {
            String query = "select * from Records;";
            PreparedStatement st = connects.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            ResultSetMetaData rsMeta = rs.getMetaData();
            int columnCount = rsMeta.getColumnCount();

            rs.next();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsMeta.getColumnName(i);
                System.out.format("%s:%s\n", columnName, rs.getString(i));
            }

            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void addRecorde(String username, int sizeSnake) {
        try {
            String query = "insert into Records (username, snakesize) values (?, ?)";
            PreparedStatement st = connects.prepareStatement(query);
            st.setString(1, username);
            st.setInt(2, sizeSnake);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
