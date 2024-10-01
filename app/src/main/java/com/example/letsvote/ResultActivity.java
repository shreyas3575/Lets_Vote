package com.example.letsvote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private PieChart pieChart;
    private List<PieEntry> pieEntryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        pieEntryList = new ArrayList<>();
        pieChart = findViewById(R.id.resultChart);
        setValues();
        new MyThread().start();
    }

    class MyThread extends Thread
    {
        @Override
        public void run() {
            try {
                sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            setUpChart();
        }
    }

    private void setUpChart() {
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "Pie Chart");
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
        pieData.setValueTextSize(16f);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void setValues() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Candidates");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                {
                    String name = String.valueOf(dataSnapshot1.child("Candidate Name").getValue());
                    int count = Integer.parseInt(String.valueOf(dataSnapshot1.child("count").getValue()));
                    pieEntryList.add(new PieEntry(count, name));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});
    }
}