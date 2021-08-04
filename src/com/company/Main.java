package com.company;

/*class SocketAcceptConnection implements Runnable {
    private final int port;
    private final List<Room> rooms;

    SocketAcceptConnection(int port, List<Room> rooms) {
        this.port = port;
        this.rooms = rooms;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(port);

            while (!Thread.currentThread().isInterrupted()) {
                Socket socketClient = socket.accept();
                new Thread(new MenuClient(new DataTransfer(socketClient), rooms)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/

/*class ParseConfig {
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
}*/

public class Main {
//    public static DataBase dataBase;
/*    private final static List<Room> rooms = new ArrayList<>();

    public static String getListRooms() {
        var ref = new Object() {
            String print = "";
        };
        ref.print += "\nRooms:\n";
        AtomicInteger id = new AtomicInteger(0);
        rooms.forEach(r -> ref.print += (id.incrementAndGet() + " " + r.toString()) + "\n");
        return ref.print;
    }*/

/*    public static void removeRoom(Room room) {
        rooms.remove(room);
    }*/

    public static void main(String[] args) throws Exception {
       /* Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 1 or 2: \n 1) Create server \n 2) Connect server");
        String selection = scanner.nextLine();
        if (selection.equals("1")) {
            SocketAcceptConnection.dataBase = ParseConfig.createDataBase(args);
            System.out.println("Enter port for program");
            int port = scanner.nextInt();
            new Thread(new PerformanceServer("")).start();
            new Thread(new SocketAcceptConnection(port)).start();
        } else if (selection.equals("2")) {
            System.out.println("Enter ip server");
            String ip = "127.0.0.1";//scanner.nextLine();
            System.out.println("Enter port server");
            String port = scanner.nextLine();

            SnakeClientG snakeClientGame = new SnakeClientG(new DataTransferG(new Socket(ip, Integer.parseInt(port))));
            snakeClientGame.start();
        } else {
            throw new IOException("Error enter data");*/
        //}
    }

}
