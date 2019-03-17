package tcc.controller_bt.model;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import tcc.controller_bt.view.DevicesList;


public class BluetoothManagerAdapter implements APIConnectionInterface {

    private BluetoothAdapter bluetooth_adapter = null;
    private BluetoothSocket bluetooth_socket = null;
    private ConnectedThread thread_connection_bluetooth;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static String mac_address = null;

    private Activity room_activity;

    public BluetoothManagerAdapter(Activity room_activity){
        this.room_activity = room_activity;
    }

    @Override
    public void connect(String address) {
        mac_address = address;

        BluetoothDevice device = bluetooth_adapter.getRemoteDevice(mac_address);

        //Cria uma conexão de saída para o dispositivo usando o serviço UUID
        try{
            bluetooth_socket = device.createInsecureRfcommSocketToServiceRecord(BTMODULEUUID);
        }catch (IOException e) {
            Toast.makeText(room_activity.getApplicationContext(), "A criação do socket falhou", Toast.LENGTH_LONG).show();
            //connect(mac_address);
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
                //connect(mac_address);
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
                Toast.makeText(room_activity.getApplicationContext(), "Não foi possível fechar o socket", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(room_activity.getApplicationContext(), "A conexão falhou", Toast.LENGTH_LONG).show();
                    //connect();
                }
            }
        } else {
            Toast.makeText(room_activity.getApplicationContext(), "Bluetooth Desconectado", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(room_activity.getApplicationContext(), "A conexão falhou", Toast.LENGTH_LONG).show();
                    //connect();
                }
            }
        } else {
            Toast.makeText(room_activity.getApplicationContext(), "Bluetooth Desconectado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int receiveData() {
        //TODO

        return 0;
    }

    public void verifyConnectionStatus() {
        bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetooth_adapter==null) {
            //Toast.makeText(getBaseContext(), "O dispositivo não suporta bluetooth", Toast.LENGTH_LONG).show();
            Toast.makeText(room_activity.getApplicationContext(), "O dispositivo não suporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            //Inicia a activity DevicesList para escolher o dispositivo que se deseja criar conexão
            Intent intent = new Intent(room_activity.getApplicationContext(), DevicesList.class);
            room_activity.startActivityForResult(intent,STATUS_CONNECTION);

            if (bluetooth_adapter.isEnabled()) {
                Toast.makeText(room_activity.getApplicationContext(), "Bluetooth está ativado", Toast.LENGTH_LONG).show();
            } else {
                Intent enable_bluetooth_intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //room_activity.startActivityForResult(enable_bluetooth_intent, 1);
                room_activity.startActivity(enable_bluetooth_intent);
            }
        }
    }
}


