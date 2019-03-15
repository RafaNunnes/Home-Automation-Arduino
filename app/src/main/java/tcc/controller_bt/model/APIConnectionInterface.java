package tcc.controller_bt.model;

import android.content.Intent;

import java.util.ArrayList;

public interface APIConnectionInterface {
    void connect(String address);

    void disconnect();

    void sendData(byte data);

    void sendData(String data);

    int receiveData();

    public ArrayList<Intent> verifyBluetoothStatus();

}
