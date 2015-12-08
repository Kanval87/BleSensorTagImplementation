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

public class IrtFragment extends AbstractSensor {

    @Bind(R.id.cubiclinechart)
    ValueLineChart valueIrtLineChart;
    ValueLineSeries irtSeries = new ValueLineSeries();
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


        irtSeries.setColor(Color.YELLOW);
        for (int i = 0; i < DeviceControlActivity.TotalSize; i++) {
            ValueLinePoint valueLinePoint = new ValueLinePoint("", 0f);
            irtSeries.addPoint(valueLinePoint);
        }


        valueIrtLineChart.setIndicatorTextUnit("Ambient");
        valueIrtLineChart.addSeries(irtSeries);
        valueIrtLineChart.startAnimation();
    }


    @Override
    public void displayData(Point3D point3D) {
        ValueLineSeries lineSeries = valueIrtLineChart.getDataSeries().get(0);
        List<ValueLinePoint> valueLinePoints = lineSeries.getSeries();
        valueLinePoints.remove(0);
        valueLinePoints.add(new ValueLinePoint("Ambient", (float) point3D.z));
        handler.post(new Runnable() {
            @Override
            public void run() {
                valueIrtLineChart.update();
            }
        });
    }
}
