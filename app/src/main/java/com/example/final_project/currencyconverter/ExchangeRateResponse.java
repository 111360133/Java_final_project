package com.example.final_project.currencyconverter;

import java.util.Map;
import com.google.gson.annotations.SerializedName;

// ExchangeRateResponse 用於映射從匯率 API 獲取的 JSON 響應，包含匯率數據和請求結果狀態。

public class ExchangeRateResponse {

    // 使用 @SerializedName 註解來匹配 JSON 中的 "conversion_rates" 鍵 (String)
    @SerializedName("conversion_rates")
    // 儲存不同貨幣及其對應的匯率
    private Map<String, Double> conversionRates;

    // 使用 @SerializedName 註解來匹配 JSON 中的 "result" 鍵
    @SerializedName("result")
    // 儲存 API 響應的結果狀態（例如 "success" 或 "error"）
    private String result;

    // 獲取特定貨幣的匯率。[currency]要查詢的貨幣代碼（例如 "USD", "EUR"），
    public Double getRate(String currency) {
        // 從 conversionRates Map 中提取指定貨幣的匯率
        return conversionRates.get(currency);
    }
}