package tcc.controller_bt.model;

public interface LayoutCreatorAdapter {
    public void generateLayout();

    public void sendNewButton();

    public boolean updateButton(DeviceControlButton control_button);
}
