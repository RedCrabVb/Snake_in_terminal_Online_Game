package com.company.server.command;

import com.company.DataTransfer;
import com.google.gson.JsonObject;

import java.io.IOException;

public class GetFrame implements Command {
    private String frame;
    private DataTransfer dataTransfer;

    public GetFrame(DataTransfer dataTransfer) {
        this.dataTransfer = dataTransfer;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    @Override
    public void execute(JsonObject json) {
        try {
            dataTransfer.sendMessage(frame);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
