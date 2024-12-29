package com.example.final_project.UnitConverter;

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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;


public class MainActivity3 extends AppCompatActivity {
    private EditText inputEditText;
    private TextView resultTextView;
    private Spinner categorySpinner;
    private Spinner fromUnitSpinner;
    private Spinner toUnitSpinner;
    private Button convertButton, button_home;
    private Button button0, button1, button2, button3, button4, button5, button6,button7,
            button8, button9, button_delete, button_dot,button_left, button_right, button_return;

    // 單位類別
    private final String[] unitCategories = {"長度", "質量", "溫度", "能量", "面積", "體積", "角度"};

    // 長度單位
    private final String[] lengthUnits = {"公尺(m)", "公里(km)", "公分(cm)", "毫米(mm)", "英吋(in)", "英尺(ft)", "碼(yd)"};

    // 重量單位
    private final String[] weightUnits = {"公斤(kg)", "公噸(ton)", "克(g)", "磅(lb)", "盎司(oz)"};

    // 溫度單位
    private final String[] temperatureUnits = {"攝氏(℃)", "華氏(℉)", "克氏(K)"};

    // 能量單位
    private final String[] energyUnits = {"千焦(kJ)", "千卡(kcal)", "英熱單位(BTU)", "千瓦時(kWh)", "馬力時(hp·h)"};

    // 面積單位
    private final String[] areaUnits = {"平方公尺(m²)", "平方公分(cm²)", "平方公釐(mm²)", "平方碼(yd²)", "平方英尺(ft²)", "平方英吋(in²)"};

    // 體積單位
    private final String[] volumeUnits = {"立方公尺(m³)", "公秉(bbl)", "立方碼(yd³)", "立方英吋(in³)", "公升(L)", "加侖(gal)"};

    // 角度單位
    private final String[] angleUnits = {"度(°)", "弧度(rad)", "弧分(′)", "弧秒(″)"};

    // 轉換率（使用 BigDecimal）
    private final BigDecimal[] lengthConversionRates = {
            BigDecimal.ONE,
            new BigDecimal("1000"),
            new BigDecimal("0.01"),
            new BigDecimal("0.001"),
            new BigDecimal("0.0254"),
            new BigDecimal("0.3048"),
            new BigDecimal("0.9144")
    };

    private final BigDecimal[] weightConversionRates = {
            BigDecimal.ONE,
            new BigDecimal("1000"),
            new BigDecimal("0.001"),
            new BigDecimal("0.45359237"),
            new BigDecimal("0.0283495")
    };

    private final BigDecimal[] energyConversionRates = {
            BigDecimal.ONE,
            new BigDecimal("0.239005736"),
            new BigDecimal("0.947817120"),
            new BigDecimal("3.6"),
            new BigDecimal("2.684519537")
    };

    private final BigDecimal[] areaConversionRates = {
            BigDecimal.ONE,
            new BigDecimal("0.0001"),
            new BigDecimal("0.000001"),
            new BigDecimal("0.836127360"),
            new BigDecimal("0.092903040"),
            new BigDecimal("0.000645160")
    };

    private final BigDecimal[] volumeConversionRates = {
            BigDecimal.ONE,
            new BigDecimal("0.158987294928"),
            new BigDecimal("0.764554857984"),
            new BigDecimal("0.000016387064"),
            new BigDecimal("0.001"),
            new BigDecimal("0.003785411784")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // 初始化UI元件
        initializeUIElements();

        // 設定按鈕點擊事件
        setButtonClickListeners();

        // 設置類別下拉選單
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 將 categoryAdapter 設定為 categorySpinner 的資料來源
        categorySpinner.setAdapter(categoryAdapter);

        // 類別選擇監聽器，當使用者選擇不同的單位類別時，更新單位選擇器
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 使用者有做選擇時
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinners(position); // 更新單位選擇器的選項
            }

            // 使用者沒有做選擇時
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 預設初始化，設定初始的單位選項
        updateUnitSpinners(0);  // 根據索引 0（預設選項）更新單位選擇器
    }

    // 初始化UI元件
    private void initializeUIElements() {
        inputEditText = findViewById(R.id.inputEditText);
        resultTextView = findViewById(R.id.resultTextView);
        categorySpinner = findViewById(R.id.categorySpinner);
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner);
        toUnitSpinner = findViewById(R.id.toUnitSpinner);
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
        convertButton.setOnClickListener(v -> convertUnits());
    }

    // 返回主畫面
    private void home(){
        Intent intent = new Intent(MainActivity3.this, MainActivity.class);
        startActivity(intent); // 啟動主畫面 Activity
        finish(); // 結束當前畫面 (MainActivity2)，防止返回時仍顯示該畫面
    }

    // 更新 EditText 中的字元
    private void updateTextView(String input) {
        // 確保游標顯示在 EditText 中
        inputEditText.requestFocus();

        // 取得當前文字和游標位置
        String currentText = inputEditText.getText().toString();
        int cursorPosition = inputEditText.getSelectionStart();

        // 在游標所在位置插入新的字元
        String newText = currentText.substring(0, cursorPosition) + input + currentText.substring(cursorPosition);
        inputEditText.setText(newText); // 更新 EditText 的內容

        // 設置游標位置到新插入字元的後方，確保使用者能繼續輸入
        inputEditText.setSelection(cursorPosition + input.length());
    }

    // 刪除單一字元
    private void deleteOneCharacter() {
        // 取得目前的內容
        String currentText = inputEditText.getText().toString();

        // 取得游標位置
        int cursorPosition = inputEditText.getSelectionStart();

        // 確保游標不在最前面
        if (cursorPosition > 0) {
            // 刪除游標前一個字元，並更新文字
            currentText = currentText.substring(0, cursorPosition - 1) + currentText.substring(cursorPosition);

            // 設定更新後的內容至 EditText
            inputEditText.setText(currentText);

            // 設置游標停留在刪除後的位置 (即游標往前移動一格)
            inputEditText.setSelection(cursorPosition - 1);
        }
    }

    // 刪除全部字元
    private void clearAllText() {
        // 清空 EditText 的內容
        inputEditText.setText("");
    }

    // 左移游標
    private void moveCursorLeft() {
        // 取得目前游標位置
        int cursorPosition = inputEditText.getSelectionStart();

        // 確保游標不會超出左邊界 (游標不能小於 0)
        if (cursorPosition > 0) {
            inputEditText.setSelection(cursorPosition - 1); // 將游標向左移動
        }
    }

    // 右移游標
    private void moveCursorRight() {
        // 取得目前游標位置
        int cursorPosition = inputEditText.getSelectionStart();

        //  // 取得 EditText 中文字的總長度
        int textLength = inputEditText.getText().length();

        // 確保游標不會超出右邊界 (游標不能大於文字長度)
        if (cursorPosition < textLength) {
            inputEditText.setSelection(cursorPosition + 1); // 將游標向右移動
        }
    }

    // 根據選擇的類別更新單位下拉選單
    private void updateUnitSpinners(int categoryPosition) {
        ArrayAdapter<String> fromUnitAdapter;
        ArrayAdapter<String> toUnitAdapter;

        switch (categoryPosition) {
            case 0: // 長度
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lengthUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lengthUnits);
                break;
            case 1: // 質量
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weightUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weightUnits);
                break;
            case 2: // 溫度
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, temperatureUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, temperatureUnits);
                break;
            case 3: // 能量
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, energyUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, energyUnits);
                break;
            case 4: // 面積
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, areaUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, areaUnits);
                break;
            case 5: // 體積
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, volumeUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, volumeUnits);
                break;
            case 6: // 角度
                fromUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, angleUnits);
                toUnitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, angleUnits);
                break;
            default:
                return;
        }

        fromUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromUnitSpinner.setAdapter(fromUnitAdapter);
        toUnitSpinner.setAdapter(toUnitAdapter);
    }

    // 單位轉換邏輯
    private void convertUnits() {
        String inputText = inputEditText.getText().toString();
        if (inputText.isEmpty()) {
            Toast.makeText(this, "請輸入數值", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal inputValue;
        try {
            inputValue = new BigDecimal(inputText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "請輸入有效的數值", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryPosition = categorySpinner.getSelectedItemPosition();
        int fromUnitPosition = fromUnitSpinner.getSelectedItemPosition();
        int toUnitPosition = toUnitSpinner.getSelectedItemPosition();

        BigDecimal result;

        switch (categoryPosition) {
            case 0: // 長度轉換
                result = convertLength(inputValue, fromUnitPosition, toUnitPosition);
                break;
            case 1: // 重量轉換
                result = convertWeight(inputValue, fromUnitPosition, toUnitPosition);
                break;
            case 2: // 溫度轉換
                result = convertTemperature(inputValue, fromUnitPosition, toUnitPosition);
                break;
            case 3: // 能量轉換
                result = convertEnergy(inputValue, fromUnitPosition, toUnitPosition);
                break;
            case 4: // 面積轉換
                result = convertArea(inputValue, fromUnitPosition, toUnitPosition);
                break;
            case 5: // 體積轉換
                result = convertVolume(inputValue, fromUnitPosition, toUnitPosition);
                break;
            case 6: // 角度轉換
                result = convertAngle(inputValue, fromUnitPosition, toUnitPosition);
                break;
            default:
                return;
        }

        // 使用動態格式化顯示結果
        String formattedResult = formatResult(result);
        resultTextView.setText(formattedResult);
    }

    // 格式化結果，去除不必要的零
    private String formatResult(BigDecimal value) {
        // 移除尾隨的零，如果是整數則不顯示小數點
        String result = value.stripTrailingZeros().toPlainString();

        // 如果數值過大或過小，使用科學記數法
        if (result.length() > 15 || (result.contains(".") && result.substring(result.indexOf(".")).length() > 10)) {
            DecimalFormat scientificFormat = new DecimalFormat("0.######E0");
            return scientificFormat.format(value);
        }

        return result;
    }

    // 長度轉換
    private BigDecimal convertLength(BigDecimal value, int fromUnit, int toUnit) {
        if (fromUnit == toUnit) return value;
        return value.multiply(lengthConversionRates[fromUnit])
                .divide(lengthConversionRates[toUnit], 15, RoundingMode.HALF_UP);
    }

    // 質量轉換
    private BigDecimal convertWeight(BigDecimal value, int fromUnit, int toUnit) {
        if (fromUnit == toUnit) return value;
        return value.multiply(weightConversionRates[fromUnit])
                .divide(weightConversionRates[toUnit], 15, RoundingMode.HALF_UP);
    }

    // 溫度轉換
    private BigDecimal convertTemperature(BigDecimal value, int fromUnit, int toUnit) {
        if (fromUnit == toUnit) return value;

        // 先轉換成攝氏
        BigDecimal celsius;
        switch (fromUnit) {
            case 0: // 攝氏
                celsius = value;
                break;
            case 1: // 華氏
                celsius = value.subtract(new BigDecimal("32"))
                        .multiply(new BigDecimal("5"))
                        .divide(new BigDecimal("9"), 15, RoundingMode.HALF_UP);
                break;
            case 2: // 克氏
                celsius = value.subtract(new BigDecimal("273.15"));
                break;
            default:
                return value;
        }

        // 從攝氏轉換到目標單位
        switch (toUnit) {
            case 0: // 攝氏
                return celsius;
            case 1: // 華氏
                return celsius.multiply(new BigDecimal("9"))
                        .divide(new BigDecimal("5"), 15, RoundingMode.HALF_UP)
                        .add(new BigDecimal("32"));
            case 2: // 克氏
                return celsius.add(new BigDecimal("273.15"));
            default:
                return value;
        }
    }

    // 能量轉換
    private BigDecimal convertEnergy(BigDecimal value, int fromUnit, int toUnit) {
        if (fromUnit == toUnit) return value;
        return value.multiply(energyConversionRates[fromUnit])
                .divide(energyConversionRates[toUnit], 15, RoundingMode.HALF_UP);
    }

    // 面積轉換
    private BigDecimal convertArea(BigDecimal value, int fromUnit, int toUnit) {
        if (fromUnit == toUnit) return value;
        return value.multiply(areaConversionRates[fromUnit])
                .divide(areaConversionRates[toUnit], 15, RoundingMode.HALF_UP);
    }

    // 體積轉換
    private BigDecimal convertVolume(BigDecimal value, int fromUnit, int toUnit) {
        if (fromUnit == toUnit) return value;
        return value.multiply(volumeConversionRates[fromUnit])
                .divide(volumeConversionRates[toUnit], 15, RoundingMode.HALF_UP);
    }

    // 角度轉換
    private BigDecimal convertAngle(BigDecimal value, int fromUnit, int toUnit) {
        if (fromUnit == toUnit) return value;

        // 常數定義
        final BigDecimal PI = new BigDecimal("3.141592653589793");
        final BigDecimal DEG_TO_RAD = PI.divide(new BigDecimal("180"), 15, RoundingMode.HALF_UP);
        final BigDecimal ARCMIN_TO_RAD = PI.divide(new BigDecimal("10800"), 15, RoundingMode.HALF_UP);
        final BigDecimal ARCSEC_TO_RAD = PI.divide(new BigDecimal("648000"), 15, RoundingMode.HALF_UP);

        // 先轉換成弧度
        BigDecimal radians;
        switch (fromUnit) {
            case 0: // 度
                radians = value.multiply(DEG_TO_RAD);
                break;
            case 1: // 弧度
                radians = value;
                break;
            case 2: // 弧分
                radians = value.multiply(ARCMIN_TO_RAD);
                break;
            case 3: // 弧秒
                radians = value.multiply(ARCSEC_TO_RAD);
                break;
            default:
                return value;
        }

        // 從弧度轉換到目標單位
        switch (toUnit) {
            case 0: // 度
                return radians.divide(DEG_TO_RAD, 15, RoundingMode.HALF_UP);
            case 1: // 弧度
                return radians;
            case 2: // 弧分
                return radians.divide(ARCMIN_TO_RAD, 15, RoundingMode.HALF_UP);
            case 3: // 弧秒
                return radians.divide(ARCSEC_TO_RAD, 15, RoundingMode.HALF_UP);
            default:
                return value;
        }
    }
}



