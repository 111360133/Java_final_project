package com.example.final_project.currencyconverter;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// ExchangeRateService 接口用於定義與匯率 API 通信的操作。使用 Retrofit 動態生成實現類，執行 HTTP 請求。
// @GET 定義了此方法執行的是 HTTP GET 請求。
// URL 模板中的 {} 表示路徑參數，將由 @Path 指定的值替換。

public interface ExchangeRateService {
    // 在 URL 中添加 API 金鑰作為查詢參數
    @GET("v6/{apiKey}/latest/{baseCurrency}")
    Call<ExchangeRateResponse> getExchangeRates(
            @Path("apiKey") String apiKey, // 用 @Path 註解將 apiKey 綁定到 URL
            @Path("baseCurrency") String baseCurrency // 用 @Path 註解將 baseCurrency 綁定到 URL
    );
}