package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.example.android.bluetoothlegatt.BLEServices.SensorFragments.AbstractSensor.AbstractSensor;
import com.example.android.bluetoothlegatt.BLEServices.SensorFragments.BarometerFragment;
import com.example.android.bluetoothlegatt.BLEServices.SensorFragments.HumidityFragment;
import com.example.android.bluetoothlegatt.BLEServices.SensorFragments.IrtFragment;
import com.example.android.bluetoothlegatt.BLEServices.SensorFragments.KeyFragment;
import com.example.android.bluetoothlegatt.BLEServices.SensorFragments.LuxometerFragment;
import com.example.android.bluetoothlegatt.BLEServices.SensorFragments.MotionFragment;
import com.example.android.bluetoothlegatt.events.BleEvents;
import com.example.android.bluetoothlegatt.utils.Point3D;

import java.util.HashMap;
import java.util.List;

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
    public static final int TotalSize = 6;
    private final static String TAG = DeviceControlActivity.class.getSimpleName();


    HashMap<String, BleGenericSensor> stringBleGenericSensorHashMap = new HashMap<>();
    //    ValueLineSeries beroSeries = new ValueLineSeries();
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
    private MotionFragment motionFragment;
    private HumidityFragment humidityFragment;
    private KeyFragment keyFragment;
    private LuxometerFragment luxometerFragment;
    private AbstractSensor beroMeterSensorFragment, irtFragment;
    private ProgressDialog mProgressBar;

    public void onEventBackgroundThread(BleEvents event) {

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

        mProgressBar = new ProgressDialog(DeviceControlActivity.this);
        mProgressBar.setMessage("Discovering Services...");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mProgressBar.show();

        ButterKnife.bind(this);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        bluetoothDevice = intent.getParcelableExtra(EXTRAS_DEVICE);

        // Sets up UI references.
        ((TextView) findViewById(R.id.device_address)).setText(bluetoothDevice.getAddress());
        mConnectionState = (TextView) findViewById(R.id.connection_state);


        beroMeterSensorFragment = (BarometerFragment) getFragmentManager().findFragmentById(R.id.berometer_fragment);
        irtFragment = (IrtFragment) getFragmentManager().findFragmentById(R.id.irt_fragment);
        motionFragment = (MotionFragment) getFragmentManager().findFragmentById(R.id.motion_fragment);
        humidityFragment = (HumidityFragment) getFragmentManager().findFragmentById(R.id.humidity_fragment);
        keyFragment = (KeyFragment) getFragmentManager().findFragmentById(R.id.key_fragment);
        luxometerFragment = (LuxometerFragment) getFragmentManager().findFragmentById(R.id.luxometer_fragment);


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
            Point3D point3D = null;
            bleGenericSensor.receiveNotification();
            if (bleGenericSensor instanceof BarometerSensor) {
                point3D = bleGenericSensor.convert(data);
                beroMeterSensorFragment.displayData(point3D);
            } else if (bleGenericSensor instanceof IRTSensor) {
                point3D = bleGenericSensor.convert(data);
                irtFragment.displayData(point3D);
            } else if (bleGenericSensor instanceof LuxometerSensor) {
                point3D = bleGenericSensor.convert(data);
                luxometerFragment.displayData(point3D);
            } else if (bleGenericSensor instanceof MotionSensor) {
                Point3D[] point3Ds = ((MotionSensor) bleGenericSensor).convertForArray(data);
                motionFragment.displayData(point3Ds);
            } else if (bleGenericSensor instanceof SensorTagHumidityProfile) {
                point3D = bleGenericSensor.convert(data);
                humidityFragment.displayData(point3D);
            } else if (bleGenericSensor instanceof SimpleKeysSensor) {
                point3D = bleGenericSensor.convert(data);
                keyFragment.displayData(point3D);
            }
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
            boolean result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_HUMIDITY_SERV.toString());
            if (result) {
                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
                bleGenericSensor = new SensorTagHumidityProfile(bluetoothGattService.getUuid(), mBluetoothLeService);
                humidityFragment.setBleGenericSensor(bleGenericSensor);
                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
            }
            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_IRT_TEMPRATURE_SERV.toString());
            if (result) {
                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
                bleGenericSensor = new IRTSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
                irtFragment.setBleGenericSensor(bleGenericSensor);
                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
            }
            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_BAROMETER_SERV.toString());
            if (result) {
                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
                bleGenericSensor = new BarometerSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
                beroMeterSensorFragment.setBleGenericSensor(bleGenericSensor);
                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
            }
            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_MOTION_SERV.toString());
            if (result) {
                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
                bleGenericSensor = new MotionSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
                motionFragment.setBleGenericSensor(bleGenericSensor);
                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
            }
            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_KEY_SERV.toString());
            if (result) {
                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
                bleGenericSensor = new SimpleKeysSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
                keyFragment.setBleGenericSensor(bleGenericSensor);
                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
            }
            result = bluetoothGattService.getUuid().toString().contentEquals(SensorTagGatt.UUID_LUXOMETER_SERV.toString());
            if (result) {
                Log.d(TAG, "Service :" + bluetoothGattService.getUuid().toString());
                bleGenericSensor = new LuxometerSensor(bluetoothGattService.getUuid(), mBluetoothLeService);
                luxometerFragment.setBleGenericSensor(bleGenericSensor);
                stringBleGenericSensorHashMap.put(bluetoothGattService.getUuid().toString(), bleGenericSensor);
            }
        }

        mProgressBar.dismiss();
    }


}
