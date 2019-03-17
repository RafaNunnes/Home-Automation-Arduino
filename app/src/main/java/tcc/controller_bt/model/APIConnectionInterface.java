package tcc.controller_bt.model;

import android.os.Parcelable;

import java.io.Serializable;

public interface APIConnectionInterface {
    public static String EXTRA_DEVICE_DATA = "device_data";
    public static int STATUS_CONNECTION = 1;

    public void connect(String connection_content);

    public void disconnect();

    public void sendData(byte data);

    public void sendData(String data);

    public int receiveData();

    public void verifyConnectionStatus();
}
