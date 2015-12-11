package com.example.android.bluetoothlegatt.BLEServices.SensorFragments.AbstractSensor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.widget.NumberPicker;

import com.example.android.bluetoothlegatt.BLEServices.BleSensor.Implementation.bleSensorAbstract.BleGenericSensor;
import com.example.android.bluetoothlegatt.R;
import com.example.android.bluetoothlegatt.SensorTagGatt;

public abstract class AbstractSensor extends Fragment implements SensorDataListerner {

    protected BleGenericSensor bleGenericSensor;
    protected int selectedPeriod = SensorTagGatt.Default_Period_Value;

    public void setBleGenericSensor(BleGenericSensor bleGenericSensor) {
        this.bleGenericSensor = bleGenericSensor;
    }

    protected void showPeriodSelectorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.period_dialog_text));
        builder.setCancelable(false);
        final NumberPicker numberPicker = new NumberPicker(getActivity());
        numberPicker.setMaxValue(2000);
        numberPicker.setMinValue(10);
        numberPicker.setValue(selectedPeriod);

        builder.setView(numberPicker);
        builder.setPositiveButton(getResources().getString(R.string.dialog_text_oky), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedPeriod = numberPicker.getValue();
                bleGenericSensor.setPeriod(selectedPeriod);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.dialog_text_cancel), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
