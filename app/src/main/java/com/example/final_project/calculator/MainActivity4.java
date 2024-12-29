package com.example.final_project.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.MainActivity;
import com.example.final_project.R;
import com.example.final_project.utils.SharedData;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

// 主活動類，用於提供計算器功能
public class MainActivity4 extends AppCompatActivity {

    // 定義UI元件
    private EditText inputEditText; // 輸入數學表達式的文字框
    private TextView resultTextView; // 顯示計算結果的文字框

    // 定義按鈕
    private Button button_home;
    private Button button_plus, button_subtract, button_multiply, button_divided, button_equals,
            button_left_parentheses, button_right_parentheses, button_square, button_sqrt;
    private Button button0, button1, button2, button3, button4, button5, button6, button7,
            button8, button9, button_delete, button_dot, button_left, button_right, button_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4); // 設定對應的佈局檔案

        // 初始化 UI 元件與按鈕點擊事件
        initializeUIElements();
        setButtonClickListeners();

        // 設置等號按鈕的點擊事件
        button_equals.setOnClickListener(v -> {
            String expression = inputEditText.getText().toString().trim();

            if (expression.isEmpty()) {
                // 如果沒有輸入表達式，提示用戶
                Toast.makeText(MainActivity4.this, "請輸入數學表達式", Toast.LENGTH_SHORT).show();
                return;
            }


            // 計算表達式並獲取結果
            String resultlog = evaluateExpression();

            if ("0".equals(resultlog) || resultlog.contains("運算錯誤")) {
                // 如果計算失敗，提示用戶
                Toast.makeText(MainActivity4.this, "計算失敗，請檢查輸入", Toast.LENGTH_SHORT).show();
                return;
            }

            // 清空輸入框，顯示計算結果，並將結果保存到歷史記錄
            inputEditText.setText("");
            resultTextView.setText(resultlog);
            saveToHistory(resultlog);
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("calculation_record")) {
            String calculationRecord = intent.getStringExtra("calculation_record");

            if (calculationRecord != null && !calculationRecord.isEmpty()) {
                String inputText = calculationRecord.contains("=")
                        ? calculationRecord.split("=")[0].trim()
                        : calculationRecord.trim();
                inputEditText.setText(inputText);
            }
        }

    }

    // 初始化 UI 元件
    private void initializeUIElements() {
        inputEditText = findViewById(R.id.inputEditText);
        resultTextView = findViewById(R.id.resultTextView);

        // 初始化按鈕
        button_home = findViewById(R.id.button_home);
        button_plus = findViewById(R.id.button_plus);
        button_subtract = findViewById(R.id.button_subtract);
        button_multiply = findViewById(R.id.button_multiply);
        button_divided = findViewById(R.id.button_divided);
        button_equals = findViewById(R.id.button_equals);

        button_left_parentheses = findViewById(R.id.button_left_parentheses);
        button_right_parentheses = findViewById(R.id.button_right_parentheses);
        button_square = findViewById(R.id.button_square);
        button_sqrt = findViewById(R.id.button_sqrt);

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
        // 數字按鈕
        Button[] numberButtons = {button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, button_dot};
        String[] numberValues = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."};

        // 為每個數字按鈕設置點擊事件
        for (int i = 0; i < numberButtons.length; i++) {
            String value = numberValues[i];
            numberButtons[i].setOnClickListener(v -> updateTextView(value));
        }

        // 運算符按鈕
        Button[] operatorButtons = {button_plus, button_subtract, button_multiply, button_divided,
                button_left_parentheses, button_right_parentheses, button_square, button_sqrt};
        String[] operatorValues = {"+", "-", "×", "÷", "(", ")", "²", "√"};

        // 為每個運算符按鈕設置點擊事件
        for (int i = 0; i < operatorButtons.length; i++) {
            String operator = operatorValues[i];
            operatorButtons[i].setOnClickListener(v -> updateTextView(operator));
        }

        // 特殊功能按鈕
        button_return.setOnClickListener(v -> deleteOneCharacter()); // 刪除一個字元
        button_delete.setOnClickListener(v -> clearAllText()); // 清空輸入框
        button_left.setOnClickListener(v -> moveCursorLeft()); // 光標左移
        button_right.setOnClickListener(v -> moveCursorRight()); // 光標右移
        button_home.setOnClickListener(v -> home()); // 返回主頁
    }

    // 更新輸入框文字
    private void updateTextView(String input) {
        inputEditText.requestFocus();
        String currentText = inputEditText.getText().toString();
        int cursorPosition = inputEditText.getSelectionStart();
        String newText = currentText.substring(0, cursorPosition) + input + currentText.substring(cursorPosition);
        inputEditText.setText(newText);
        inputEditText.setSelection(cursorPosition + input.length());
    }

    // 刪除光標前一個字元
    private void deleteOneCharacter() {
        String currentText = inputEditText.getText().toString();
        int cursorPosition = inputEditText.getSelectionStart();
        if (cursorPosition > 0) {
            currentText = currentText.substring(0, cursorPosition - 1) + currentText.substring(cursorPosition);
            inputEditText.setText(currentText);
            inputEditText.setSelection(cursorPosition - 1);
        }
    }

    // 清空輸入框和結果框
    private void clearAllText() {
        inputEditText.setText("");
        resultTextView.setText("");
    }

    // 光標左移
    private void moveCursorLeft() {
        int cursorPosition = inputEditText.getSelectionStart();
        if (cursorPosition > 0) {
            inputEditText.setSelection(cursorPosition - 1);
        }
    }

    // 光標右移
    private void moveCursorRight() {
        int cursorPosition = inputEditText.getSelectionStart();
        int textLength = inputEditText.getText().length();
        if (cursorPosition < textLength) {
            inputEditText.setSelection(cursorPosition + 1);
        }
    }

    // 返回主頁
    private void home() {
        Intent intent = new Intent(MainActivity4.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 保存計算結果到歷史記錄
    private void saveToHistory(String result) {
        long timestamp = System.currentTimeMillis(); // 獲取當前時間戳
        String historyEntry = timestamp + "#" + result; // 記錄格式：時間戳#結果
        SharedData.getInstance().getHistoryList().add(historyEntry); // 添加到共享資料中
        Toast.makeText(this, "結果已保存到歷史記錄", Toast.LENGTH_SHORT).show();
    }

    // 評估數學表達式
    private String evaluateExpression() {
        try {
            //獲取輸入數據
            String inputdata = inputEditText.getText().toString().trim();

            //修正連續小數點等異常輸入
            String expression = FixDoubleOperators(inputdata);

            //將 StringBuilder 轉換為 String
            expression = expression.replace("×", "*").replace("÷", "/")
                    .replace("²", "^2").replace("√", "sqrt");

            //使用 exp4j 計算結果
            double result = new ExpressionBuilder(expression).build().evaluate();

            //返回計算結果
            return expression + "=" + result;

        } catch (Exception e) {
            // 捕捉異常並顯示錯誤消息
            Toast.makeText(this, "運算錯誤", Toast.LENGTH_SHORT).show();
            return "0";
        }
    }
    private String FixDoubleOperators(String beforefix) {
        StringBuilder result = new StringBuilder();
        char lastType = '\0'; // 用於記錄上一個字元的類型

        for (char currentChar : beforefix.toCharArray()) {
            if ("+-×÷".indexOf(currentChar) != -1) {
                // 當前字元是運算符
                if (lastType != 'O') {
                    result.append(currentChar);
                    lastType = 'O'; // 設定為運算符類型
                }
            } else if (currentChar == '.') {
                // 當前字元是小數點
                if (lastType != 'D') {
                    result.append(currentChar);
                    lastType = 'D'; // 設定為小數點類型
                }
            } else {
                // 其他字元
                result.append(currentChar);
                lastType = 'X'; // 設定為其他字元類型
            }
        }

        return result.toString();
    }
}
