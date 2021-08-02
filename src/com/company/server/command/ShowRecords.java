package com.company.server.command;

import com.company.DataTransfer;
import com.company.Main;
import com.google.gson.JsonObject;

import java.io.IOException;

public class ShowRecords implements Command {
    private DataTransfer dataTransfer;

    public ShowRecords(DataTransfer dataTransfer) {
        this.dataTransfer = dataTransfer;
    }

    @Override
    public void execute(JsonObject json) {
        try {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("showRecords", Main.dataBase.getRecorde());
            dataTransfer.sendMessage(jsonObj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
