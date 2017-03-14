package com.example.zhuwojia.multipicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String[] titles = {"卧室", "客厅", "卫浴", "厨房", "书房", "阳台"};
    private static final String[] PLANETS = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    MyWheelView my_wheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_door_type_show);
        my_wheel = (MyWheelView) findViewById(R.id.my_wheel);
        my_wheel.setTitleLayout(this, titles);
        my_wheel.setWheelTitle(this, PLANETS);
        my_wheel.setOnWheelViewListener(new MyWheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int titleIndex, String titleItem, int selectedIndex, String item) {
                super.onSelected(titleIndex, titleItem, selectedIndex, item);
                Toast.makeText(MainActivity.this, titleIndex + titleItem + selectedIndex + item, Toast.LENGTH_SHORT).show();
            }
        });
        my_wheel.setOnCancleClickListener(new MyWheelView.OnClickListener() {
            @Override
            public void onClickListener() {
                Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }
        });
        my_wheel.setOnCommitListener(new MyWheelView.OnClickListener() {
            @Override
            public void onClickListener() {
                Toast.makeText(MainActivity.this, "确定", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
