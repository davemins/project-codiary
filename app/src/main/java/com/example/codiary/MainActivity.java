package com.example.codiary;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    BottomNavigationView bottomNavigationView;
    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 액티비티 레이아웃 설정
        setContentView(R.layout.activity_main);

        // BottomNavigationView 초기화
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 프래그먼트 생성
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        // 제일 처음에 띄워줄 뷰를 세팅합니다.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_layout, fragment1)  // main_layout 영역에 fragment1을 추가합니다.
                .commitAllowingStateLoss();

        // BottomNavigationView의 아이템 선택 시 프래그먼트 전환을 처리하기 위한 리스너 설정
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.tab1) {  // 첫 번째 아이템 선택 시
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_layout, fragment1)  // main_layout 영역에 fragment1을 추가합니다.
                            .commitAllowingStateLoss();
                    return true;
                } else if (itemId == R.id.tab2) {  // 두 번째 아이템 선택 시
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_layout, fragment2)  // main_layout 영역에 fragment2를 추가합니다.
                            .commitAllowingStateLoss();
                    return true;
                } else if (itemId == R.id.tab3) {  // 세 번째 아이템 선택 시
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_layout, fragment3)  // main_layout 영역에 fragment3을 추가합니다.
                            .commitAllowingStateLoss();
                    return true;
                }
                return false;
            }
        });
    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
