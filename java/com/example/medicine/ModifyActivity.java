package com.example.medicine;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ModifyActivity extends AppCompatActivity {
    ImageView btn_back;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button timeplus, btn_modify, start, end;
    EditText number, name;
    LinearLayout timelayout1, timelayout2, timelayout3, timelayout4, timelayout5;
    SQLiteDatabase sqlDB;
    MyDBHelper myHelper;
    Integer timesPerDay;
    Integer[] day_array;
    String[] time_array;
    String startday, endday;
    TextView[] tv = new TextView[5];
    Integer Y, M, D;
    //약 종류
    TextView textViewSym;

    //음성 인식
    Intent intent;
    SpeechRecognizer mRecognizer;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        //음성 인식
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);
            }
        }

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);

        Button buttonRecord = (Button) findViewById(R.id.btnRecord);
        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer.startListening(intent);
            }
        });

        myHelper = new MyDBHelper(this);

        Intent intent = getIntent();

        String mName = intent.getStringExtra("mediName");
        String mStart = intent.getStringExtra("startDate");
        String mEnd = intent.getStringExtra("endDate");
        String mType = intent.getStringExtra("mediType");

        final int MediId = intent.getIntExtra("mediId", 0);

        final int iMon = intent.getIntExtra("mon", 0);
        final int iTue = intent.getIntExtra("tue", 0);
        final int iWed = intent.getIntExtra("wed", 0);
        final int iThu = intent.getIntExtra("thu", 0);
        final int iFri = intent.getIntExtra("fri", 0);
        final int iSat = intent.getIntExtra("sat", 0);
        final int iSun = intent.getIntExtra("sun", 0);

        final int mTimes = intent.getIntExtra("timesPerDay", 0);
        String one = intent.getStringExtra("oneTime");
        String two = intent.getStringExtra("twoTime");
        String three = intent.getStringExtra("threeTime");
        String four = intent.getStringExtra("fourTime");
        String five = intent.getStringExtra("fiveTime");

        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_modify = (Button) findViewById(R.id.btn_modify);

        name = (EditText) findViewById(R.id.name);
        start = (Button) findViewById(R.id.startBtn);
        end = (Button) findViewById(R.id.endBtn);
        textViewSym = (TextView) findViewById(R.id.txtSym);
        number = (EditText) findViewById(R.id.number);

        final TextView startDate = (TextView) findViewById(R.id.startDate);
        final TextView endDate = (TextView) findViewById(R.id.endDate);

        startDate.setText(" " + mStart.substring(0,4) + "년 " + mStart.substring(5, 7) +"월 " +
                mStart.substring(8, 10) + "일");
        endDate.setText(" " + mEnd.substring(0, 4) + "년 " + mEnd.substring(5, 7) +"월 " +
                mEnd.substring(8, 10) + "일");

        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        startday = mStart;
        endday = mEnd;

        Y = Integer.parseInt(mStart.substring(0,4));
        M = Integer.parseInt(mStart.substring(5, 7));
        D = Integer.parseInt(mStart.substring(8, 10));

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ModifyActivity.this, R.style.DialogTheme, mDateSetListener, mYear, mMonth, mDay).show();
            }

            public DatePickerDialog.OnDateSetListener mDateSetListener =
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            Y = year;
                            M = monthOfYear + 1;
                            D = dayOfMonth;
                            UpdateNow();
                        }
                    };

            void UpdateNow() {
                if (mMonth < 9) {
                    if (mDay < 10) {
                        startDate.setText(String.format(" %d년 0%d월 0%d일", mYear, mMonth + 1, mDay));
                        startday = String.format("%d-0%d-0%d", mYear, mMonth + 1, mDay);
                    } else {
                        startDate.setText(String.format(" %d년 0%d월 %d일", mYear, mMonth + 1, mDay));
                        startday = String.format("%d-0%d-%d", mYear, mMonth + 1, mDay);
                    }
                } else {
                    if (mDay < 10) {
                        startDate.setText(String.format(" %d년 %d월 0%d일", mYear, mMonth + 1, mDay));
                        startday = String.format("%d-%d-0%d", mYear, mMonth + 1, mDay);
                    } else {
                        startDate.setText(String.format(" %d년 %d월 %d일", mYear, mMonth + 1, mDay));
                        startday = String.format("%d-%d-%d", mYear, mMonth + 1, mDay);
                    }
                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ModifyActivity.this, R.style.DialogTheme, mDateSetListener, mYear, mMonth, mDay).show();
            }

            public DatePickerDialog.OnDateSetListener mDateSetListener =
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            UpdateNow();
                        }
                    };

            void UpdateNow() {
                Toast toast3 = new Toast(ModifyActivity.this);
                View toastView3 = (View) View.inflate(ModifyActivity.this, R.layout.toast, null);
                TextView toastText3 = (TextView) toastView3.findViewById(R.id.toast1);
                toastText3.setText("잘못된 선택입니다.");
                toast3.setView(toastView3);

                if (mMonth < 9) {
                    if (mYear < Y) {
                        toast3.show();
                    } else if (mYear == Y && mMonth + 1 < M) {
                        toast3.show();
                    } else if (mYear == Y && mMonth + 1 == M && mDay < D) {
                        toast3.show();
                    } else if (mDay < 10) {
                        endDate.setText(String.format(" %d년 0%d월 0%d일", mYear, mMonth + 1, mDay));
                        endday = String.format("%d-0%d-0%d", mYear, mMonth + 1, mDay);
                    } else {
                        endDate.setText(String.format(" %d년 0%d월 %d일", mYear, mMonth + 1, mDay));
                        endday = String.format("%d-0%d-%d", mYear, mMonth + 1, mDay);
                    }
                } else {
                    if (mYear < Y) {
                        toast3.show();
                    } else if (mYear == Y && mMonth  + 1 < M) {
                        toast3.show();
                    } else if (mYear == Y && mMonth + 1 == M && mDay < D) {
                        toast3.show();
                    } else if (mDay < 10) {
                        endDate.setText(String.format(" %d년 %d월 0%d일", mYear, mMonth + 1, mDay));
                        endday = String.format("%d-%d-0%d", mYear, mMonth + 1, mDay);
                    } else {
                        endDate.setText(String.format(" %d년 %d월 %d일", mYear, mMonth + 1, mDay));
                        endday = String.format("%d-%d-%d", mYear, mMonth + 1, mDay);
                    }
                }
            }
        });

        final Button[] day = new Button[7];
        final Integer[] dayID = {R.id.mon, R.id.tue, R.id.wed, R.id.thu, R.id.fri, R.id.sat, R.id.sun};

        int i;

        for (i = 0; i < dayID.length; i++) {
            day[i] = (Button) findViewById(dayID[i]);
        }

        day_array = new Integer[7];
        for (i = 0; i < 7; i++) {
            day_array[i] = 0;
        }

        day_array[0] = iMon;
        day_array[1] = iTue;
        day_array[2] = iWed;
        day_array[3] = iThu;
        day_array[4] = iFri;
        day_array[5] = iSat;
        day_array[6] = iSun;

        for (i = 0; i < 7; i++) {
            if (day_array[i] == 1) {
                day[i].setBackgroundResource(R.drawable.primary_border_fill_4);
                day[i].setTextColor(Color.WHITE);
            }
        }

        for (i = 0; i < dayID.length; i++) {
            final int index;
            index = i;
            day[index].setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                public void onClick(View view) {
                    if (day[index].getCurrentTextColor() == Color.BLACK) {
                        day[index].setBackgroundResource(R.drawable.primary_border_fill_4);
                        day[index].setTextColor(Color.WHITE);
                        day_array[index] = 1;
                    } else {
                        day[index].setBackgroundResource(R.drawable.primary_border_fill3);
                        day[index].setTextColor(Color.BLACK);
                        day_array[index] = 0;
                    }
                }
            });
        }

        timeplus = (Button) findViewById(R.id.timeplus);
        registerForContextMenu(timeplus);

        timelayout1 = (LinearLayout) findViewById(R.id.timelayout1);
        timelayout2 = (LinearLayout) findViewById(R.id.timelayout2);
        timelayout3 = (LinearLayout) findViewById(R.id.timelayout3);
        timelayout4 = (LinearLayout) findViewById(R.id.timelayout4);
        timelayout5 = (LinearLayout) findViewById(R.id.timelayout5);

        final Button[] timeSet = new Button[5];
        final Integer[] timeSetID = {R.id.modi1, R.id.modi2, R.id.modi3, R.id.modi4, R.id.modi5};

        int j;

        for (j = 0; j < timeSetID.length; j++) {
            timeSet[j] = (Button) findViewById(timeSetID[j]);
        }

        final View[] timePick = new View[5];
        final Integer[] timePickID = {R.layout.timepicker1, R.layout.timepicker2, R.layout.timepicker3, R.layout.timepicker4, R.layout.timepicker5};

        final TimePicker[] times = new TimePicker[5];
        final Integer[] timesID = {R.id.timepicker1, R.id.timepicker2, R.id.timepicker3, R.id.timepicker4, R.id.timepicker5};

        final Integer[] tvID = {R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5};

        int k;

        for (k = 0; k < timePickID.length; k++) {
            timePick[k] = (View) View.inflate(ModifyActivity.this, timePickID[k], null);
            times[k] = (TimePicker) timePick[k].findViewById(timesID[k]);
            tv[k] = (TextView) findViewById(tvID[k]);
        }

        time_array = new String[5];
        for (k = 0; k < 5; k++) {
            time_array[k] = null;
        }

        time_array[0] = one;
        time_array[1] = two;
        time_array[2]= three;
        time_array[3] = four;
        time_array[4] = five;

        LinearLayout[] timelayout = {timelayout1, timelayout2, timelayout3, timelayout4, timelayout5};
        for (int o = 0; o < mTimes; o++) {
            if (Integer.parseInt(time_array[o].substring(0, 2)) < 12) {
                tv[o].setText("오전 " + time_array[o].substring(0,2) + " : "
                        + time_array[o].substring(3, 5));
                timelayout[o].setVisibility(View.VISIBLE);
            } else if (Integer.parseInt(time_array[o].substring(0, 2)) > 12) {
                int n = Integer.parseInt(time_array[o].substring(0,2));
                if(n - 12 < 10) {
                    tv[o].setText("오후 0" + Integer.toString(n - 12) + " : " + time_array[o].substring(3,5));
                    timelayout[o].setVisibility(View.VISIBLE);
                } else {
                    tv[o].setText("오후 " + Integer.toString(n - 12) + " : " + time_array[o].substring(3,5));
                    timelayout[o].setVisibility(View.VISIBLE);
                }
            } else {
                tv[o].setText("오후 " + time_array[o].substring(0,2) + " : " + time_array[o].substring(3, 5));
                timelayout[o].setVisibility(View.VISIBLE);
            }
        }

        for (k = 0; k < timePickID.length; k++) {
            final int index;
            index = k;
            timeSet[index].setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("deprecation")
                public void onClick(View view) {
                    AlertDialog.Builder dlg1 = new AlertDialog.Builder(ModifyActivity.this, R.style.DialogTheme);
                    dlg1.setTitle("시간선택");
                    if (timePick[index].getParent() != null) {
                        ((ViewGroup) timePick[index].getParent()).removeView(timePick[index]);
                    }
                    times[index].setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
                    dlg1.setView(timePick[index]);
                    dlg1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (times[index].getCurrentHour() < 12) {
                                if(times[index].getCurrentHour() <10){
                                    if(times[index].getCurrentMinute() < 10){
                                        tv[index].setText("오전 " + 0 + Integer.toString(times[index].getCurrentHour()) + " : " +
                                                0 + Integer.toString(times[index].getCurrentMinute()));
                                        timeSet[index].setText("수정");
                                        time_array[index] = 0 + Integer.toString(times[index].getCurrentHour()) + ":" +
                                                0 + Integer.toString(times[index].getCurrentMinute());
                                    } else {
                                        tv[index].setText("오전 " + 0 + Integer.toString(times[index].getCurrentHour()) + " : "
                                                + Integer.toString(times[index].getCurrentMinute()));
                                        timeSet[index].setText("수정");
                                        time_array[index] = 0 + Integer.toString(times[index].getCurrentHour()) + ":"
                                                + Integer.toString(times[index].getCurrentMinute());
                                    }
                                } else {
                                    if(times[index].getCurrentMinute() < 10){
                                        tv[index].setText("오전 " + Integer.toString(times[index].getCurrentHour()) + " : "
                                                + 0 + Integer.toString(times[index].getCurrentMinute()));
                                        timeSet[index].setText("수정");
                                        time_array[index] = Integer.toString(times[index].getCurrentHour()) + ":"
                                                + 0 + Integer.toString(times[index].getCurrentMinute());
                                    } else {
                                        tv[index].setText("오전 " + Integer.toString(times[index].getCurrentHour()) + " : "
                                                + Integer.toString(times[index].getCurrentMinute()));
                                        timeSet[index].setText("수정");
                                        time_array[index] = Integer.toString(times[index].getCurrentHour()) + ":"
                                                + Integer.toString(times[index].getCurrentMinute());
                                    }
                                }
                            } else {
                                if(times[index].getCurrentHour() - 12 < 10){
                                    if(times[index].getCurrentMinute() < 10) {
                                        tv[index].setText("오후 " + 0 + Integer.toString(times[index].getCurrentHour() - 12) + " : "
                                                + 0 + Integer.toString(times[index].getCurrentMinute()));
                                        timeSet[index].setText("수정");
                                        time_array[index] = Integer.toString(times[index].getCurrentHour()) + ":"
                                                + 0 + Integer.toString(times[index].getCurrentMinute());
                                    } else {
                                        tv[index].setText("오후 " + 0 + Integer.toString(times[index].getCurrentHour() - 12) + " : "
                                                + Integer.toString(times[index].getCurrentMinute()));
                                        timeSet[index].setText("수정");
                                        time_array[index] = Integer.toString(times[index].getCurrentHour()) + ":"
                                                + Integer.toString(times[index].getCurrentMinute());
                                    }
                                } else if(times[index].getCurrentMinute() < 10){
                                    tv[index].setText("오후 " + Integer.toString(times[index].getCurrentHour() - 12) + " : "
                                            + 0 + Integer.toString(times[index].getCurrentMinute()));
                                    timeSet[index].setText("수정");
                                    time_array[index] = Integer.toString(times[index].getCurrentHour()) + ":"
                                            + 0 + Integer.toString(times[index].getCurrentMinute());
                                } else {
                                    tv[index].setText("오후 " + Integer.toString(times[index].getCurrentHour() - 12) + " : "
                                            + Integer.toString(times[index].getCurrentMinute()));
                                    timeSet[index].setText("수정");
                                    time_array[index] = Integer.toString(times[index].getCurrentHour()) + ":"
                                            + Integer.toString(times[index].getCurrentMinute());
                                }
                            }
                        }
                    });
                    dlg1.setNegativeButton("취소", null);
                    dlg1.show();
                }
            });
        }

        name.setText(mName);
        textViewSym.setText(mType);
        number.setText("1일 " + mTimes + "회 복용");

        Button buttonType0 = (Button) findViewById(R.id.btnType0);
        buttonType0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSym.setText("병원 약");
            }
        });

        Button buttonType1 = (Button) findViewById(R.id.btnType1);
        buttonType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSym.setText("감기");
            }
        });

        Button buttonType2 = (Button) findViewById(R.id.btnType2);
        buttonType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSym.setText("복통");
            }
        });

        Button buttonType3 = (Button) findViewById(R.id.btnType3);
        buttonType3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSym.setText("생리통");
            }
        });

        Button buttonType4 = (Button) findViewById(R.id.btnType4);
        buttonType4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSym.setText("안약");
            }
        });

        Button buttonType5 = (Button) findViewById(R.id.btnType5);
        buttonType5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSym.setText("치통");
            }
        });

        Button buttonType6 = (Button) findViewById(R.id.btnType6);
        buttonType6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSym.setText("비타민");
            }
        });

        Button buttonType7 = (Button) findViewById(R.id.btnType7);
        buttonType7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewSym.setText("기타");
            }
        });

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name == null || startday == null || endday == null ||
                        (time_array[0] == null && time_array[1] == null && time_array[2] == null &&
                                time_array[3] == null && time_array[4] == null)
                        || (day_array[0] == 0 && day_array[1] == 0 && day_array[2] == 0 && day_array[3] == 0
                        && day_array[4] == 0 && day_array[5] == 0 && day_array[6] == 0) || textViewSym.getText().equals("나의 증상")) {
                    Toast toast1 = new Toast(ModifyActivity.this);
                    View toastView1 = (View) View.inflate(ModifyActivity.this, R.layout.toast, null);
                    TextView toastText1 = (TextView) toastView1.findViewById(R.id.toast1);
                    toastText1.setText("모든 항목을 입력하세요");
                    toast1.setView(toastView1);
                    toast1.show();
                } else if (timesPerDay == null) {
                    timesPerDay = mTimes;
                } else {
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("UPDATE medi SET mediName = '" + name.getText().toString() + "', " +
                            "startDate = '" + startday + "', endDate = '" + endday + "', " +
                            "timesPerDay = " + timesPerDay + ", mon = " + day_array[0] + ", " +
                            "tue = " + day_array[1] + ", wed = " + day_array[2] + ", " +
                            "thu = " + day_array[3] + ", fri = " + day_array[4] + ", " +
                            "sat = " + day_array[5] + ", sun = " + day_array[6] + ", mediType = '" + textViewSym.getText().toString() + "' " + "WHERE mediId = " + MediId + ";");
                    sqlDB.execSQL("UPDATE time SET oneTime = '" + time_array[0] + "', " +
                            "twoTime = '" + time_array[1] + "', threeTime = '" + time_array[2] + "', " +
                            "fourTime = '" + time_array[3] + "', fiveTime = '" + time_array[4] + "' " +
                            "WHERE timeId = " + MediId + ";");
                    sqlDB.close();

                    startService(new Intent(ModifyActivity.this, MyService.class));
                    Intent intent = new Intent(getApplicationContext(), SelectingActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    //음성 인식
    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            name.setText("좀 더 빨리 말해주세요");
        }

        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            name.setText(rs[0]);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater mInflater = getMenuInflater();
        if (v == timeplus) {
            mInflater.inflate(R.menu.time_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        timesPerDay = null;

        switch (item.getItemId()) {
            case R.id.item1:
                number.setText("1일 1회 복용");
                timesPerDay = 1;
                timelayout1.setVisibility(View.VISIBLE);
                timelayout2.setVisibility(View.GONE);
                time_array[1] = null;
                timelayout3.setVisibility(View.GONE);
                time_array[2] = null;
                timelayout4.setVisibility(View.GONE);
                time_array[3] = null;
                timelayout5.setVisibility(View.GONE);
                time_array[4] = null;
                break;
            case R.id.item2:
                number.setText("1일 2회 복용");
                timesPerDay = 2;
                timelayout1.setVisibility(View.VISIBLE);
                timelayout2.setVisibility(View.VISIBLE);
                timelayout3.setVisibility(View.GONE);
                time_array[2] = null;
                timelayout4.setVisibility(View.GONE);
                time_array[3] = null;
                timelayout5.setVisibility(View.GONE);
                time_array[4] = null;
                break;
            case R.id.item3:
                number.setText("1일 3회 복용");
                timesPerDay = 3;
                timelayout1.setVisibility(View.VISIBLE);
                timelayout2.setVisibility(View.VISIBLE);
                timelayout3.setVisibility(View.VISIBLE);
                timelayout4.setVisibility(View.GONE);
                time_array[3] = null;
                timelayout5.setVisibility(View.GONE);
                time_array[4] = null;
                break;
            case R.id.item4:
                number.setText("1일 4회 복용");
                timesPerDay = 4;
                timelayout1.setVisibility(View.VISIBLE);
                timelayout2.setVisibility(View.VISIBLE);
                timelayout3.setVisibility(View.VISIBLE);
                timelayout4.setVisibility(View.VISIBLE);
                timelayout5.setVisibility(View.GONE);
                time_array[4] = null;
                break;
            case R.id.item5:
                number.setText("1일 5회 복용");
                timesPerDay = 5;
                timelayout1.setVisibility(View.VISIBLE);
                timelayout2.setVisibility(View.VISIBLE);
                timelayout3.setVisibility(View.VISIBLE);
                timelayout4.setVisibility(View.VISIBLE);
                timelayout5.setVisibility(View.VISIBLE);
                break;
        }

        return false;
    }
}
