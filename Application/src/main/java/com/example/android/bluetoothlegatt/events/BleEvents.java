package com.example.android.bluetoothlegatt.events;


import java.util.Arrays;

public class BleEvents {

    private BleEnum bleEnum;
    private byte[] data;
    private String uuid;

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
                ", data=" + Arrays.toString(data) +
                ", uuid='" + uuid + '\'' +
                '}';
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public byte[] getData() {
        return data;
    }

    public String getUuid() {
        return uuid;
    }
}
