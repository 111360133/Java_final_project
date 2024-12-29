package com.example.final_project.currencyconverter;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// ExchangeRateManager 負責管理與匯率 API 的交互，並提供貨幣轉換的功能，包含 API 請求處理及匯率計算邏輯。

public class ExchangeRateManager {

    // API 的基礎 URL
    private static final String BASE_URL = "https://v6.exchangerate-api.com/";

    // 定義服務接口
    private ExchangeRateService service;

    // 初始化 Retrofit 並建立 ExchangeRateService 實例
    public ExchangeRateManager() {
        // 使用 Retrofit 建立實例，配置基礎 URL 和 JSON 解析器
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // // 設置 API 的基礎 URL
                .addConverterFactory(GsonConverterFactory.create()) // 使用 Gson 解析 API 回傳的 JSON 資料
                .build();

        // 使用 Retrofit 建立服務接口的實現
        service = retrofit.create(ExchangeRateService.class);
    }

    // 向 API 發送請求以獲取匯率資訊。
    // [apiKey] API 金鑰，用於身份驗證，[baseCurrency] 基礎貨幣 (如 "USD")，[callback] 回調接口，用於處理請求結果。
    public void getExchangeRates(String apiKey, String baseCurrency, ExchangeRateCallback callback) {
        // 調用 service 中定義的 getExchangeRates 方法，傳入 API 金鑰和基礎貨幣
        Call<ExchangeRateResponse> call = service.getExchangeRates(apiKey, baseCurrency);

        // 使用 enqueue 方法執行非同步請求
        call.enqueue(new Callback<ExchangeRateResponse>() {
            @Override
            // 處理 API 響應，檢查是否成功
            public void onResponse(Call<ExchangeRateResponse> call, Response<ExchangeRateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 如果成功，通過回調接口返回響應數據
                    callback.onSuccess(response.body());
                } else {
                    // 如果失敗，通過回調接口返回錯誤信息
                    callback.onError("Error fetching exchange rates");
                }
            }

            @Override
            public void onFailure(Call<ExchangeRateResponse> call, Throwable t) {
                // 處理請求失敗，例如網絡錯誤
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // 回調接口，用於處理 API 響應結果
    public interface ExchangeRateCallback {
        // 當 API 響應成功時執行的回調方法。
        void onSuccess(ExchangeRateResponse response);
        // 當 API 請求失敗或出現錯誤時執行的回調方法。
        void onError(String errorMessage);
    }

    // 貨幣轉換方法
    public double convertCurrency(double amount, double fromRate, double toRate) {
        // 計算公式：要轉換的金額 * (目標貨幣匯率 / 起始貨幣匯率)，return 轉換後的金額。
        return amount * (toRate / fromRate);
    }
}
