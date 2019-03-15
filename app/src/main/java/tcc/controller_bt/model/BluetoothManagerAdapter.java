package tcc.controller_bt.model;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import tcc.controller_bt.controller.DevicesList;


public class BluetoothManagerAdapter extends Activity implements APIConnectionInterface {

    private BluetoothAdapter bluetooth_adapter = null;
    private BluetoothSocket bluetooth_socket = null;
    private ConnectedThread thread_connection_bluetooth;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private static String mac_address = null;

    private Context room_context;

    public BluetoothManagerAdapter(Context room_context){
        this.room_context = room_context.getApplicationContext();
    }

    /*@Override
    public void connect() {
        bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();
        verifyBluetoothStatus();

        Intent intent = new Intent(room_context, DevicesList.class);
        startActivityForResult(intent,1);
    }*/

    public void connect(String address){
        mac_address = address;

        BluetoothDevice device = bluetooth_adapter.getRemoteDevice(mac_address);

        //Cria uma conexão de saída para o dispositivo usando o serviço UUID
        try{
            bluetooth_socket = device.createInsecureRfcommSocketToServiceRecord(BTMODULEUUID);
        }catch (IOException e) {
            Toast.makeText(room_context, "A criação do socket falhou", Toast.LENGTH_LONG).show();
            //connect();
        }

        //Estabelece a conexão com o Socket
        try
        {
            bluetooth_socket.connect();
        } catch (IOException e) {
            try {
                bluetooth_socket.close();
            } catch (IOException e2) {

            }finally {
                //connect();
            }
        }

        thread_connection_bluetooth = new ConnectedThread(bluetooth_socket);
        thread_connection_bluetooth.start();
    }

    @Override
    public void disconnect() {
        if (bluetooth_socket != null)
        {
            try {
                bluetooth_socket.close();
            }
            catch (IOException e) {
                Toast.makeText(room_context, "Não foi possível fechar o socket", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void sendData(byte data) {
        if(bluetooth_socket != null) {
            if(bluetooth_socket.isConnected()){
                try {
                    thread_connection_bluetooth.write(data);
                }catch (IOException e)
                {
                    //Toast.makeText(getBaseContext(), "A conexão falhou", Toast.LENGTH_LONG).show();
                    Toast.makeText(room_context, "A conexão falhou", Toast.LENGTH_LONG).show();
                    //connect();
                }
            }
        } else {
            Toast.makeText(room_context, "Bluetooth Desconectado", Toast.LENGTH_LONG).show();
        }
    }

    public void sendData( String infrared_code) {
        if(bluetooth_socket != null) {
            if(bluetooth_socket.isConnected()){
                try {
                    thread_connection_bluetooth.write(infrared_code);
                }catch (IOException e)
                {
                    //Toast.makeText(getBaseContext(), "A conexão falhou", Toast.LENGTH_LONG).show();
                    Toast.makeText(room_context, "A conexão falhou", Toast.LENGTH_LONG).show();
                    //connect();
                }
            }
        } else {
            Toast.makeText(room_context, "Bluetooth Desconectado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int receiveData() {
        //TODO

        return 0;
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle chosen_address = data.getExtras();
        mac_address = chosen_address.getString(EXTRA_DEVICE_ADDRESS);

        BluetoothDevice device = bluetooth_adapter.getRemoteDevice(mac_address);

        //Cria uma conexão de saída para o dispositivo usando o serviço UUID
        try{
            bluetooth_socket = device.createInsecureRfcommSocketToServiceRecord(BTMODULEUUID);
        }catch (IOException e) {
            Toast.makeText(room_context, "A criação do socket falhou", Toast.LENGTH_LONG).show();
            connect();
        }

        //Estabelece a conexão com o Socket
        try
        {
            bluetooth_socket.connect();
        } catch (IOException e) {
            try {
                bluetooth_socket.close();
            } catch (IOException e2) {

            }finally {
                connect();
            }
        }

        thread_connection_bluetooth = new ConnectedThread(bluetooth_socket);
        thread_connection_bluetooth.start();

    }

    private void verifyBluetoothStatus() {
        if(bluetooth_adapter==null) {
            //Toast.makeText(getBaseContext(), "O dispositivo não suporta bluetooth", Toast.LENGTH_LONG).show();
            Toast.makeText(room_context, "O dispositivo não suporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (bluetooth_adapter.isEnabled()) {
                Toast.makeText(room_context, "Bluetooth está ativado", Toast.LENGTH_LONG).show();
            } else {
                Intent enable_bluetooth_intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enable_bluetooth_intent, 1);
            }
        }
    }*/

    public ArrayList<Intent> verifyBluetoothStatus() {
        Intent devices_list_intent = new Intent(room_context, DevicesList.class);
        ArrayList<Intent> intents = new ArrayList<Intent>();

        bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetooth_adapter==null) {
            //Toast.makeText(getBaseContext(), "O dispositivo não suporta bluetooth", Toast.LENGTH_LONG).show();
            Toast.makeText(room_context, "O dispositivo não suporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            intents.add(devices_list_intent);

            if (bluetooth_adapter.isEnabled()) {
                //Toast.makeText(room_context, "Bluetooth está ativado", Toast.LENGTH_LONG).show();
            } else {
                Intent enable_bluetooth_intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //startActivityForResult(enable_bluetooth_intent, 1);
                intents.add(enable_bluetooth_intent);
            }
        }

        return intents;
    }
}


