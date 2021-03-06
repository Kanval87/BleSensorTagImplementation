package com.example.android.bluetoothlegatt.BLEServices.BleSensor.Implementation;


import com.example.android.bluetoothlegatt.BLEServices.BleSensor.Implementation.bleSensorAbstract.BleGenericSensor;
import com.example.android.bluetoothlegatt.BluetoothLeService;
import com.example.android.bluetoothlegatt.utils.Point3D;

import java.util.UUID;

public class SimpleKeysSensor extends BleGenericSensor {


    private static final String TAG = SimpleKeysSensor.class.getSimpleName();
    private int key;

    public SimpleKeysSensor(UUID serviceUuid, BluetoothLeService mBluetoothLeService) {
        super(serviceUuid, mBluetoothLeService);
        this.key = 0;
    }

    @Override
    public Point3D convert(byte[] value) {
        byte b = value[0];
        key = b;
        return new Point3D((double) key, 0, 0);
    }

    @Override
    public String toString() {
        return this.key + " Key";
    }


}
