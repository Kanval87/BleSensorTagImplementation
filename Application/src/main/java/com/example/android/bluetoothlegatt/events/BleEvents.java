package com.example.android.bluetoothlegatt.events;


public class BleEvents {

    private BleEnum bleEnum;

    public BleEvents(BleEnum bleEnum) {
        this.bleEnum = bleEnum;
    }

    public BleEnum getBleEnum() {
        return bleEnum;
    }

    public void setBleEnum(BleEnum bleEnum) {
        this.bleEnum = bleEnum;
    }

    @Override
    public String toString() {
        return "BleEvents{" +
                "bleEnum=" + bleEnum +
                '}';
    }
}
