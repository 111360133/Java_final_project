package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.final_project.currencyconverter.MainActivity2;
import com.example.final_project.UnitConverter.MainActivity3;
import com.example.final_project.calculator.MainActivity4;
//import com.example.final_project.History.MainActivity5;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private Button generalButton, unitConversionButton, exchangeRateButton, historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        text = findViewById(R.id.text); // 連接 TextView 元件
        generalButton = findViewById(R.id.generalButton); // 連接 Button 元件
        unitConversionButton = findViewById(R.id.unitConversionButton); // 連接 Button 元件
        exchangeRateButton = findViewById(R.id.exchangeRateButton); // 連接 Button 元件
        historyButton = findViewById(R.id.historyButton); // 連接 Button 元件

        generalButton.setOnClickListener(view ->
                startActivityForResult(new Intent(MainActivity.this, MainActivity4.class), 1)
        );

        exchangeRateButton.setOnClickListener(view ->
                startActivityForResult(new Intent(MainActivity.this, MainActivity2.class), 1)
        );

        unitConversionButton.setOnClickListener(view ->
                startActivityForResult(new Intent(MainActivity.this, MainActivity3.class), 1)
        );

       /* historyButton.setOnClickListener(view ->
                startActivityForResult(new Intent(MainActivity.this, MainActivity5.class), 1)
        );*/

    }
}