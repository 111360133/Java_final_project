package com.example.final_project.utils;

import java.util.ArrayList;
import java.util.List;

public class SharedData {
    private static SharedData instance;
    private List<String> historyList = new ArrayList<>();

    private SharedData() {}

    public static SharedData getInstance() {
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    public List<String> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<String> historyList) {
        this.historyList = historyList;
    }

    public void clearHistory() {
        historyList.clear();
    }
}
