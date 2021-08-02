package com.company.dataBase;

public interface DataBase {
    boolean isRealAccount(String userName, String password);
    String getRecorde();
    void addRecorde(String userName, int sizeSnake);
}
