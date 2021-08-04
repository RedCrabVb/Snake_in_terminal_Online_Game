package com.company.server.menu;

import com.company.server.Room;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public interface UI {
    void showAllRooms();

    void connectionToRooms(int number, Performance performance);

    void createRoom(String nameRoom);

    void showRecorde();

    void createServer(int port);

    void connectionServer(String ip, int port) throws IOException;
}
