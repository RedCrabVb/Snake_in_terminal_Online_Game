package com.company.server.performance;

import com.company.server.menu.ConsoleUI;

public class PerformanceServer implements Performance {
    private String username;
    private ConsoleUI consoleUI;

    public PerformanceServer(String username, ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
        this.username = username;
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