package com.company.server.menu;

public class PerformanceServer implements Performance {
    private String username;
    private ConsoleUI consoleUI;

    public PerformanceServer(String username, ConsoleUI consoleUI) {
        this.username = username;
        this.consoleUI = consoleUI;
    }

    public void runMenu() {
        synchronized (consoleUI) {
            consoleUI.notifyAll();
        }
    }

    public String getUsername() {
        return username;
    }
}