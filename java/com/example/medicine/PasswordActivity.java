package com.example.medicine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordActivity extends AppCompatActivity {
    static String subPassword = "";
    static String previousPassword = "";

    TextView txtPassword;
    ImageView password1, password2, password3, password4;
    TextView btnForgetPw;
    Button btn1, btn2, btn3,
            btn4, btn5, btn6,
            btn7, btn8, btn9, btn0;
    ImageButton btnNumBack;

    SharedPreferenceController controller = new SharedPreferenceController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        txtPassword = (TextView) findViewById(R.id.txtPassword);
        password1 = (ImageView)findViewById(R.id.password1);
        password2 = (ImageView)findViewById(R.id.password2);
        password3 = (ImageView)findViewById(R.id.password3);
        password4 = (ImageView)findViewById(R.id.password4);
        btnForgetPw = (TextView) findViewById(R.id.btnForgetPw);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn7 = (Button)findViewById(R.id.btn7);
        btn8 = (Button)findViewById(R.id.btn8);
        btn9 = (Button)findViewById(R.id.btn9);
        btn0 = (Button)findViewById(R.id.btn0);
        btnNumBack = (ImageButton)findViewById(R.id.btnNumBack);

        previousPassword = controller.getPassword(this);

        if (previousPassword.equals("")) {
            //처음 암호 설정
            txtPassword.setText("비밀번호를 설정해주세요");
            btnForgetPw.setVisibility(View.INVISIBLE);
            btnForgetPw.setEnabled(false);
        } else if (!previousPassword.equals("")) {
            Log.e("prev", previousPassword);

            txtPassword.setText("비밀번호를 입력해주세요");
            btnForgetPw.setVisibility(View.VISIBLE);
            btnForgetPw.setEnabled(true);

            btnForgetPw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast = new Toast(PasswordActivity.this);
                    View toastView = (View)View.inflate(PasswordActivity.this, R.layout.toast, null);
                    TextView toastText = (TextView)toastView.findViewById(R.id.toast1);
                    toastText.setText(previousPassword);
                    toast.setView(toastView);
                    toast.show();
                }
            });
        }

        setNumBtnClickListener();
    }

    void setNumBtnClickListener() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn(1);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn(2);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn(3);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn(4);
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn(5);
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn(6);
            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn(7);
            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn(8);
            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn(9);
            }
        });

        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn(0);
            }
        });

        btnNumBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn(-1);
            }
        });
    }

    void clickBtn(int num) {
        if (num != -1) {
            int length = subPassword.length();

            if (length < 4) {
                subPassword += num;

                Log.e("sub_plus", subPassword);

                if (length + 1 == 1) {
                    password1.setSelected(!password1.isSelected());
                } else if (length + 1 == 2) {
                    password2.setSelected(!password2.isSelected());
                } else if (length + 1 == 3) {
                    password3.setSelected(!password3.isSelected());
                } else if (length + 1 == 4) {
                    password4.setSelected(!password4.isSelected());

                    password1.setSelected(false);
                    password2.setSelected(false);
                    password3.setSelected(false);
                    password4.setSelected(false);

                    if (previousPassword.equals("")) {
                        //처음 암호 설정
                        btnForgetPw.setVisibility(View.INVISIBLE);
                        btnForgetPw.setEnabled(false);

                        controller.setPassword(PasswordActivity.this, subPassword);

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //설정한 암호 존재
                        if (previousPassword.equals(subPassword)) {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            subPassword = "";
                            txtPassword.setText("한 번 더 입력해주세요");
                            btnForgetPw.setVisibility(View.VISIBLE);
                            btnForgetPw.setEnabled(true);
                        }
                    }
                }
            }
        } else {
            int length = subPassword.length();

            if (length != 0) {
                //길이가 0이 아니면 암호 맨 뒤 숫자 삭제
                //하늘색 -> 회색

                switch (length) {
                    case 1: password1.setSelected(false);
                        break;

                    case 2: password2.setSelected(false);
                        break;

                    case 3: password3.setSelected(false);
                        break;

                    case 4: password4.setSelected(false);
                        break;
                }
            }

            String[] subs = subPassword.split("");
            subPassword = "";
            for (int i = 0; i < subs.length - 1; i++) {
                subPassword += subs[i];
            }

            Log.e("sub_back", subPassword);
        }
    }
}
