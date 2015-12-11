package com.example.android.bluetoothlegatt;

import java.util.HashMap;
import java.util.UUID;

import static java.util.UUID.fromString;

public class SensorTagGatt {

    public final static UUID UUID_DEVINFO_SERV = fromString("0000180a-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_DEVINFO_FWREV = fromString("00002A26-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_IRT_TEMPRATURE_SERV = fromString("f000aa00-0451-4000-b000-000000000000");
    public final static UUID UUID_IRT_TEMPRATURE_DATA = fromString("f000aa01-0451-4000-b000-000000000000");
    public final static UUID UUID_IRT_TEMPRATURE_CONF = fromString("f000aa02-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    public final static UUID UUID_IRT_TEMPRATURE_PERI = fromString("f000aa03-0451-4000-b000-000000000000"); // Period in tens of milliseconds
    public final static UUID UUID_ACCELEROMETER_SERV = fromString("f000aa10-0451-4000-b000-000000000000");
    public final static UUID UUID_ACCELEROMETER_DATA = fromString("f000aa11-0451-4000-b000-000000000000");
    public final static UUID UUID_ACCELEROMETER_CONF = fromString("f000aa12-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    public final static UUID UUID_ACCELEROMETER_PERI = fromString("f000aa13-0451-4000-b000-000000000000"); // Period in tens of milliseconds
    public final static UUID UUID_HUMIDITY_SERV = fromString("f000aa20-0451-4000-b000-000000000000");
    public final static UUID UUID_HUMIDITY_DATA = fromString("f000aa21-0451-4000-b000-000000000000");
    public final static UUID UUID_HUMIDITY_CONF = fromString("f000aa22-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    public final static UUID UUID_HUMIDITY_PERI = fromString("f000aa23-0451-4000-b000-000000000000"); // Period in tens of milliseconds
    public final static UUID UUID_MAGNETIC_SERV = fromString("f000aa30-0451-4000-b000-000000000000");
    public final static UUID UUID_MAGNETIC_DATA = fromString("f000aa31-0451-4000-b000-000000000000");
    public final static UUID UUID_MAGNETIC_CONF = fromString("f000aa32-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    public final static UUID UUID_MAGNETIC_PERI = fromString("f000aa33-0451-4000-b000-000000000000"); // Period in tens of milliseconds
    public final static UUID UUID_LUXOMETER_SERV = fromString("f000aa70-0451-4000-b000-000000000000");
    public final static UUID UUID_LUXOMETER_DATA = fromString("f000aa71-0451-4000-b000-000000000000");
    public final static UUID UUID_LUXOMETER_CONF = fromString("f000aa72-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    public final static UUID UUID_LUXOMETER_PERI = fromString("f000aa73-0451-4000-b000-000000000000"); // Period in tens of milliseconds
    public final static UUID UUID_BAROMETER_SERV = fromString("f000aa40-0451-4000-b000-000000000000");
    public final static UUID UUID_BAROMETER_DATA = fromString("f000aa41-0451-4000-b000-000000000000");
    public final static UUID UUID_BAROMETER_CONF = fromString("f000aa42-0451-4000-b000-000000000000"); // 0: disable, 1: enable
    public final static UUID UUID_BAROMETER_CALI = fromString("f000aa43-0451-4000-b000-000000000000"); // Calibration characteristic
    public final static UUID UUID_BAROMETER_PERI = fromString("f000aa44-0451-4000-b000-000000000000"); // Period in tens of milliseconds
    public final static UUID UUID_GYROSCOPE_SERV = fromString("f000aa50-0451-4000-b000-000000000000");
    public final static UUID UUID_GYROSCOPE_DATA = fromString("f000aa51-0451-4000-b000-000000000000");
    public final static UUID UUID_GYROSCOPE_CONF = fromString("f000aa52-0451-4000-b000-000000000000"); // 0: disable, bit 0: enable x, bit 1: enable y, bit 2: enable z
    public final static UUID UUID_GYROSCOPE_PERI = fromString("f000aa53-0451-4000-b000-000000000000"); // Period in tens of milliseconds
    public final static UUID UUID_MOTION_SERV = fromString("f000aa80-0451-4000-b000-000000000000");
    public final static UUID UUID_MOTION_DATA = fromString("f000aa81-0451-4000-b000-000000000000");
    public final static UUID UUID_MOTION_CONF = fromString("f000aa82-0451-4000-b000-000000000000"); // 0: disable, bit 0: enable x, bit 1: enable y, bit 2: enable z
    public final static UUID UUID_MOTION_PERI = fromString("f000aa83-0451-4000-b000-000000000000"); // Period in tens of milliseconds
    public final static UUID UUID_KEY_SERV = fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_KEY_DATA = fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public static int Default_Period_Value = 100;
    // Useful maps.
    private static HashMap<String, String> attributes = new HashMap<>();
    private static HashMap<String, String> serverToConfig = new HashMap<>();
    private static HashMap<String, String> serverToData = new HashMap<>();
    private static HashMap<String, String> serverToPeriod = new HashMap<>();

    static {
        // Sample Services.
        attributes.put(UUID_DEVINFO_SERV.toString(), "Device Information");
        attributes.put(UUID_IRT_TEMPRATURE_SERV.toString(), "IRT Temperature Sensor");
        attributes.put(UUID_ACCELEROMETER_SERV.toString(), "Accelerometer");
        attributes.put(UUID_HUMIDITY_SERV.toString(), "Humidity Sensor");
        attributes.put(UUID_MAGNETIC_SERV.toString(), "Magnetometer");
        attributes.put(UUID_BAROMETER_SERV.toString(), "Barometric Pressure Sensor");
        attributes.put(UUID_GYROSCOPE_SERV.toString(), "Gyroscope");
        attributes.put(UUID_LUXOMETER_SERV.toString(), "Luxometer");
        attributes.put(UUID_MOTION_SERV.toString(), "Motion Sensor");
        attributes.put(UUID_KEY_SERV.toString(), "Keys");
        // Sample Characteristics.
        attributes.put(UUID_DEVINFO_FWREV.toString(), "Device Revision");
        attributes.put(UUID_IRT_TEMPRATURE_DATA.toString(), "Data");
        attributes.put(UUID_IRT_TEMPRATURE_CONF.toString(), "Configuration");
        attributes.put(UUID_IRT_TEMPRATURE_PERI.toString(), "Period");
        attributes.put(UUID_ACCELEROMETER_DATA.toString(), "Data");
        attributes.put(UUID_ACCELEROMETER_CONF.toString(), "Configuration");
        attributes.put(UUID_ACCELEROMETER_PERI.toString(), "Period");
        attributes.put(UUID_HUMIDITY_DATA.toString(), "Data");
        attributes.put(UUID_HUMIDITY_CONF.toString(), "Configuration");
        attributes.put(UUID_HUMIDITY_PERI.toString(), "Period");
        attributes.put(UUID_MAGNETIC_DATA.toString(), "Data");
        attributes.put(UUID_MAGNETIC_CONF.toString(), "Configuration");
        attributes.put(UUID_MAGNETIC_PERI.toString(), "Period");
        attributes.put(UUID_BAROMETER_DATA.toString(), "Data");
        attributes.put(UUID_BAROMETER_CONF.toString(), "Configuration");
        attributes.put(UUID_BAROMETER_CALI.toString(), "Calibration");
        attributes.put(UUID_BAROMETER_PERI.toString(), "Period");
        attributes.put(UUID_GYROSCOPE_DATA.toString(), "Data");
        attributes.put(UUID_GYROSCOPE_CONF.toString(), "Configuration");
        attributes.put(UUID_GYROSCOPE_PERI.toString(), "Period");
        attributes.put(UUID_LUXOMETER_DATA.toString(), "Data");
        attributes.put(UUID_LUXOMETER_CONF.toString(), "Configuration");
        attributes.put(UUID_LUXOMETER_PERI.toString(), "Period");
        attributes.put(UUID_MOTION_DATA.toString(), "Data");
        attributes.put(UUID_MOTION_PERI.toString(), "Period");
        attributes.put(UUID_MOTION_CONF.toString(), "Configuration");
        attributes.put(UUID_KEY_DATA.toString(), "Data");
        // Service UUID to configuration UUID map.
        serverToConfig.put(UUID_IRT_TEMPRATURE_SERV.toString(), UUID_IRT_TEMPRATURE_CONF.toString());
        serverToConfig.put(UUID_ACCELEROMETER_SERV.toString(), UUID_ACCELEROMETER_CONF.toString());
        serverToConfig.put(UUID_HUMIDITY_SERV.toString(), UUID_HUMIDITY_CONF.toString());
        serverToConfig.put(UUID_MAGNETIC_SERV.toString(), UUID_MAGNETIC_CONF.toString());
        serverToConfig.put(UUID_BAROMETER_SERV.toString(), UUID_BAROMETER_CONF.toString());
        serverToConfig.put(UUID_GYROSCOPE_SERV.toString(), UUID_GYROSCOPE_CONF.toString());
        serverToConfig.put(UUID_LUXOMETER_SERV.toString(), UUID_LUXOMETER_CONF.toString());
        serverToConfig.put(UUID_MOTION_SERV.toString(), UUID_MOTION_CONF.toString());
        // Service UUID to data UUID map.
        serverToData.put(UUID_IRT_TEMPRATURE_SERV.toString(), UUID_IRT_TEMPRATURE_DATA.toString());
        serverToData.put(UUID_ACCELEROMETER_SERV.toString(), UUID_ACCELEROMETER_DATA.toString());
        serverToData.put(UUID_HUMIDITY_SERV.toString(), UUID_HUMIDITY_DATA.toString());
        serverToData.put(UUID_MAGNETIC_SERV.toString(), UUID_MAGNETIC_DATA.toString());
        serverToData.put(UUID_BAROMETER_SERV.toString(), UUID_BAROMETER_DATA.toString());
        serverToData.put(UUID_GYROSCOPE_SERV.toString(), UUID_GYROSCOPE_DATA.toString());
        serverToData.put(UUID_LUXOMETER_SERV.toString(), UUID_LUXOMETER_DATA.toString());
        serverToData.put(UUID_MOTION_SERV.toString(), UUID_MOTION_DATA.toString());
        serverToData.put(UUID_KEY_SERV.toString(), UUID_KEY_DATA.toString());
        // Service UUID to period UUID map.
        serverToPeriod.put(UUID_IRT_TEMPRATURE_SERV.toString(), UUID_IRT_TEMPRATURE_PERI.toString());
        serverToPeriod.put(UUID_ACCELEROMETER_SERV.toString(), UUID_ACCELEROMETER_PERI.toString());
        serverToPeriod.put(UUID_HUMIDITY_SERV.toString(), UUID_HUMIDITY_PERI.toString());
        serverToPeriod.put(UUID_MAGNETIC_SERV.toString(), UUID_MAGNETIC_PERI.toString());
        serverToPeriod.put(UUID_BAROMETER_SERV.toString(), UUID_BAROMETER_PERI.toString());
        serverToPeriod.put(UUID_GYROSCOPE_SERV.toString(), UUID_GYROSCOPE_PERI.toString());
        serverToPeriod.put(UUID_LUXOMETER_SERV.toString(), UUID_LUXOMETER_PERI.toString());
        serverToPeriod.put(UUID_MOTION_SERV.toString(), UUID_MOTION_PERI.toString());
    }

    /**
     * Gives the name of the service corresponding to the input service UUID.
     */
    public static String lookup(String serviceUuid, String defaultName) {
        String name = attributes.get(serviceUuid);
        return name == null ? defaultName : name;
    }

    /**
     * Gives the configure UUID corresponding to the input service UUID (as String).
     */
    public static String configCharacteristicsOfService(String serviceUuid, String defaultName) {
        String name = serverToConfig.get(serviceUuid);
        return name == null ? defaultName : name;
    }

    /**
     * Gives the data UUID corresponding to the input service UUID (as String).
     */
    public static String dataCharacteristicsOfService(String serviceUuid, String defaultName) {
        String name = serverToData.get(serviceUuid);
        return name == null ? defaultName : name;
    }

    /**
     * Gives the period UUID corresponding to the input service UUID (as String).
     */
    public static String periodCharacteristicsOfService(String serviceUuid, String defaultName) {
        String name = serverToPeriod.get(serviceUuid);
        return name == null ? defaultName : name;
    }

    /**
     * Gives the optimal period for the input service.
     */
    public static byte optimalPeriod(String serviceUuid) {
        if (serviceUuid.equals(UUID_IRT_TEMPRATURE_SERV)) return (byte) Default_Period_Value;
        else if (serviceUuid.equals(UUID_HUMIDITY_SERV)) return (byte) Default_Period_Value;
        else if (serviceUuid.equals(UUID_BAROMETER_SERV)) return (byte) Default_Period_Value;
        else if (serviceUuid.equals(UUID_LUXOMETER_SERV)) return (byte) Default_Period_Value;
        else if (serviceUuid.equals(UUID_MOTION_SERV)) return (byte) Default_Period_Value;
        else return (byte) Default_Period_Value;
    }

    /**
     * Checks whether the input UUID corresponds to a service the user can ask for.
     */
    public static boolean validService(String uuid) {
        if (uuid.equals(UUID_IRT_TEMPRATURE_SERV)) return true;
        else if (uuid.equals(UUID_HUMIDITY_SERV)) return true;
        else if (uuid.equals(UUID_BAROMETER_SERV)) return true;
        else if (uuid.equals(UUID_LUXOMETER_SERV)) return true;
        else if (uuid.equals(UUID_MOTION_SERV)) return true;
        return false;
    }
}
