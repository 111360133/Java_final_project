package com.example.final_project.History;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HistoryAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> historyList;

    public HistoryAdapter(Context context, List<String> historyList) {
        super(context, android.R.layout.simple_list_item_1, historyList);
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = view.findViewById(android.R.id.text1);
        String record = historyList.get(position);

        SpannableString coloredExpression = colorizeExpression(record);
        textView.setText(coloredExpression);

        return view;
    }

    // 🛠️ 根據運算優先級和括號內容著色
    private SpannableString colorizeExpression(String expression) {
        SpannableString spannable = new SpannableString(expression);

        // 🔍 檢查是否包含括號
        boolean hasBrackets = expression.contains("(") && expression.contains(")");

        if (hasBrackets) {
            // 🟣 外層括號背景顏色（淡紫色）
            applyBackgroundToBrackets(spannable, "\\([^()]*\\([^()]*\\)[^()]*\\)|\\([^()]*\\)", Color.parseColor("#D1C4E9"));

            // 🟢 內層括號背景顏色（淡綠色）
            applyBackgroundToBrackets(spannable, "\\([^()]*\\)", Color.parseColor("#C8E6C9"));

            // 🔵 括號內的運算符著色（包含小數）
            applyColorToInnerBracketOperations(spannable, "\\(([^()]*)\\)");

            // 🟠 括號前的乘除號著色
            applyColorToBeforeBracketOperators(spannable, "(\\d+(\\.\\d+)?)\\s*([*/])\\s*\\(");

            // 🟦 括號後的乘除號著色
            applyColorToAfterBracketOperators(spannable, "\\)\\s*([*/])\\s*(\\d+(\\.\\d+)?)");
        } else {
            // 🟠 沒有括號時，著色所有的乘除運算符（包含小數）
            applyColorToAllOperators(spannable, "(\\d+(\\.\\d+)?)\\s*([*/])\\s*(\\d+(\\.\\d+)?)");
        }

        return spannable;
    }

    // 🟣 設定括號及其內容的背景顏色
    private void applyBackgroundToBrackets(SpannableString spannable, String regex, int color) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(spannable);

        while (matcher.find()) {
            spannable.setSpan(
                    new BackgroundColorSpan(color),
                    matcher.start(),
                    matcher.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }

    // 🟢 括號內的運算符著色（包含小數）
    private void applyColorToInnerBracketOperations(SpannableString spannable, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(spannable);

        while (matcher.find()) {
            String content = matcher.group(1);
            int start = matcher.start(1);

            Pattern innerPattern = Pattern.compile(
                    "(?<!\\))(?<!\\^)(?<!sqrt)\\b(\\d+(\\.\\d+)?)\\s*\\*\\s*(\\d+(\\.\\d+)?)\\b(?!\\^)(?!sqrt)|" +
                            "(?<!\\))(?<!\\^)(?<!sqrt)\\b(\\d+(\\.\\d+)?)\\s*/\\s*(\\d+(\\.\\d+)?)\\b(?!\\^)(?!sqrt)"
            );
            Matcher innerMatcher = innerPattern.matcher(content);

            while (innerMatcher.find()) {
                int color = innerMatcher.group(3) != null ? Color.parseColor("#FF9800") : Color.parseColor("#2196F3");
                spannable.setSpan(
                        new ForegroundColorSpan(color),
                        start + innerMatcher.start(),
                        start + innerMatcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }
    }

    // 🔵 括號前的乘除運算符著色（包含小數）
    private void applyColorToBeforeBracketOperators(SpannableString spannable, String regex) {
        Pattern pattern = Pattern.compile(
                "(?<!\\))(?<!\\^)(?<!sqrt)\\b(\\d+(\\.\\d+)?)\\s*([*/])\\s*\\("
        );
        Matcher matcher = pattern.matcher(spannable);

        while (matcher.find()) {
            spannable.setSpan(
                    new ForegroundColorSpan(matcher.group(3).equals("*") ? Color.parseColor("#FF9800") : Color.parseColor("#2196F3")),
                    matcher.start(3),
                    matcher.end(3),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            spannable.setSpan(
                    new ForegroundColorSpan(matcher.group(3).equals("*") ? Color.parseColor("#FF9800") : Color.parseColor("#2196F3")),
                    matcher.start(1),
                    matcher.end(1),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }

    // 🟦 括號後的乘除運算符著色（包含小數）
    private void applyColorToAfterBracketOperators(SpannableString spannable, String regex) {
        Pattern pattern = Pattern.compile(
                "\\)\\s*([*/])\\s*(?<!\\^)(?<!sqrt)\\b(\\d+(\\.\\d+)?)\\b(?!\\^)(?!sqrt)"
        );
        Matcher matcher = pattern.matcher(spannable);

        while (matcher.find()) {
            spannable.setSpan(
                    new ForegroundColorSpan(matcher.group(1).equals("*") ? Color.parseColor("#FF9800") : Color.parseColor("#2196F3")),
                    matcher.start(1),
                    matcher.end(1),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            spannable.setSpan(
                    new ForegroundColorSpan(matcher.group(1).equals("*") ? Color.parseColor("#FF9800") : Color.parseColor("#2196F3")),
                    matcher.start(2),
                    matcher.end(2),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }

    // 🟠 著色所有乘除運算符（無括號，包含小數）
    private void applyColorToAllOperators(SpannableString spannable, String regex) {
        // 排除 ^ 和 sqrt 后的运算元
        Pattern pattern = Pattern.compile(
                "(?<!\\))(?<!\\^)(?<!sqrt)\\b(\\d+(\\.\\d+)?)\\s*([*/])\\s*(\\d+(\\.\\d+)?)\\b(?!\\^)(?!sqrt)"
        );
        Matcher matcher = pattern.matcher(spannable);

        while (matcher.find()) {
            String operator = matcher.group(3);
            int color = operator.equals("*") ? Color.parseColor("#FF9800") : Color.parseColor("#2196F3");

            spannable.setSpan(
                    new ForegroundColorSpan(color),
                    matcher.start(3),
                    matcher.end(3),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            spannable.setSpan(
                    new ForegroundColorSpan(color),
                    matcher.start(1),
                    matcher.end(1),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            spannable.setSpan(
                    new ForegroundColorSpan(color),
                    matcher.start(4),
                    matcher.end(4),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }




    public void removeItem(int position) {
        if (position >= 0 && position < getCount()) {
            historyList.remove(position);
        }
    }
}
