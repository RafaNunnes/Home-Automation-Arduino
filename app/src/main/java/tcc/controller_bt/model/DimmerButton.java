package tcc.controller_bt.model;

public class DimmerButton extends DeviceControlButton {
    private byte current_progress_value;

    public DimmerButton(long id, String name, byte type, byte port){
        setControlType(type);
        setLogicalPort(port);
        this.name_button = name;
        this.id = id;
    }


    @Override
    public ButtonViewFactory getFactory() {
        return new DimmerButtonFactory(DimmerButton.this);
    }

    public byte getCurrentProgressValue() {
        return current_progress_value;
    }

    public void setCurrentProgressValue(byte current_progress_value) {
        this.current_progress_value = current_progress_value;
    }
}
