package com.sdj2022.tp06todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CalendarView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sdj2022.tp06todoapp.databinding.ActivityEditBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    ActivityEditBinding binding;

    int category; //카테고리 구분번호
    String[] categoryTitle = new String[]{"ALL", "WORK", "STUDY", "HEALTH", "HOBBY", "MEETING", "ETC", "DONE"};

    String date = "2022년 09월 30일";

    BottomSheetDialog bottomSheetDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("할 일 추가");

        category = getIntent().getIntExtra("category", 0);
        if (category == 0) category = 1;
        binding.tvCategory.setText(categoryTitle[category]);

        date = new SimpleDateFormat("yyyy년 MM월 dd일").format(new Date());
        binding.tvDate.setText(date);

        binding.tvDate.setOnClickListener(v->showBottomSheetDialogCalendar());
        binding.tvCategory.setOnClickListener(v->showBottomSheetDialogCategory());
        binding.btnComplete.setOnClickListener(v->clickComplete());
    }//onCreate method..

    void clickComplete(){
        SQLiteDatabase db = openOrCreateDatabase("Todo", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS todo(num INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, date TEXT, category INTEGER, note TEXT, isDone INTEGER)");

        // 저장할 데이터들 ( title, date(멤버변수), category(멤버변수), note )
        String title = binding.etTitle.getText().toString();
        String note = binding.etNote.getText().toString();

        // "todo" 테이블 안에 데이터를 삽입하는 SQL 쿼리문 실행
        db.execSQL("INSERT INTO todo(title,date,category,note,isDone) VALUES(?,?,?,?,?)", new Object[]{title, date, category, note, 0});

        // 저장완료했으니..현재 액티비티 종료
        //finish();
        onBackPressed(); //돌아갈때 애니메이션을 적용했다면 finish 와 다르게 돌아감*
    }

    void showBottomSheetDialogCategory(){
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bs_category);
        bottomSheetDialog.show();

        bottomSheetDialog.findViewById(R.id.bsd_category_work).setOnClickListener(v->clickCategory(1));
        bottomSheetDialog.findViewById(R.id.bsd_category_study).setOnClickListener(v->clickCategory(2));
        bottomSheetDialog.findViewById(R.id.bsd_category_health).setOnClickListener(v->clickCategory(3));
        bottomSheetDialog.findViewById(R.id.bsd_category_hobby).setOnClickListener(v->clickCategory(4));
        bottomSheetDialog.findViewById(R.id.bsd_category_meeting).setOnClickListener(v->clickCategory(5));
        bottomSheetDialog.findViewById(R.id.bsd_category_etc).setOnClickListener(v->clickCategory(6));
    }

    void clickCategory(int category){

        this.category = category;
        binding.tvCategory.setText(categoryTitle[category]);

        bottomSheetDialog.dismiss();

    }

    void showBottomSheetDialogCalendar(){
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bs_calendar);
        bottomSheetDialog.show();

        CalendarView calendarView = bottomSheetDialog.findViewById(R.id.bsd_calendar);
        calendarView.setOnDateChangeListener((view, year, month, day)->{

            // 달력에서 선택한 날짜로 Calendar 객체 생성(태양력)
            GregorianCalendar calendar = new GregorianCalendar(year, month, day);

            date =  new SimpleDateFormat("yyyy년 MM월 dd일").format(calendar.getTime());
            binding.tvDate.setText(date);

            bottomSheetDialog.dismiss(); //다이얼로그 닫기

        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}