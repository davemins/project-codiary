package com.example.codiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.codiary.AlertReceiver;

import java.text.DateFormat;
import java.util.Calendar;

public class Fragment3 extends Fragment implements TimePickerDialog.OnTimeSetListener {

    public static final String TAG = "FRAGMENT3";

    private TextView time_text;
    private View rootView;
    private Calendar alarmTime; // 알람 시간을 저장할 변수

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment3, container, false);

        time_text = rootView.findViewById(R.id.time_text);

        Button time_btn = rootView.findViewById(R.id.time_btn);
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        Button alarm_cancel_btn = rootView.findViewById(R.id.alarm_cancel_btn);
        alarm_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        return rootView;
    }

    private void showTimePickerDialog() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        // TimePickerDialog 생성 및 설정
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), this, hour, minute, false);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // 알람 시간 설정
        alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        alarmTime.set(Calendar.MINUTE, minute);
        alarmTime.set(Calendar.SECOND, 0);

        updateTimeText();
        startAlarm();
    }

    private void updateTimeText() {
        if (alarmTime != null) {
            String timeText = "알람시간 : " + DateFormat.getTimeInstance(DateFormat.SHORT).format(alarmTime.getTime());
            time_text.setText(timeText);
        }
    }

    private void startAlarm() {
        // 알람 시간이 null이 아닌 경우에만 알람을 설정합니다.
        if (alarmTime != null) {
            // AlarmManager 인스턴스를 가져옵니다.
            AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

            // 알림을 받을 BroadcastReceiver를 설정하기 위한 Intent 생성합니다.
            Intent intent = new Intent(requireActivity(), AlertReceiver.class);

            // PendingIntent를 생성합니다. FLAG_UPDATE_CURRENT 플래그를 사용하여 이미 존재하는 PendingIntent를 업데이트합니다.
            PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // 알람 시간이 현재 시간보다 이전인 경우, 알람을 다음 날로 설정합니다.
            if (alarmTime.before(Calendar.getInstance())) {
                alarmTime.add(Calendar.DATE, 1);
            }

            // AlarmManager를 사용하여 정확한 시간에 알림을 설정합니다.
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);

            // 알림 설정 완료 메시지를 Toast로 표시합니다.
            Toast.makeText(requireContext(), "알림이 설정되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireActivity(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();

        Toast.makeText(requireContext(), "알림이 취소되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
