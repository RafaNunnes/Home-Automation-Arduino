package tcc.controller_bt.model;

import java.io.Serializable;

public abstract class DeviceControlButton implements Serializable {
    public final static int SWITCH_TYPE = 1;
    public final static int DIMMER_TYPE = 2;
    public final static int INFRARED_TYPE = 3;

    protected long id;
    protected String name_button;
    protected byte control_type;
    protected byte logical_port;

    public String getName(){
        return name_button;
    }

    public void setName(String name){
        this.name_button = name;
    }

    public long getId(){
        return id;
    }

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

    public abstract ButtonViewFactory getFactory();
}
