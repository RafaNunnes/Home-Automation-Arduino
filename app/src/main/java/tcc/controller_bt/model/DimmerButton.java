package tcc.controller_bt.model;

import android.content.Context;
import android.view.View;
import android.widget.SeekBar;

public class DimmerButton extends DeviceControlButton {
    private byte current_progress_value;

    public DimmerButton(String name, byte type, byte port){
        setControlType(type);
        setLogicalPort(port);
        this.name_button = name;
    }

    public byte getCurrentProgressValue() {
        return current_progress_value;
    }

    public void setCurrentProgressValue(byte current_progress_value) {
        this.current_progress_value = current_progress_value;
    }

    @Override
    public View generateControlButton(Context context, final APIConnectionInterface manager_connection) {
        final SeekBar new_button = new SeekBar(context);
        new_button.setTag(1);

        new_button.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                manager_connection.sendData((byte) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                manager_connection.sendData(getControlType());
                manager_connection.sendData(getLogicalPort());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                manager_connection.sendData((byte) -1);
            }
        });

        return new_button;
    }
}
