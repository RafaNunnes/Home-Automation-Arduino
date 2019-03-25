package tcc.controller_bt.model;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class DimmerButton extends DeviceControlButton {
    private byte current_progress_value;

    public DimmerButton(long id, String name, byte type, byte port){
        setControlType(type);
        setLogicalPort(port);
        this.name_button = name;
        this.id = id;
    }

    public byte getCurrentProgressValue() {
        return current_progress_value;
    }

    public void setCurrentProgressValue(byte current_progress_value) {
        this.current_progress_value = current_progress_value;
    }

    @Override
    public View generateControlButton(Activity activity, final APIConnectionInterface manager_connection, ViewGroup room_screen_layout) {
        final SeekBar new_button = new SeekBar(activity.getApplicationContext());
        new_button.setTag(getId());
        new_button.setMax(120);

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
