package tcc.controller_bt.model;

import android.content.Context;
import android.view.View;
import android.widget.Button;

public class SwitchButton extends DeviceControlButton {

    public SwitchButton(String name, byte type, byte port){
        setControlType(type);
        setLogicalPort(port);
        this.name_button = name;
    }

    @Override
    public View generateControlButton(Context context, final APIConnectionInterface manager_connection) {
        final Button new_button = new Button(context);

        new_button.setText(name_button);
        new_button.setTag(1);

        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager_connection.sendData(getControlType());
                manager_connection.sendData(getLogicalPort());
            }
        });

        return new_button;
    }
}
