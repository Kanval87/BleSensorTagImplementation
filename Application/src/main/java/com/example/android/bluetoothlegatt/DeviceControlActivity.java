/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.bluetoothlegatt.BLEServices.BleGenericSensor;
import com.example.android.bluetoothlegatt.BLEServices.Implementation.BarometerSensor;
import com.example.android.bluetoothlegatt.BLEServices.Implementation.IRTSensor;
import com.example.android.bluetoothlegatt.BLEServices.Implementation.LuxometerSensor;
import com.example.android.bluetoothlegatt.BLEServices.Implementation.MotionSensor;
import com.example.android.bluetoothlegatt.BLEServices.Implementation.SensorTagHumidityProfile;
import com.example.android.bluetoothlegatt.BLEServices.Implementation.SimpleKeysSensor;
import com.example.android.bluetoothlegatt.events.BleEvents;
import com.example.android.bluetoothlegatt.utils.Point3D;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE = "DEVICE";
    private final static String TAG = DeviceControlActivity.class.getSimpleName();
    private final int TotalSize = 6;
    @Bind(R.id.txt_humidity)
    TextView txtViewHumidity;
    @Bind(R.id.txt_keys)
    TextView txtViewKeys;
    @Bind(R.id.txt_motion)
    TextView txtViewMotion;
    @Bind(R.id.txt_luxometer)
    TextView txtViewLuxometer;
    @Bind(R.id.cubiclinechart_berometer)
    ValueLineChart valueBarometerLineChart;
    @Bind(R.id.cubiclinechart_irt)
    ValueLineChart valueIrtLineChart;
    HashMap<String, BleGenericSensor> stringBleGenericSensorHashMap = new HashMap<>();
    ValueLineSeries beroSeries = new ValueLineSeries();
    ValueLineSeries irtSeries = new ValueLineSeries();
    private EventBus bus = EventBus.getDefault();
    private TextView mConnectionState;
    private String mDeviceName;
    private BluetoothLeService mBluetoothLeService;
    private BluetoothDevice bluetoothDevice;
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(bluetoothDevice.getAddress());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    private boolean mConnected = false;
    private HashMap<String, BluetoothGattCharacteristic> charList = new HashMap<>();

    public void onEvent(BleEvents event) {

        switch (event.getBleEnum()) {
            case DataAvailable:
                displayData(event.getData(), event.getUuid());
                break;
        }

    }

    public void onEventMainThread(BleEvents event) {
        switch (event.getBleEnum()) {
            case Connected:
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
                break;
            case Disconnected:
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                break;
            case ServicesDiscovered:
                displayGattServices();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        ButterKnife.bind(this);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        bluetoothDevice = intent.getParcelableExtra(EXTRAS_DEVICE);

        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(bluetoothDevice.getAddress());
        mConnectionState = (TextView) findViewById(R.id.connection_state);

        beroSeries.setColor(0xFF56B7F1);
        irtSeries.setColor(0x5556B7F1);
        for (int i = 0; i < TotalSize; i++) {
            ValueLinePoint valueLinePoint = new ValueLinePoint("Bero", 0f);
            beroSeries.addPoint(valueLinePoint);
            irtSeries.addPoint(valueLinePoint);
        }

        valueBarometerLineChart.setIndicatorTextUnit("rh");
        valueBarometerLineChart.addSeries(beroSeries);
        valueBarometerLineChart.startAnimation();

        valueIrtLineChart.setIndicatorTextUnit("C");
        valueIrtLineChart.addSeries(irtSeries);
        valueIrtLineChart.startAnimation();

        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(bluetoothDevice.getAddress());
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(bluetoothDevice.getAddress());
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }


    private void displayData(byte[] data, String uuid) {

        final BleGenericSensor bleGenericSensor = stringBleGenericSensorHashMap.get(uuid);
        if (bleGenericSensor != null) {
            Point3D point3D = bleGenericSensor.convert(data);
            bleGenericSensor.receiveNotification();
            if (bleGenericSensor instanceof BarometerSensor) {
                ValueLineSeries lineSeries = valueBarometerLineChart.getDataSeries().get(0);
                List<ValueLinePoint> valueLinePoints = lineSeries.getSeries();
                valueLinePoints.remove(0);
                valueLinePoints.add(new ValueLinePoint("bero", (float) point3D.x));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        valueBarometerLineChart.update();
                    }
                });
            } else if (bleGenericSensor instanceof IRTSensor) {
                ValueLineSeries lineSeries = valueIrtLineChart.getDataSeries().get(0);
                List<ValueLinePoint> valueLinePoints = lineSeries.getSeries();
                valueLinePoints.remove(0);
                valueLinePoints.add(new ValueLinePoint("Ambient", (float) point3D.z));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        valueIrtLineChart.update();
                    }
                });
            } else if (bleGenericSensor instanceof LuxometerSensor) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtViewLuxometer.setText(bleGenericSensor.toString());
                    }
                });

            } else if (bleGenericSensor instanceof MotionSensor) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtViewMotion.setText(bleGenericSensor.toString());
                    }
                });

            } else if (bleGenericSensor instanceof SensorTagHumidityProfile) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtViewHumidity.setText(bleGenericSensor.toString());
                    }
                });

            } else if (bleGenericSensor instanceof SimpleKeysSensor) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtViewKeys.setText(bleGenericSensor.toString());
                    }
                });

            }
//            Log.d(TAG, "Data : " + bleGenericSensor.toString());
        } else {
            Log.d(TAG, "None Matched");
        }

    }


    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.

    private void displayGattServices() {
        final List<BluetoothGattService> gattServices = mBluetoothLeService.getSupportedGattServices();
        if (gattServices == null) return;

        if (gattServices.size() > 0) {
            for (int ii = 0; ii < gattServices.size(); ii++) {
                BluetoothGattService s = gattServices.get(ii);
                List<BluetoothGattCharacteristic> c = s.getCharacteristics();
                if (c.size() > 0) {
                    for (int jj = 0; jj < c.size(); jj++) {
                        charList.put(c.get(jj).getUuid().toString(), c.get(jj));
                    }
                }
            }
        }

        BleGenericSensor bleGenericSensor;
        for (int i = 0; i < gattServices.size(); i++) {
            BluetoothGattService bluetoothGattService = gattServices.get(i);
            boolean result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_HUM_SERV.toString());
            if (result) {
                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
                bleGenericSensor = new SensorTagHumidityProfile(bluetoothGattService.getUuid(), mBluetoothLeService);
                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
            }
            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_IRT_SERV.toString());
            if (result) {
                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
                bleGenericSensor = new IRTSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
            }
            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_BAR_SERV.toString());
            if (result) {
                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
                bleGenericSensor = new BarometerSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
            }
            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_MOV_SERV.toString());
            if (result) {
                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
                bleGenericSensor = new MotionSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
            }
            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_KEY_SERV.toString());
            if (result) {
                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
                bleGenericSensor = new SimpleKeysSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
            }

        }
    }


}
