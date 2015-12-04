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
    @Bind(R.id.txt_berometer)
    TextView txtViewBerometer;
    @Bind(R.id.txt_humidity)
    TextView txtViewHumidity;
    @Bind(R.id.txt_irt)
    TextView txtViewIrt;
    @Bind(R.id.txt_keys)
    TextView txtViewKeys;
    @Bind(R.id.txt_motion)
    TextView txtViewMotion;
    @Bind(R.id.txt_luxometer)
    TextView txtViewLuxometer;
    HashMap<String, BleGenericSensor> stringBleGenericSensorHashMap = new HashMap<>();
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

    //    public void onEvent(BleEvents event) {
//
//    }
    private boolean mConnected = false;
    private HashMap<String, BluetoothGattCharacteristic> charList = new HashMap<>();

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
            case DataAvailable:
                displayData(event.getData(), event.getUuid());
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

        BleGenericSensor bleGenericSensor = stringBleGenericSensorHashMap.get(uuid);
        if (bleGenericSensor != null) {
            bleGenericSensor.convert(data);
            bleGenericSensor.receiveNotification();
            if (bleGenericSensor instanceof BarometerSensor) {
                txtViewBerometer.setText(bleGenericSensor.toString());
            } else if (bleGenericSensor instanceof IRTSensor) {
                txtViewIrt.setText(bleGenericSensor.toString());
            } else if (bleGenericSensor instanceof LuxometerSensor) {
                txtViewLuxometer.setText(bleGenericSensor.toString());
            } else if (bleGenericSensor instanceof MotionSensor) {
                txtViewMotion.setText(bleGenericSensor.toString());
            } else if (bleGenericSensor instanceof SensorTagHumidityProfile) {
                txtViewHumidity.setText(bleGenericSensor.toString());
            } else if (bleGenericSensor instanceof SimpleKeysSensor) {
                txtViewKeys.setText(bleGenericSensor.toString());
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
