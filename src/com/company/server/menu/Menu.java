package com.company.server.menu;

public abstract class Menu {
    private boolean isRunMenu = true;

    public void startMenu() {
        isRunMenu = true;
    }

    public void stopMenu() {
        isRunMenu = false;
    }

    public boolean isRunMenu() {
        return isRunMenu;
    }

    public abstract String getUsername();
}
