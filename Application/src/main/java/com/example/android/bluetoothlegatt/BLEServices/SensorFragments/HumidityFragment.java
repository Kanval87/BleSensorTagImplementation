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
import com.example.android.bluetoothlegatt.DeviceControlActivity;
import com.example.android.bluetoothlegatt.R;
import com.example.android.bluetoothlegatt.utils.Point3D;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HumidityFragment extends AbstractSensor {

    @Bind(R.id.cubiclinechart)
    ValueLineChart valueIrtLineChart;
    ValueLineSeries lineSeries = new ValueLineSeries();
    Handler handler = new Handler(Looper.getMainLooper());

    @Bind(R.id.button_activate)
    Button button_activate;

    @Bind(R.id.button_period)
    Button button_period;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sensor_graph_fragement_layout, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        lineSeries.setColor(Color.GRAY);
        for (int i = 0; i < DeviceControlActivity.TotalSize; i++) {
            ValueLinePoint valueLinePoint = new ValueLinePoint("", 0f);
            lineSeries.addPoint(valueLinePoint);
        }


        valueIrtLineChart.setIndicatorTextUnit("Humidity");
        valueIrtLineChart.addSeries(lineSeries);
        valueIrtLineChart.startAnimation();

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

        button_period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bleGenericSensor.isEnable()) {
                    showPeriodSelectorDialog();
                }
            }
        });
    }


    @Override
    public void displayData(Point3D point3D) {
        ValueLineSeries lineSeries = valueIrtLineChart.getDataSeries().get(0);
        List<ValueLinePoint> valueLinePoints = lineSeries.getSeries();
        valueLinePoints.remove(0);
        valueLinePoints.add(new ValueLinePoint("rH", (float) point3D.x));
        handler.post(new Runnable() {
            @Override
            public void run() {
                valueIrtLineChart.update();
            }
        });
    }

    @Override
    public void displayData(Point3D[] point3Ds) {

    }
}
