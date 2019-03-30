package tcc.controller_bt.model;

public class SwitchButton extends DeviceControlButton {
    public SwitchButton(long id, String name, byte type, byte port){
        setControlType(type);
        setLogicalPort(port);
        setName(name);
        this.id = id;
    }

    @Override
    public ButtonViewFactory getFactory() {
        return new SwitchButtonFactory(SwitchButton.this);
    }
}
