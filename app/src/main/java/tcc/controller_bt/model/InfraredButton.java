package tcc.controller_bt.model;

public class InfraredButton extends DeviceControlButton {
    private String infrared_code;
    private byte format_type;
    private byte num_bits;

    public InfraredButton(long id, String name, byte type, byte port, String code, byte format, byte bits){
        setControlType(type);
        setLogicalPort(port);
        setInfraredCode(code);
        setFormatType(format);
        setNumBits(bits);
        this.name_button = name;
        this.id = id;
    }

    @Override
    public ButtonViewFactory getFactory() {
        return new InfraredButtonFactory(InfraredButton.this);
    }

    public String getInfraredCode() {
        return infrared_code;
    }

    public void setInfraredCode(String infrared_code) {
        this.infrared_code = infrared_code;
    }

    public byte getFormatType() {
        return format_type;
    }

    public void setFormatType(byte format_type) {
        this.format_type = format_type;
    }

    public byte getNumBits() {
        return num_bits;
    }

    public void setNumBits(byte num_bits) {
        this.num_bits = num_bits;
    }
}
