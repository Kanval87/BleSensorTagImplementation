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
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.example.android.bluetoothlegatt.BLEServices.BleGenericSensor;
import com.example.android.bluetoothlegatt.BLEServices.SimpleKeysSensor;
import com.example.android.bluetoothlegatt.events.BleEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    private EventBus bus = EventBus.getDefault();

    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_DEVICE = "DEVICE";

    private TextView mConnectionState;
    private TextView mDataField;
    private String mDeviceName;

    private ExpandableListView mGattServicesList;

    private BluetoothLeService mBluetoothLeService;
    private BluetoothDevice bluetoothDevice;

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;


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

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(intent);
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA), intent.getStringExtra(BluetoothLeService.EXTRA_UUID));
            }
        }
    };


    public void onEvent(BleEvents event) {
        Log.d(TAG, event.toString());
    }


    // If a given GATT characteristic is selected, check for supported features.  This sample
    // demonstrates 'Read' and 'Notify' features.  See
    // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
    // list of supported characteristic features.
    private final ExpandableListView.OnChildClickListener servicesListClickListner = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            if (mGattCharacteristics != null) {
                final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(groupPosition).get(childPosition);
                final int charaProp = characteristic.getProperties();
                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    // If there is an active notification on a characteristic, clear
                    // it first so it doesn't update the data field on the user interface.
                    if (mNotifyCharacteristic != null) {
                        mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
                        mNotifyCharacteristic = null;
                    }
                    mBluetoothLeService.readCharacteristic(characteristic);
                }
                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    mNotifyCharacteristic = characteristic;
                    mBluetoothLeService.setCharacteristicNotification(characteristic, true);
                }
                return true;
            }
            return false;
        }
    };
    private Context context = DeviceControlActivity.this;
    private HashMap<String, BluetoothGattCharacteristic> charList = new HashMap<>();


    private void clearUI() {
        mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        mDataField.setText(R.string.no_data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        bluetoothDevice = intent.getParcelableExtra(EXTRAS_DEVICE);

        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(bluetoothDevice.getAddress());
        mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);
        mGattServicesList.setOnChildClickListener(servicesListClickListner);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        mDataField = (TextView) findViewById(R.id.data_value);

        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
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
        unregisterReceiver(mGattUpdateReceiver);
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
            Log.d(TAG, "Data : " + bleGenericSensor.toString());
        } else {
            Log.d(TAG, "None Matched");
        }

    }

    HashMap<String, BluetoothGattCharacteristic> bluetoothGattCharacteristicHashMap = new HashMap<String, BluetoothGattCharacteristic>();
    HashMap<String, BleGenericSensor> stringBleGenericSensorHashMap = new HashMap<>();

//    private ArrayList<BluetoothGattCharacteristic> charList = new ArrayList<BluetoothGattCharacteristic>();

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.

    private void displayGattServices(Intent intent) {
        final List<BluetoothGattService> gattServices = mBluetoothLeService.getSupportedGattServices();
        if (gattServices == null) return;

//        String uuid = null;
//        String unknownServiceString = getResources().getString(R.string.unknown_service);
//        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
//        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
//        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();

//        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

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

//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
        BleGenericSensor bleGenericSensor;
        for (int i = 0; i < gattServices.size(); i++) {
            BluetoothGattService bluetoothGattService = gattServices.get(i);
            boolean result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_HUM_SERV.toString());
//            if (result) {
//                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
//                bleGenericSensor = new SensorTagHumidityProfile(bluetoothGattService.getUuid(), mBluetoothLeService);
//                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
//            }
//            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_IRT_SERV.toString());
//            if (result) {
//                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
//                bleGenericSensor = new IRTSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
//                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
//            }
//            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_BAR_SERV.toString());
//            if (result) {
//                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
//                bleGenericSensor = new BarometerSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
//                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
//            }
//            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_MOV_SERV.toString());
//            if (result) {
//                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
//                bleGenericSensor = new MotionSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
//                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
//            }
            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_KEY_SERV.toString());
            if (result) {
                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
                bleGenericSensor = new SimpleKeysSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
            }

        }

//        Executors.newSingleThreadExecutor().submit(runnable);
        // Loops through available GATT Services.

    }


    private void waitForResponse() {
        synchronized (mBluetoothLeService.getSyncObject()) {
            try {
                Log.d(TAG, "going to wait");
                mBluetoothLeService.getSyncObject().wait();
                Log.d(TAG, "Waiting Done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_WRITE);
        return intentFilter;
    }
}
