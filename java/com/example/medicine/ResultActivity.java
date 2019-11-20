package com.example.medicine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ResultActivity extends AppCompatActivity {
    SQLiteDatabase sqlDB;
    MyDBHelper myHelper;
    ImageView btn_back;
    LinearLayout llMost;
    TextView txtType0, txtType1, txtType2, txtType3,
            txtType4, txtType5, txtType6, txtType7,
            txtNum0, txtNum1, txtNum2, txtNum3,
            txtNum4, txtNum5, txtNum6, txtNum7,
            txtDate, txtMost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        txtType0 = (TextView) findViewById(R.id.meditype0);
        txtType1 = (TextView) findViewById(R.id.meditype1);
        txtType2 = (TextView) findViewById(R.id.meditype2);
        txtType3 = (TextView) findViewById(R.id.meditype3);
        txtType4 = (TextView) findViewById(R.id.meditype4);
        txtType5 = (TextView) findViewById(R.id.meditype5);
        txtType6 = (TextView) findViewById(R.id.meditype6);
        txtType7 = (TextView) findViewById(R.id.meditype7);

        txtNum0 = (TextView) findViewById(R.id.txt_type_num0);
        txtNum1 = (TextView) findViewById(R.id.txt_type_num1);
        txtNum2 = (TextView) findViewById(R.id.txt_type_num2);
        txtNum3 = (TextView) findViewById(R.id.txt_type_num3);
        txtNum4 = (TextView) findViewById(R.id.txt_type_num4);
        txtNum5 = (TextView) findViewById(R.id.txt_type_num5);
        txtNum6 = (TextView) findViewById(R.id.txt_type_num6);
        txtNum7 = (TextView) findViewById(R.id.txt_type_num7);

        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtDate = (TextView) findViewById(R.id.txtDate);
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        txtDate.setText(month + "월");

        llMost = (LinearLayout) findViewById(R.id.ll_most);
        txtMost = (TextView) findViewById(R.id.txt_most);

        myHelper = new MyDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();

        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM medi WHERE startDate >= date('now', 'start of month') AND endDate <= date('now', 'start of month', '+1 month', '-1 day', 'localtime');", null);

        int sum0 = 0, sum1 = 0, sum2 = 0, sum3 = 0,
                sum4 = 0, sum5 = 0, sum6 = 0, sum7 = 0;
        String str;
        while (cursor.moveToNext()) {
            str = cursor.getString(12);

            if (str.equals("병원 약")) {
                sum0 += 1;
            } else if (str.equals("감기")) {
                sum1 += 1;
            } else if (str.equals("복통")) {
                sum2 += 1;
            } else if (str.equals("생리통")) {
                sum3 += 1;
            } else if (str.equals("치통")) {
                sum4 += 1;
            } else if (str.equals("안약")) {
                sum5 += 1;
            } else if (str.equals("비타민")) {
                sum6 += 1;
            } else if (str.equals("기타")) {
                sum7 += 1;
            }
        }

        txtNum0.setText(sum0 + "");
        txtNum1.setText(sum1 + "");
        txtNum2.setText(sum2 + "");
        txtNum3.setText(sum3 + "");
        txtNum4.setText(sum4 + "");
        txtNum5.setText(sum5 + "");
        txtNum6.setText(sum6 + "");
        txtNum7.setText(sum7 + "");

        ArrayList<Integer> bList = new ArrayList<>();
        bList.add(sum0);
        bList.add(sum1);
        bList.add(sum2);
        bList.add(sum3);
        bList.add(sum4);
        bList.add(sum5);
        bList.add(sum6);
        bList.add(sum7);

        ArrayList<Integer> aList = new ArrayList<>();
        aList.add(sum0);
        aList.add(sum1);
        aList.add(sum2);
        aList.add(sum3);
        aList.add(sum4);
        aList.add(sum5);
        aList.add(sum6);
        aList.add(sum7);

        Collections.sort(aList);

        if (aList.get(7) != 0) {
            llMost.setVisibility(View.VISIBLE);

            for (int i = 0; i < bList.size(); i++) {
                if (bList.get(i) == aList.get(7)) {
                    if (i == 0) {
                        txtMost.append(" " + txtType0.getText());
                    } else if (i == 1) {
                        txtMost.append(" " + txtType1.getText());
                    } else if (i == 2) {
                        txtMost.append(" " + txtType2.getText());
                    } else if (i == 3) {
                        txtMost.append(" " + txtType3.getText());
                    } else if (i == 4) {
                        txtMost.append(" " + txtType4.getText());
                    } else if (i == 5) {
                        txtMost.append(" " + txtType5.getText());
                    } else if (i == 6) {
                        txtMost.append(" " + txtType6.getText());
                    } else if (i == 7) {
                        txtMost.append(" " + txtType7.getText());
                    }
                }
            }
        } else {
            llMost.setVisibility(View.INVISIBLE);
        }
    }
}
