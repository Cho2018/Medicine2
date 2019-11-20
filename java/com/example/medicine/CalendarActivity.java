package com.example.medicine;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {
    private Calendar cal = Calendar.getInstance();
    private CalendarView calV;

    private long cur;

    private int year;
    private int month;
    private int dayOfMonth;

    private TextView displayTxtV;

    RelativeLayout rl_check, rl_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        rl_check = (RelativeLayout) findViewById(R.id.rl_check);
        rl_select = (RelativeLayout) findViewById(R.id.rl_select);

        rl_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        rl_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), SelectingActivity.class);
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

        defaultDate();
    }

    @Override
    public void onResume() {
        super.onResume();

        displayTxtV = (TextView) findViewById(R.id.displayTxtV);

        calV = (CalendarView) findViewById(R.id.calV);
        cur = calV.getDate();

        calV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year2, int month2, int dayOfMonth2) {
                year = year2;
                month = month2 + 1;
                dayOfMonth = dayOfMonth2;
                display();
            }
        });

        display();

        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        Button btn3 =(Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
    }

    private void selectDate() {
        Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("dayOfMonth", dayOfMonth);
        startActivity(intent);
    }

    private void send() {
        Intent intent = new Intent(getApplicationContext(), SendingActivity.class);
        startActivity(intent);
    }

    private void defaultDate() {
        int curYear = cal.get(Calendar.YEAR);
        int curMonth = cal.get(Calendar.MONTH) + 1;
        int curDayOfMonth = cal.get(Calendar.DATE);

        year = curYear;
        month = curMonth;
        dayOfMonth = curDayOfMonth;
    }

    private void display() {
        MyDBHelper myDBHelper = new MyDBHelper(this);
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        Cursor cursor;

        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String dbDate = "";
        Date d = null;
        int m = month - 1;
        try {
            d = dt.parse("" + year + "-" + month + "- " + dayOfMonth);
            dbDate = dt.format(d);
        } catch (Exception e) {
        }

        displayTxtV.setText("");
        String day = getDateDay(d);

        cursor = db.rawQuery("SELECT * FROM medi WHERE startDate <='" + dbDate + "' AND endDate >= '" + dbDate +"';", null);
        while(cursor.moveToNext()) {
            if (day.equals("일") && cursor.getInt(11) == 1) {
                displayTxtV.setText("목록 및 메모가 있습니다");
            }  else if (day.equals("월") && cursor.getInt(5) == 1) {
                displayTxtV.setText("목록 및 메모가 있습니다");
            }  else if (day.equals("화") && cursor.getInt(6) == 1) {
                displayTxtV.setText("목록 및 메모가 있습니다");
            }  else if (day.equals("수") && cursor.getInt(7) == 1) {
                displayTxtV.setText("목록 및 메모가 있습니다");
            } else if (day.equals("목") && cursor.getInt(8) == 1) {
                displayTxtV.setText("목록 및 메모가 있습니다");
            } else if (day.equals("금") && cursor.getInt(9) == 1) {
                displayTxtV.setText("목록 및 메모가 있습니다");
            } else if (day.equals("토") && cursor.getInt(10) == 1) {
                displayTxtV.setText("목록 및 메모가 있습니다");
            }
        }

        cursor.close();
        db.close();

        if (getMemo() == 1) {
            displayTxtV.setText("목록 및 메모가 있습니다");
        }
    }

    private int getMemo() {
        String fileName = year + "" + month + "" + dayOfMonth + ".txt";
        FileInputStream fis = null;
        String str = "";
        try {
            fis = openFileInput(fileName);
            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str = new String(fileData);
        } catch (Exception e) {
        }

        if (str.trim().equals("")) {
            return 0;
        } else {
            return 1;
        }
    }

    private String getDateDay(Date d) {
        String day = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;
        }

        return day;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
