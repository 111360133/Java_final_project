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

//MainActivity5 顯示歷史紀錄，提供清除歷史紀錄和返回主畫面的功能。
public class MainActivity5 extends AppCompatActivity {

    private ListView historyListView; // 顯示歷史紀錄的 ListView
    private Button clearHistoryButton, homeButton; // 清除歷史紀錄與返回主畫面的按鈕
    private ArrayAdapter<String> adapter; // 用於綁定歷史紀錄的適配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        // 綁定 UI 元件
        historyListView = findViewById(R.id.historyListView);
        clearHistoryButton = findViewById(R.id.clearHistoryButton);
        homeButton = findViewById(R.id.homeButton);

        // 設置按鈕的點擊事件
        clearHistoryButton.setOnClickListener(v -> clearHistory());
        homeButton.setOnClickListener(v -> returnToHome());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory(); // 載入歷史紀錄
    }

    //載入歷史紀錄並顯示在 ListView 上
    private void loadHistory() {
        List<String> historyList = SharedData.getInstance().getHistoryList();

        if (historyList == null || historyList.isEmpty()) {
            historyListView.setAdapter(null);
            showToast("沒有歷史紀錄可顯示");
            return;
        }

        //提取歷史紀錄並透過時間戳記進行升序排列
        List<String> displayList = historyList.stream()
                .sorted((a, b) -> Long.compare(extractTimestamp(a), extractTimestamp(b)))
                .map(record -> record.split("#", 2))
                .filter(parts -> parts.length == 2)
                .map(parts -> parts[1])
                .collect(Collectors.toList());

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        historyListView.setAdapter(adapter);

        // ListView 點擊事件
        historyListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedRecord = adapter.getItem(position);
            new AlertDialog.Builder(this)
                    .setTitle("重新計算")
                    .setMessage("是否要將該計算式帶入計算機？")
                    .setPositiveButton("確定", (dialog, which) -> {
                        Intent intent = new Intent(MainActivity5.this, MainActivity4.class);
                        intent.putExtra("calculation_record", selectedRecord);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });
    }

    //從紀錄中提取時間戳記
    private long extractTimestamp(String record) {
        if (record == null || !record.contains("#")) {
            return 0;
        }

        try {
            String[] parts = record.split("#");
            if (parts.length > 0) {
                return Long.parseLong(parts[0]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return 0;
    }


    //清除歷史紀錄
    private void clearHistory() {
        SharedData.getInstance().clearHistory();
        adapter.notifyDataSetChanged();
        showToast("歷史紀錄已清除");
        returnToHome();
    }

    //返回主畫面
    private void returnToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    //顯示提示訊息
    private Toast toast;
    private void showToast(String message) {
        runOnUiThread(() -> {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast.show();
        });
    }

}
