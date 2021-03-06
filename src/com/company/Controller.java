package com.company;

public enum Controller {
    left("w", "s"),
    right("s", "w"),
    up("a", "d"),
    down("d", "a");

    private final String key, opposite;

    Controller(String key, String opposite) {
        this.key = key;
        this.opposite = opposite;
    }

    public String getKey() {
        return key;
    }

    public boolean isNotOpposite(Controller controller) {
        return !opposite.equals(controller.getKey());
    }
}
