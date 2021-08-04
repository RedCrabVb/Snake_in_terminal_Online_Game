package com.company.server.menu;

import com.company.server.Room;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public abstract class UI {
    public abstract void showAllRooms();

    public abstract void connectionToRooms(int number);

    public abstract void createRoom(String nameRoom);

    public abstract void showRecorde();

    public abstract void createServer(int port, List<Room> rooms);

    public abstract void connectionServer(String ip, int port) throws IOException;
}
