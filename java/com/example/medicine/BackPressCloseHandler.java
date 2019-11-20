package com.example.medicine;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BackPressCloseHandler {
    private Activity activity;
    private long backKeyPressedTime = 0;
    private Toast toast;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis(); showGuide(); return;
        } if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish(); toast.cancel();
        }
    }

    public void showGuide() {
        toast = new Toast(activity);
        View toastView = (View) View.inflate(activity, R.layout.toast, null);
        TextView toastText = (TextView) toastView.findViewById(R.id.toast1);
        toastText.setText("뒤로가기 버튼을 한번 더 누르면 종료됩니다");
        toast.setView(toastView);
        toast.show();
    }
}
