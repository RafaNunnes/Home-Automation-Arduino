package tcc.controller_bt.model;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

public abstract class DeviceControlButton implements Serializable {
    protected byte control_type;
    protected byte logical_port;
    protected String name_button;

    protected void setControlType(byte control_type) {
        this.control_type = control_type;
    }

    protected void setLogicalPort(byte logical_port) {
        this.logical_port = logical_port;
    }

    public byte getControlType() {
        return control_type;
    }

    public byte getLogicalPort() {
        return logical_port;
    }

    public abstract View generateControlButton(Context context, final APIConnectionInterface manager_connection);
}
