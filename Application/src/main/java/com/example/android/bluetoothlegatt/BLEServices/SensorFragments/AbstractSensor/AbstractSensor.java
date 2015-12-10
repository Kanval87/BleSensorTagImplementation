package com.example.android.bluetoothlegatt.BLEServices.SensorFragments.AbstractSensor;

import android.app.Fragment;

import com.example.android.bluetoothlegatt.BLEServices.BleGenericSensor;

public abstract class AbstractSensor extends Fragment implements SensorDataListerner {

    protected BleGenericSensor bleGenericSensor;

    public void setBleGenericSensor(BleGenericSensor bleGenericSensor) {
        this.bleGenericSensor = bleGenericSensor;
    }

}
