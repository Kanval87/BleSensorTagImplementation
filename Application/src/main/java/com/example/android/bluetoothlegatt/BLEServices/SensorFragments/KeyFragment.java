package com.example.android.bluetoothlegatt.BLEServices.SensorFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.bluetoothlegatt.BLEServices.SensorFragments.AbstractSensor.AbstractSensor;
import com.example.android.bluetoothlegatt.R;
import com.example.android.bluetoothlegatt.utils.Point3D;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class KeyFragment extends AbstractSensor {

    @Bind(R.id.piechart)
    PieChart mPieChart;

    @Bind(R.id.button_activate)
    Button button_activate;

    @Bind(R.id.button_period)
    Button button_period;

    Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sensor_pie_fragement_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        mPieChart.addPieSlice(new PieModel("", 0, Color.RED));
        mPieChart.addPieSlice(new PieModel("", 100, Color.GRAY));


        mPieChart.startAnimation();

        button_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bleGenericSensor.isEnable()) {
                    button_activate.setText(getResources().getText(R.string.string_button_activate));
                    bleGenericSensor.disable();
                } else {
                    button_activate.setText(getResources().getText(R.string.string_button_deactivate));
                    bleGenericSensor.enable();
                }
            }
        });
    }


    @Override
    public void displayData(final Point3D point3D) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int i = (int) point3D.x;
                switch (i) {
                    case 1:
                        mPieChart.clearChart();
                        mPieChart.addPieSlice(new PieModel("", 67, Color.GRAY));
                        mPieChart.addPieSlice(new PieModel("1 Key", 33, Color.RED));
                        break;
                    case 2:
                        mPieChart.clearChart();
                        mPieChart.addPieSlice(new PieModel("", 34, Color.GRAY));
                        mPieChart.addPieSlice(new PieModel("2 Key", 64, Color.RED));
                        break;
                }
            }
        });
    }

    @Override
    public void displayData(Point3D[] point3Ds) {

    }
}
