package com.example.android.bluetoothlegatt.BLEServices.BleSensor.Implementation;

import com.example.android.bluetoothlegatt.BLEServices.BleSensor.Implementation.bleSensorAbstract.BleGenericSensor;
import com.example.android.bluetoothlegatt.BluetoothLeService;
import com.example.android.bluetoothlegatt.utils.Point3D;

import java.util.UUID;

import static java.lang.Math.pow;

/**
 * Implements the Barometric Pressure Sensor.
 */
public class BarometerSensor extends BleGenericSensor {

    private double pressure;

    public BarometerSensor(UUID serviceUuid, BluetoothLeService mBluetoothLeService) {
        super(serviceUuid, mBluetoothLeService);
        this.pressure = 0.0;
    }

    @Override
    public Point3D convert(byte[] value) {
        if (value.length > 4) {
            Integer val = twentyFourBitUnsignedAtOffset(value, 2);
            this.pressure = (double) val / 100.0;
        } else {
            int mantissa;
            int exponent;
            Integer sfloat = shortUnsignedAtOffset(value, 2);
            mantissa = sfloat & 0x0FFF;
            exponent = (sfloat >> 12) & 0xFF;
            double output;
            double magnitude = pow(2.0f, exponent);
            output = (mantissa * magnitude);
            this.pressure = output / 100.0f;
        }
        return new Point3D(this.pressure, 0, 0);
    }

    @Override
    public String toString() {
        return this.pressure + " mbar";
    }

}
