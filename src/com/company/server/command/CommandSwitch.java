package com.company.server.command;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class CommandSwitch {
    private Map<String, Command> map = new HashMap<>();

    public void register(String commandName, Command command) {
        map.put(commandName, command);
    }

    public void execute(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

        String commandJs = jsonObject.get("header").getAsString();
        Command commandObj = map.get(commandJs);
        if (commandObj == null) {
            throw new IllegalStateException("no command registered for " + jsonObject.get("header").getAsString());
        }

        commandObj.execute(jsonObject);
    }
}
