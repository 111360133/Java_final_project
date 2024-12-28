package com.example.final_project.currencyconverter;

import android.content.Context;

import com.example.final_project.R;

// ApiKeyReader 負責讀取匯率轉換的 API，用於存取保存在 res/values/strings.xml 中的安全字串(API)。

public class ApiKeyReader {

    public static String getApiKey(Context context) {
        // 使用 Context 的 getString 方法來讀取 R.string.api_key 的值
        return context.getString(R.string.api_key);
    }
}