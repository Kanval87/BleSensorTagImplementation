package com.example.android.bluetoothlegatt.BLEServices.Implementation;


import com.example.android.bluetoothlegatt.BLEServices.BleGenericSensor;
import com.example.android.bluetoothlegatt.BluetoothLeService;
import com.example.android.bluetoothlegatt.utils.Point3D;

import java.util.UUID;

public class SensorTagHumidityProfile extends BleGenericSensor {


    private static final String TAG = SensorTagHumidityProfile.class.getSimpleName();
    private double humidity;

    public SensorTagHumidityProfile(UUID serviceUuid, BluetoothLeService mBluetoothLeService) {
        super(serviceUuid, mBluetoothLeService);
        this.humidity = 0.0;
    }

    @Override
    public Point3D convert(byte[] value) {
        int a = shortUnsignedAtOffset(value, 2);
        // bits [1..0] are status bits and need to be cleared according
        // to the user guide, but the iOS code doesn't bother. It should
        // have minimal impact.
        a = a - (a % 4);
        this.humidity = (-6f) + 125f * (a / 65535f);
        return new Point3D(this.humidity, 0, 0);
    }

    @Override
    public String toString() {
        return this.humidity + " rH";
    }


}
