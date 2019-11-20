package com.example.medicine;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SendingActivity extends AppCompatActivity {
    private TextView txtV1;
    private TextView txtV2;
    private Button btn1;
    private Button btn2;
    private Button btn5;

    private String startDate;
    private String endDate;

    private ScrollView scrollView;
    private ImageView btn_back;
    private int year2;
    private int month2;
    private int dayOfMonth2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending);

        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtV1 = (TextView) findViewById(R.id.txtV1);
        txtV2 = (TextView) findViewById(R.id.txtV2);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        final Calendar cal = Calendar.getInstance();

        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SendingActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDate = String.format("%d-%d-%d", year,
                                month + 1, dayOfMonth);
                        year2 = year;
                        month2 = month;
                        dayOfMonth2 = dayOfMonth;

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = null;
                        try {
                            d = format.parse(startDate);
                        } catch (ParseException e) {
                        }

                        String startDate2 = format.format(d);

                        txtV1.setText(startDate2);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.show();
            }
        });

        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SendingActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDate = String.format("%d-%d-%d", year,
                                month + 1, dayOfMonth);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date d = null;
                        try {
                            d = format.parse(endDate);
                        } catch (ParseException e) {
                        }

                        String endDate2 = format.format(d);

                        txtV2.setText(endDate2);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.show();
            }
        });

        btn5 = (Button) findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPeriod();
            }
        });
    }

    private void checkPeriod() {
        if (txtV1.getText().toString().length() == 0 || txtV2.getText().toString().length() == 0) {
            Toast toast1 = new Toast(SendingActivity.this);
            View toastView1 = (View) View.inflate(SendingActivity.this, R.layout.toast, null);
            TextView toastText1 = (TextView) toastView1.findViewById(R.id.toast1);
            toastText1.setText("시작 및 종료 날짜를 정해주세요");
            toast1.setView(toastView1);
            toast1.show();
            return;
        }

        Date d = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            d = simpleDateFormat.parse(startDate);
        } catch (Exception e) {
        }

        Date d2 = new Date();
        try {
            d2 = simpleDateFormat.parse(endDate);
        } catch (Exception e) {
        }

        int compare = d2.compareTo(d);

        if (compare < 0) {
            Toast toast1 = new Toast(SendingActivity.this);
            View toastView1 = (View) View.inflate(SendingActivity.this, R.layout.toast, null);
            TextView toastText1 = (TextView) toastView1.findViewById(R.id.toast1);
            toastText1.setText("종료 날짜가 시작 날짜보다 빠릅니다");
            toast1.setView(toastView1);
            toast1.show();
            return;
        } else {
            Intent intent = new Intent(getApplicationContext(), PdfActivity.class);
            intent.putExtra("startDate", startDate);
            intent.putExtra("endDate", endDate);
            intent.putExtra("year", year2);
            intent.putExtra("month", month2);
            intent.putExtra("dayOfMonth", dayOfMonth2);
            startActivity(intent);
            return;
        }
    }
}
