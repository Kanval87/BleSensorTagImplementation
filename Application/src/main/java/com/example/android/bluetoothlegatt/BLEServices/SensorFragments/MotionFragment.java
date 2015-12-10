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

import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.StackedBarModel;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MotionFragment extends AbstractSensor {

    @Bind(R.id.stackedbarchart)
    StackedBarChart stackedBarChart;
    Handler handler = new Handler(Looper.getMainLooper());

    @Bind(R.id.button_activate)
    Button button_activate;

    @Bind(R.id.button_period)
    Button button_period;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sensor_stackedbar_fragement_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        StackedBarModel stackedBarModel;

        stackedBarModel = new StackedBarModel("Accelerometer");
        stackedBarModel.addBar(new BarModel(2f, Color.GRAY));
        stackedBarModel.addBar(new BarModel(2f, Color.BLUE));
        stackedBarModel.addBar(new BarModel(2f, Color.RED));

        stackedBarChart.addBar(stackedBarModel);

        stackedBarModel = new StackedBarModel("Gyroscope");
        stackedBarModel.addBar(new BarModel(2f, Color.GRAY));
        stackedBarModel.addBar(new BarModel(2f, Color.BLUE));
        stackedBarModel.addBar(new BarModel(2f, Color.RED));

        stackedBarChart.addBar(stackedBarModel);

        stackedBarModel = new StackedBarModel("Megnometer");
        stackedBarModel.addBar(new BarModel(2f, Color.GRAY));
        stackedBarModel.addBar(new BarModel(2f, Color.BLUE));
        stackedBarModel.addBar(new BarModel(2f, Color.RED));

        stackedBarChart.addBar(stackedBarModel);

        stackedBarModel = new StackedBarModel("Accelerometer");
        stackedBarModel.addBar(new BarModel(2f, Color.GRAY));
        stackedBarModel.addBar(new BarModel(2f, Color.BLUE));
        stackedBarModel.addBar(new BarModel(2f, Color.RED));

        stackedBarChart.addBar(stackedBarModel);

        stackedBarModel = new StackedBarModel("Gyroscope");
        stackedBarModel.addBar(new BarModel(2f, Color.GRAY));
        stackedBarModel.addBar(new BarModel(2f, Color.BLUE));
        stackedBarModel.addBar(new BarModel(2f, Color.RED));

        stackedBarChart.addBar(stackedBarModel);

        stackedBarModel = new StackedBarModel("Megnometer");
        stackedBarModel.addBar(new BarModel(2f, Color.GRAY));
        stackedBarModel.addBar(new BarModel(2f, Color.BLUE));
        stackedBarModel.addBar(new BarModel(2f, Color.RED));

        stackedBarChart.addBar(stackedBarModel);

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
    public void displayData(Point3D[] point3Ds) {

        final StackedBarModel finalStackedBarModel0 = new StackedBarModel("Accelerometer");
        finalStackedBarModel0.addBar(new BarModel((float) point3Ds[0].x, Color.GRAY));
        finalStackedBarModel0.addBar(new BarModel((float) point3Ds[0].y, Color.BLUE));
        finalStackedBarModel0.addBar(new BarModel((float) point3Ds[0].z, Color.RED));


        final StackedBarModel finalStackedBarModel1 = new StackedBarModel("Gyroscope");
        finalStackedBarModel1.addBar(new BarModel((float) point3Ds[1].x, Color.GRAY));
        finalStackedBarModel1.addBar(new BarModel((float) point3Ds[1].y, Color.BLUE));
        finalStackedBarModel1.addBar(new BarModel((float) point3Ds[1].z, Color.RED));


        final StackedBarModel finalStackedBarModel2 = new StackedBarModel("Megnometer");
        finalStackedBarModel2.addBar(new BarModel((float) point3Ds[2].x, Color.GRAY));
        finalStackedBarModel2.addBar(new BarModel((float) point3Ds[2].y, Color.BLUE));
        finalStackedBarModel2.addBar(new BarModel((float) point3Ds[2].z, Color.RED));


        handler.post(new Runnable() {
            @Override
            public void run() {
                stackedBarChart.clearChart();
                stackedBarChart.addBar(finalStackedBarModel0);
                stackedBarChart.addBar(finalStackedBarModel1);
                stackedBarChart.addBar(finalStackedBarModel2);
                stackedBarChart.update();
            }
        });
    }

    @Override
    public void displayData(Point3D point3Ds) {

    }
}
