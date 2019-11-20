package com.example.medicine;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle;
import com.tom_roush.pdfbox.pdmodel.font.PDFont;
import com.tom_roush.pdfbox.pdmodel.font.PDType0Font;
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PdfActivity extends AppCompatActivity {
    private static final String TAG = "webnautes" ;
    private File root;
    private AssetManager assetManager;
    private ImageView imageView;
    private TextView textView;
    private PDFont font;
    private ImageView btn_back;
    private String startDate;
    private String endDate;
    private SaveTask saveTask;

    private ProgressDialog progressBar;
    private MyDBHelper myDBHelper;
    private String fileName;
    private String text;

    private int year;
    private int month;
    private int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.txtV1);

        final Intent intent = getIntent();
        startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");

        getPdfContent();

        TextView txtV2 = (TextView) findViewById(R.id.txtV2);
        txtV2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final SaveTask saveTask = new SaveTask();
                saveTask.execute();
            }
        });
    }

    private void getPdfContent() {
        myDBHelper = new MyDBHelper(this);

        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        Cursor cursor;

        long diffDays = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        try {
            start = format.parse(startDate);
            end = format.parse(endDate);
            long diff = end.getTime() - start.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
        }

        String date = "";
        String date2 = "";
        date = format.format(start);
        date2 = format.format(end);

        text = date + " ~ " + date2 + "\r\n\r\n";

        final Intent intent2 = getIntent();
        year = intent2.getIntExtra("year", 0);
        month = intent2.getIntExtra("month", 0);
        dayOfMonth = intent2.getIntExtra("dayOfMonth", 0);
        month += 1;

        for (int i = 0; i <= diffDays; i++) {
            cursor = db.rawQuery("SELECT * FROM medi WHERE startDate <='" + date + "' AND endDate >= '" + date +"';", null);
            String day = "";
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);

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

            text = text + date + " " + day + "\r\n\r\n약 목록\n";

            while (cursor.moveToNext()) {
                if (day.equals("일") && cursor.getInt(11) == 1) {
                    text = text +  cursor.getString(1) + "\n";
                }  else if (day.equals("월") && cursor.getInt(5) == 1) {
                    text = text +  cursor.getString(1) + "\n";
                }  else if (day.equals("화") && cursor.getInt(6) == 1) {
                    text = text +  cursor.getString(1) + "\n";
                }  else if (day.equals("수") && cursor.getInt(7) == 1) {
                    text = text +  cursor.getString(1) + "\n";
                } else if (day.equals("목") && cursor.getInt(8) == 1) {
                    text = text +  cursor.getString(1) + "\n";
                } else if (day.equals("금") && cursor.getInt(9) == 1) {
                    text = text +  cursor.getString(1) + "\n";
                } else if (day.equals("토") && cursor.getInt(10) == 1) {
                    text = text +  cursor.getString(1) + "\n";
                }
            }
            text += "\r\n메모\n";
            displayMemo();

            text += "\r\n\r\n\n";
            cal.setTime(start);
            cal.add(Calendar.DATE, 1);
            date = format.format(cal.getTime());
            try {
                start = format.parse(date);
            } catch (ParseException e) {
            }

            plus();
        }

        textView.setText(text);
    }

    private void displayMemo() {
        fileName = year + "" + month + "" + dayOfMonth + ".txt";

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
        text += "\r\n" + str;
    }

    private boolean isLeapYear(int year) {
        return (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0));
    }

    private void plus() {
        if (month == 12 && dayOfMonth == 31) {
            year = year + 1;
            month = 1;
            dayOfMonth = 1;
        } else if ((month == 4 || month == 6 || month == 9 || month == 11) && dayOfMonth == 30) {
            month = month + 1;
            dayOfMonth = 1;
        } else if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10) && dayOfMonth == 31) {
            month = month + 1;
            dayOfMonth = 1;
        } else if ((month == 2 && isLeapYear(year)) && dayOfMonth == 29) {
            month = month + 1;
            dayOfMonth = 1;
        } else if ((month == 2 && !isLeapYear(year)) && dayOfMonth == 28) {
            month = month + 1;
            dayOfMonth = 1;
        }else {
            dayOfMonth = dayOfMonth + 1;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        setup();
    }

    private void setup() {

        PDFBoxResourceLoader.init(getApplicationContext());
        root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        assetManager = getAssets();

        if (ContextCompat.checkSelfPermission(PdfActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PdfActivity.this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public String createPdf() {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try {
            font = PDType0Font.load(document, assetManager.open("NanumBarunGothicLight.ttf"));
        } catch (IOException e) {
        }

        PDPageContentStream contentStream;

        try {
            contentStream = new PDPageContentStream( document, page, true, true);

            Drawable drawable = imageView.getDrawable();
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

            int image_width = bitmap.getWidth();
            int image_height = bitmap.getHeight();
            int A4_width = (int) PDRectangle.A4.getWidth();
            int A4_height = (int) PDRectangle.A4.getHeight();

            float scale = (float) (A4_width / (float)image_width*0.8);

            int image_w = (int) (bitmap.getWidth() * scale);
            int image_h = (int) (bitmap.getHeight() * scale);

            Bitmap resized = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
            PDImageXObject pdImage = LosslessFactory.createFromImage(document, resized);

            float x_pos = page.getCropBox().getWidth();
            float y_pos = page.getCropBox().getHeight();

            float x_adjusted = (float) (( x_pos - image_w ) * 0.5 + page.getCropBox().getLowerLeftX());
            float y_adjusted = (float) ((y_pos - image_h) * 0.9 + page.getCropBox().getLowerLeftY());

            contentStream.drawImage(pdImage, x_adjusted, y_adjusted, image_w, image_h);

            int text_width = 470;
            int text_left = 70;

            String textN = textView.getText().toString();
            int fontSize = 17;
            float leading = 1.5f * fontSize;

            PDRectangle mediabox = page.getMediaBox();
            float margin = 72;
            float startX = mediabox.getLowerLeftX() + margin;
            float startY = mediabox.getUpperRightY() - margin;

            List<String> lines = new ArrayList<String>();
            int lastSpace = - 1;

            for (String text : textN.split("\n")) {
                while (text.length() > 0) {
                    int spaceIndex = text.indexOf(' ', lastSpace + 1);
                    if (spaceIndex < 0)
                        spaceIndex = text.length();
                    String subString = text.substring(0, spaceIndex);
                    float size = fontSize * font.getStringWidth(subString) / 1000;
                    if (size > text_width) {
                        if (lastSpace < 0)
                            lastSpace = spaceIndex;
                        subString = text.substring(0, lastSpace);
                        subString += "\r\n";
                        lines.add(subString);
                        text = text.substring(lastSpace).trim();
                        lastSpace = - 1;
                    } else if (spaceIndex == text.length()) {
                        lines.add(text);
                        text = "";
                    } else {
                        lastSpace = spaceIndex;
                    }
                }
            }

            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.newLineAtOffset(text_left, y_adjusted - 20);
            float currentY = startY - leading * 3;

            for (String line: lines) {
                currentY -= leading;

                if (currentY <= margin) {
                    contentStream.endText();
                    contentStream.close();
                    PDPage new_Page = new PDPage();
                    document.addPage(new_Page);
                    contentStream = new PDPageContentStream(document, new_Page);
                    contentStream.beginText();
                    contentStream.setFont(font, fontSize);
                    contentStream.newLineAtOffset(startX, startY);
                    currentY = startY;
                }
                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -leading);
            }

            contentStream.endText();
            contentStream.close();

            String path = root.getAbsolutePath() + "/DDingDDing_" + startDate + "_" + endDate + ".pdf";
            contentStream.beginText();
            document.save(path);
            document.close();

            return path;
        } catch (IOException e) {
        }

        return "error";
    }

    @Override
    public void onBackPressed() {
        try {
            saveTask.cancel(true);
        } catch (Exception e) {
        }

        super.onBackPressed();
    }

    class SaveTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            if (isCancelled()) {
                return null;
            } else {
                String path = createPdf();
                return path;
            }
        }

        @Override
        protected void onPreExecute() {
            if (isCancelled()) {
                return;
            } else {
                progressBar = new ProgressDialog(PdfActivity.this);
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setMessage("파일을 다운로드 받는 중입니다. 잠시만 기다려주세요.");
                progressBar.setCancelable(false);
                progressBar.show();

                super.onPreExecute();
            }
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);

            Toast toast1 = new Toast(PdfActivity.this);
            View toastView1 = (View) View.inflate(PdfActivity.this, R.layout.toast, null);
            TextView toastText1 = (TextView) toastView1.findViewById(R.id.toast1);
            toastText1.setText(path+"에 PDF 파일로 저장했습니다.");
            toast1.setView(toastView1);
            toast1.show();
            onBackPressed();
        }
    }
}
