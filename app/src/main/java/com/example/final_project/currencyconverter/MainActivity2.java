package com.example.final_project.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.final_project.MainActivity;
import com.example.final_project.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private ExchangeRateManager exchangeRateManager;
    private Spinner fromCurrencySpinner, toCurrencySpinner;
    private EditText amountEditText;
    private TextView resultTextView;
    private Button convertButton, button_home;
    private Button button0, button1, button2, button3, button4, button5, button6,button7,
            button8, button9, button_delete, button_dot,button_left, button_right, button_return;
    private String fromCurrency = "USD";
    private String toCurrency = "TWD";
    private ExchangeRateResponse currentRates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // 初始化UI元件
        initializeUIElements();

        // 設定按鈕點擊事件
        setButtonClickListeners();

        // 初始化匯率管理器
        exchangeRateManager = new ExchangeRateManager();

        // 設置貨幣選擇器
        setupCurrencySpinners();

        // 初始載入匯率
        fetchExchangeRates();
    }

    // 初始化UI元件
    private void initializeUIElements() {
        fromCurrencySpinner = findViewById(R.id.fromCurrencySpinner);
        toCurrencySpinner = findViewById(R.id.toCurrencySpinner);
        amountEditText = findViewById(R.id.amountEditText);
        convertButton = findViewById(R.id.convertButton);
        resultTextView = findViewById(R.id.resultTextView);

        button_home = findViewById(R.id.button_home);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        button_dot = findViewById(R.id.button_dot);
        button_return = findViewById(R.id.button_return);
        button_left = findViewById(R.id.button_left);
        button_right = findViewById(R.id.button_right);
        button_delete = findViewById(R.id.button_delete);
    }

    // 設置按鈕的 OnClickListener
    private void setButtonClickListeners() {
        // 數字按鈕 (0-9) 與點號
        // 定義數字按鈕的陣列，對應於 XML 中的按鈕元件
        Button[] numberButtons = {button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, button_dot};
        // 定義每個按鈕對應的輸入值 (數字或小數點)
        String[] numberValues = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."};

        // 為每個數字按鈕設定點擊事件
        for (int i = 0; i < numberButtons.length; i++) {
            String value = numberValues[i]; // 取得對應的值
            numberButtons[i].setOnClickListener(v -> updateTextView(value)); // 將按下的數字或小數點插入到輸入框中
        }

        /* 特殊按鈕 */
        // 刪除單一字元按鈕
        button_return.setOnClickListener(v -> deleteOneCharacter());
        // 刪除全部字元按鈕
        button_delete.setOnClickListener(v -> clearAllText());
        // 左移游標按鈕
        button_left.setOnClickListener(v -> moveCursorLeft());
        // 右移游標按鈕
        button_right.setOnClickListener(v -> moveCursorRight());
        // 返回主畫面按鈕
        button_home.setOnClickListener(v -> home());
        // 轉換按鈕
        convertButton.setOnClickListener(v -> performConversion());
    }

    // 返回主畫面
    private void home(){
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent); // 啟動主畫面 Activity
        finish(); // 結束當前畫面 (MainActivity2)，防止返回時仍顯示該畫面
    }

    // 更新 EditText 中的字元
    private void updateTextView(String input) {
        // 確保游標顯示在 EditText 中
        amountEditText.requestFocus();

        // 取得當前文字和游標位置
        String currentText = amountEditText.getText().toString();
        int cursorPosition = amountEditText.getSelectionStart();

        // 在游標所在位置插入新的字元
        String newText = currentText.substring(0, cursorPosition) + input + currentText.substring(cursorPosition);
        amountEditText.setText(newText); // 更新 EditText 的內容

        // 設置游標位置到新插入字元的後方，確保使用者能繼續輸入
        amountEditText.setSelection(cursorPosition + input.length());
    }

    // 刪除單一字元
    private void deleteOneCharacter() {
        // 取得目前的內容
        String currentText = amountEditText.getText().toString();

        // 取得游標位置
        int cursorPosition = amountEditText.getSelectionStart();

        // 確保游標不在最前面
        if (cursorPosition > 0) {
            // 刪除游標前一個字元，並更新文字
            currentText = currentText.substring(0, cursorPosition - 1) + currentText.substring(cursorPosition);

            // 設定更新後的內容至 EditText
            amountEditText.setText(currentText);

            // 設置游標停留在刪除後的位置 (即游標往前移動一格)
            amountEditText.setSelection(cursorPosition - 1);
        }
    }

    // 刪除全部字元
    private void clearAllText() {
        // 清空 EditText 的內容
        amountEditText.setText("");
    }

    // 左移游標
    private void moveCursorLeft() {
        // 取得目前游標位置
        int cursorPosition = amountEditText.getSelectionStart();

        // 確保游標不會超出左邊界 (游標不能小於 0)
        if (cursorPosition > 0) {
            amountEditText.setSelection(cursorPosition - 1); // 將游標向左移動
        }
    }

    // 右移游標
    private void moveCursorRight() {
        // 取得目前游標位置
        int cursorPosition = amountEditText.getSelectionStart();

        //  // 取得 EditText 中文字的總長度
        int textLength = amountEditText.getText().length();

        // 確保游標不會超出右邊界 (游標不能大於文字長度)
        if (cursorPosition < textLength) {
            amountEditText.setSelection(cursorPosition + 1); // 將游標向右移動
        }
    }

    // 設置貨幣選擇器 (Spinner)
    private void setupCurrencySpinners() {
        // 義貨幣列表，包含貨幣代碼與貨幣名稱
        List<String> currencies = new ArrayList<>();
        currencies.add("USD(美元)");
        currencies.add("TWD(台幣)");
        currencies.add("EUR(歐元)");
        currencies.add("JPY(日元)");
        currencies.add("GBP(英鎊)");
        currencies.add("CNY(人民幣)");
        currencies.add("HKD(港幣)");
        currencies.add("KRW(韓元)");
        currencies.add("AUD(澳元)");
        currencies.add("NZD(紐西蘭元)");
        currencies.add("SGD(新加坡幣)");
        currencies.add("IDR(印尼盾)");

        // 創建 ArrayAdapter 並設置貨幣列表為選項
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                currencies
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 設置 Spinner 的適配器
        fromCurrencySpinner.setAdapter(adapter);
        toCurrencySpinner.setAdapter(adapter);

        // 設定 Spinner 的預設選擇項
        fromCurrencySpinner.setSelection(0); // "USD(美元)"
        toCurrencySpinner.setSelection(1); // "TWD(台幣)"

        // 設置 "fromCurrencySpinner" 的選擇監聽器
        fromCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 當選擇變更時，取得貨幣代碼 (例如 "USD")
                // split("\\(") 用來從字符串中提取貨幣代碼部分
                fromCurrency = currencies.get(position).split("\\(")[0];  // 只選取貨幣代碼
                fetchExchangeRates();  // 呼叫方法獲取匯率資料
            }

            //使用者未選擇任何項目時被觸發的回調方法
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 設置 "toCurrencySpinner" 的選擇監聽器
        toCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 當選擇變更時，取得貨幣代碼 (例如 "TWD")
                // split("\\(") 用來從字符串中提取貨幣代碼部分
                toCurrency = currencies.get(position).split("\\(")[0];
            }

            //使用者未選擇任何項目時被觸發的回調方法
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // 從 API 獲取匯率
    private void fetchExchangeRates() {
        // 從 ApiKeyReader 讀取 API 金鑰
        String apiKey = ApiKeyReader.getApiKey(this);

        // 如果 API 金鑰無效，顯示錯誤提示
        if (apiKey == null) {
            Toast.makeText(this, "API金鑰無效", Toast.LENGTH_SHORT).show();
            return;
        }

        // 使用 ExchangeRateManager 獲取匯率，並設置回調方法處理成功或錯誤的情況
        exchangeRateManager.getExchangeRates(apiKey, fromCurrency, new ExchangeRateManager.ExchangeRateCallback() {
            // 匯率獲取成功時的處理
            @Override
            public void onSuccess(ExchangeRateResponse response) {
                // 設定當前匯率
                currentRates = response;
            }

            // 匯率獲取失敗時的處理
            @Override
            public void onError(String errorMessage) {
                // 顯示錯誤提示，Toast.LENGTH_SHORT為短時間顯示，大約為 2 秒。
                // errorMessage 設定在 ExchangeRateManager
                Toast.makeText(MainActivity2.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 執行貨幣換算
    private void performConversion() {
        // 取得用戶輸入的金額並移除多餘空格
        String amountStr = amountEditText.getText().toString().trim();

        //  如果當前匯率尚未獲取，顯示提示並退出
        if (currentRates == null) {
            Toast.makeText(this, "尚未載入匯率，請稍後", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 嘗試將金額字符串轉換為 Double 型別
            double amount = Double.parseDouble(amountStr);

            // 根據來源和目標貨幣代碼獲取相應的匯率，使用 ExchangeRateResponse 的 currentRates.getRate 方法
            double fromRate = currentRates.getRate(fromCurrency);
            double toRate = currentRates.getRate(toCurrency);

            // 使用 exchangeRateManager 的 convertCurrency 方法進行貨幣換算
            double convertedAmount = exchangeRateManager.convertCurrency(amount, fromRate, toRate);

            String formattedAmount = formatNumber(amount);
            String formattedConvertedAmount = formatNumber(convertedAmount);

            // 顯示格式化後的結果
            String resultText = String.format("%s %s = %s %s", formattedAmount, fromCurrency, formattedConvertedAmount, toCurrency);
            resultTextView.setText(resultText);

        } catch (NumberFormatException e) {
            // 捕捉數字格式異常（例如用戶輸入的金額無效）
            Toast.makeText(this, "無效的金額", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            // 捕捉匯率為 null 的情況，表示無法找到匯率
            Toast.makeText(this, "無法找到匯率", Toast.LENGTH_SHORT).show();
        }
    }

    // 格式化數字方法，包含科學記數法與小數位限制
    private String formatNumber(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.######"); // 最多顯示 6 位小數
        String result = decimalFormat.format(value);

        // 如果數值過大或過小，使用科學記數法
        if (result.length() > 15 || (result.contains(".") && result.substring(result.indexOf(".")).length() > 10)) {
            DecimalFormat scientificFormat = new DecimalFormat("0.######E0");
            return scientificFormat.format(value);
        }
        return result;
    }
}