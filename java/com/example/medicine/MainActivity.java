package com.example.medicine;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase sqlDB;
    MyDBHelper myHelper;
    RelativeLayout rl_select, rl_calendar;
    RecyclerView pill_list;
    PillListRecyclerViewAdapter PillListRecyclerViewAdapter;
    //com.example.medicine.PillListRecyclerViewAdapter PillListRecyclerViewAdapter;
    ArrayList<PillList> pillList = new ArrayList<>();
    BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);

        rl_select = (RelativeLayout) findViewById(R.id.rl_select);
        rl_calendar = (RelativeLayout) findViewById(R.id.rl_calendar);

        rl_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectingActivity.class);
                startActivity(intent);
            }
        });

        rl_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        ImageView imgSta = (ImageView) findViewById(R.id.img_statistics);
        imgSta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(intent);
            }
        });

        myHelper = new MyDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();

        pillList.addAll(myHelper.allPListItems());

        pill_list = (RecyclerView) findViewById(R.id.pill_list);
        PillListRecyclerViewAdapter = new PillListRecyclerViewAdapter(pillList, this);
        pill_list.setAdapter(PillListRecyclerViewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pill_list.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
