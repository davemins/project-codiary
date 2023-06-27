package com.example.codiary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Fragment1 extends Fragment {
    private DatePicker datePicker;
    private Spinner categorySpinner;
    private EditText contentEditText;
    private Button saveButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);

        datePicker = view.findViewById(R.id.datePicker);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        contentEditText = view.findViewById(R.id.contentEditText);
        saveButton = view.findViewById(R.id.saveButton);

        // 카테고리 스피너 초기화
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedFileName = getFileName(year, monthOfYear, dayOfMonth);
                String diaryContent = readDiary(selectedFileName);
                contentEditText.setText(diaryContent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int day = datePicker.getDayOfMonth();
                String selectedDate = getDateFromDatePicker(year, month, day);
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                String content = contentEditText.getText().toString();

                saveDiaryToFile(year, month, day, selectedDate, selectedCategory, content);

                Toast.makeText(getActivity(), "저장되었습니다!", Toast.LENGTH_SHORT).show();

                // 입력 필드 초기화
                contentEditText.setText("");
            }
        });

        return view;
    }

    private String getFileName(int year, int month, int day) {
        return year + "-" + (month + 1) + "-" + day + ".txt";
    }

    private String getDateFromDatePicker(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private String readDiary(String fileName) {
        String diaryContent = "";

        try {
            FileInputStream fileInputStream = requireContext().openFileInput(fileName);
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            fileInputStream.close();
            diaryContent = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return diaryContent;
    }

    private void saveDiaryToFile(int year, int month, int day, String selectedDate, String selectedCategory, String content) {
        // 파일 이름 생성
        String fileName = getFileName(year, month, day);

        if (fileName == null) {
            // 파일 이름이 null인 경우 처리
            return;
        }

        // 저장할 데이터 준비
        String data = "날짜: " + selectedDate + "\n분류: " + selectedCategory + "\n내용: " + content;

        try {
            // 파일 출력 스트림 생성
            FileOutputStream fileOutputStream = requireContext().openFileOutput(fileName, Context.MODE_PRIVATE);

            // 문자열을 파일에 쓰기 위한 OutputStreamWriter 생성
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            // 데이터를 파일에 작성
            outputStreamWriter.write(data);

            // 출력 스트림 닫기
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
