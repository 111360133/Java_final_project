package com.example.final_project.History;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.MainActivity;
import com.example.final_project.R;
import com.example.final_project.calculator.MainActivity4;
import com.example.final_project.utils.SharedData;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity5 extends AppCompatActivity {

    private ListView historyListView;
    private Button clearHistoryButton, homeButton;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        historyListView = findViewById(R.id.historyListView);
        clearHistoryButton = findViewById(R.id.clearHistoryButton);
        homeButton = findViewById(R.id.homeButton);

        clearHistoryButton.setOnClickListener(v -> clearHistory());
        homeButton.setOnClickListener(v -> returnToHome());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        List<String> historyList = SharedData.getInstance().getHistoryList();

        if (historyList == null || historyList.isEmpty()) {
            historyListView.setAdapter(null); // 如果沒有資料，清空 ListView
            showToast("沒有歷史紀錄可顯示");
            return;
        }

        // 排序並提取結果部分
        List<String> displayList = historyList.stream()
                .sorted((a, b) -> Long.compare(extractTimestamp(a), extractTimestamp(b)))
                .map(record -> record.split("#", 2))
                .filter(parts -> parts.length == 2)
                .map(parts -> parts[1])
                .collect(Collectors.toList());

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        historyListView.setAdapter(adapter); // 設置新的適配器

        // 添加 ListView 點擊事件
        historyListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedRecord = adapter.getItem(position); // 取得被點擊的歷史紀錄

            // 顯示確認對話框
            new AlertDialog.Builder(this)
                    .setTitle("重新計算")
                    .setMessage("是否要將該計算式帶入計算機？")
                    .setPositiveButton("確定", (dialog, which) -> {
                        Intent intent = new Intent(MainActivity5.this, MainActivity4.class);
                        intent.putExtra("calculation_record", selectedRecord);
                        startActivity(intent);
                        finish(); // 結束 MainActivity5
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });
    }

    private long extractTimestamp(String record) {
        try {
            return Long.parseLong(record.split("#")[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void clearHistory() {
        SharedData.getInstance().clearHistory();
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "歷史紀錄已清除", Toast.LENGTH_SHORT).show();
        returnToHome();
    }

    private void returnToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
