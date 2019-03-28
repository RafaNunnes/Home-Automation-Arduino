package tcc.controller_bt.model;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import tcc.controller_bt.view.DevicesList;


public class BluetoothManagerAdapter implements APIConnectionInterface {
    private static BluetoothManagerAdapter instance;    // Singleton

    private BluetoothAdapter bluetooth_adapter = null;
    private BluetoothSocket bluetooth_socket = null;
    private ConnectedThread thread_connection_bluetooth;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static String mac_address = null;

    private Activity room_activity;

    private BluetoothManagerAdapter(Activity room_activity){
        this.room_activity = room_activity;
    }

    public static BluetoothManagerAdapter getFirstInstance(Activity room_activity){
        if(instance==null)
        {
            instance = new BluetoothManagerAdapter(room_activity);
        }
        return instance;
    }

    public static BluetoothManagerAdapter getInstance(){
        return instance;
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

        return 0;
    }

    public void requestInfraredData(InfraredLayoutAdapter infrared_layout_adapter){
        //  Comunicar o sistema embarcado que necessita receber dados
        //  0 ---> Sinal de envio de dados infravermelho
        sendData((byte) 0);

        //  Comunica a thread que deseja captar a mensagem bluetooth
        thread_connection_bluetooth.setInfraredFlag(true);

        //  Espera a resposta da Thread
        while (thread_connection_bluetooth.getInfraredFlag()){}

        String msg = thread_connection_bluetooth.getInfraredCodeReceived();
        System.out.println("\t\tRECEBI A MENSAGEM: " + msg);

        infrared_layout_adapter.decodeInfraredMessageReceived(msg);

        /*synchronized (thread_connection_bluetooth){
            try{
                thread_connection_bluetooth.wait();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\t\tRECEBI A MENSAGEM");
            String msg = thread_connection_bluetooth.getInfraredCodeReceived();
            infrared_layout_adapter.decodeInfraredMessageReceived(msg);
        }*/
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


