package com.example.final_project.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * SharedData 類是一個單例類，負責在應用程式中共享歷史紀錄數據。
 * 這種設計模式確保只存在一個 SharedData 實例，所有的類都可以存取相同的數據。
 */
public class SharedData {
    // 靜態變量，用於存儲唯一實例
    private static SharedData instance;

    // 用於存儲歷史紀錄的列表
    private List<String> historyList = new ArrayList<>();

    /**
     * 私有構造函數，防止外部直接實例化該類。
     */
    private SharedData() {}

    /**
     * 提供一個靜態方法來獲取唯一的 SharedData 實例。
     * 如果實例不存在，則創建一個新的實例。
     * @return SharedData 的單例實例
     */
    public static SharedData getInstance() {
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    /**
     * 獲取歷史紀錄列表。
     * @return 包含歷史紀錄的 List
     */
    public List<String> getHistoryList() {
        return historyList;
    }

    /**
     * 設定歷史紀錄列表。
     * @param historyList 要設置的新歷史紀錄列表
     */
    public void setHistoryList(List<String> historyList) {
        this.historyList = historyList;
    }

    /**
     * 清空歷史紀錄列表。
     */
    public void clearHistory() {
        historyList.clear();
    }
}
