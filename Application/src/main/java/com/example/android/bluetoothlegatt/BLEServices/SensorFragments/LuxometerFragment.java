package com.example.android.bluetoothlegatt.BLEServices.SensorFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class LuxometerFragment extends AbstractSensor {

    @Bind(R.id.cubiclinechart)
    ValueLineChart valueIrtLineChart;
    ValueLineSeries lineSeries = new ValueLineSeries();
    Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sensor_graph_fragement_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        lineSeries.setColor(Color.MAGENTA);
        for (int i = 0; i < DeviceControlActivity.TotalSize; i++) {
            ValueLinePoint valueLinePoint = new ValueLinePoint("", 0f);
            lineSeries.addPoint(valueLinePoint);
        }


        valueIrtLineChart.setIndicatorTextUnit("Luxometer");
        valueIrtLineChart.addSeries(lineSeries);
        valueIrtLineChart.setUseCubic(true);
        valueIrtLineChart.setUseOverlapFill(true);
        valueIrtLineChart.startAnimation();
    }


    @Override
    public void displayData(Point3D point3D) {
        ValueLineSeries lineSeries = valueIrtLineChart.getDataSeries().get(0);
        List<ValueLinePoint> valueLinePoints = lineSeries.getSeries();
        valueLinePoints.remove(0);
        valueLinePoints.add(new ValueLinePoint("Lux", (float) point3D.x));
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
