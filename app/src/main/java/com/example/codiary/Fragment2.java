package com.example.codiary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Fragment2 extends Fragment {
    private ListView fileListView;
    private ArrayAdapter<String> fileListAdapter;
    private List<String> fileList;
    private Spinner categorySpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

        fileListView = view.findViewById(R.id.listView);
        categorySpinner = view.findViewById(R.id.categorySpinner);

        // 파일 리스트 초기화
        fileList = new ArrayList<>();
        fileListAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, fileList);
        fileListView.setAdapter(fileListAdapter);

        // 카테고리 스피너 초기화
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // 선택된 카테고리 가져오기
                String selectedCategory = categorySpinner.getSelectedItem().toString();

                // 파일 리스트 다시 로드
                loadFilesByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    private void loadFilesByCategory(String category) {
        fileList.clear();

        // 내부 저장소 디렉토리 경로 가져오기
        File directory = requireContext().getFilesDir();

        // 내부 저장소 디렉토리에서 모든 파일들 가져오기
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.endsWith(".txt")) {
                    // 파일 이름에서 날짜 추출
                    String date = fileName.substring(0, fileName.indexOf(".txt"));

                    // 파일의 내용 읽어오기 등 원하는 처리 수행
                    String content = readDiaryContent(file);

                    // 파일의 카테고리 가져오기
                    String fileCategory = getCategoryFromFile(file);

                    // 선택된 카테고리가 "All" 이거나 파일의 카테고리가 선택된 카테고리와 일치하면 추가
                    if (category.equals("All") || category.equals(fileCategory)) {
                        // 날짜와 내용을 결합하여 리스트에 추가
                        String entry = date + "\n" + content;
                        fileList.add(entry);
                    }
                }
            }
        }

        // 날짜를 시간 역순으로 정렬
        Collections.sort(fileList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] parts1 = o1.split("\n");
                String[] parts2 = o2.split("\n");
                return parts2[0].compareTo(parts1[0]);
            }
        });

        // 리스트뷰 갱신
        fileListAdapter.notifyDataSetChanged();
    }

    private String readDiaryContent(File file) {
        StringBuilder content = new StringBuilder();

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // 첫 번째 줄은 날짜 정보이므로 건너뜀
            bufferedReader.readLine();

            // 두 번째 줄은 카테고리 정보이므로 건너뜀
            bufferedReader.readLine();

            // 파일의 내용을 읽어와서 StringBuilder에 추가
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    private String getCategoryFromFile(File file) {
        String category = ""; // 카테고리 정보를 저장할 변수

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // 첫 번째 줄은 날짜 정보이므로 건너뜀
            bufferedReader.readLine();

            // 두 번째 줄에 카테고리 정보가 있는지 확인하고 가져옴
            String line = bufferedReader.readLine();
            if (line != null && line.startsWith("분류: ")) {
                category = line.substring(4); // "분류: " 부분을 제외한 카테고리 정보만 저장
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return category;
    }


}

