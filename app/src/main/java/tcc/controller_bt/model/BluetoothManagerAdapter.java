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

/**
 * Classe Gerente de Conexão Bluetooth, responsável por todos os tratamentos
 * relativos a este tipo de conexão
 */
public class BluetoothManagerAdapter implements APIConnectionInterface {
    private static BluetoothManagerAdapter instance;    // Singleton

    private BluetoothAdapter bluetooth_adapter = null;
    private BluetoothSocket bluetooth_socket = null;
    private ConnectedThread thread_connection_bluetooth;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static String mac_address = null;

    private Activity room_activity;

    /**
     * Construtor da Classe Gerente de Conexão Bluetooth
     *
     * @param room_activity Activity principal da aplicação (RoomActivity)
     */
    private BluetoothManagerAdapter(Activity room_activity){
        this.room_activity = room_activity;
    }

    /**
     * Método que inicializa a primeira e única instância do objeto BluetoothManagerAdapter
     * para ser utilizada por qualquer Gerente de Controle da aplicação
     *
     * @param room_activity Activity principal da aplicação (RoomActivity)
     * @return Instância única do objeto BluetoothManagerAdapter (Padrão Singleton)
     */
    public static BluetoothManagerAdapter getFirstInstance(Activity room_activity){
        if(instance==null)
        {
            instance = new BluetoothManagerAdapter(room_activity);
        }
        return instance;
    }

    /**
     * Método que retorna a única instância do objeto BluetoothManagerAdapter
     *
     * @return Instância única do objeto BluetoothManagerAdapter (Padrão Singleton)
     */
    public static BluetoothManagerAdapter getInstance(){
        return instance;
    }

    /**
     * Método que efetua a conexão de fato entre o Sistema Embarcado e o Smartphone
     * Estabelecendo a abertura do seu referido Socket e criando a Thread que
     * constantemente tratará os eventos de Envios e Recebimentos de Dados
     * entre os dispositivos
     *
     * @param address Endereço MAC do dispositivo pareado
     */
    public void connect(String address) {
        mac_address = address;

        //  Carrega o dispositivo pareado através do endereço MAC no Adaptador Padrão do Smartphone
        BluetoothDevice device = bluetooth_adapter.getRemoteDevice(mac_address);

        //  Cria uma conexão de saída para o dispositivo usando o serviço UUID
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

        //  Instancia e inicializa a Thread responsável pela troca de informações entre os dispositivos
        thread_connection_bluetooth = new ConnectedThread(bluetooth_socket);
        thread_connection_bluetooth.start();
    }

    /**
     * Método que fecha a conexão socket e, consequentemente, desconecta
     * os dispositivos
     */
    public void disconnect() {
        if (bluetooth_socket != null)
        {
            try {
                //  Fecha o Socket
                bluetooth_socket.close();
            }
            catch (IOException e) {
                Toast.makeText(room_activity.getApplicationContext(), "Não foi possível fechar o socket", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Método para enviar dados do tipo byte da Aplicação para o Sistema Embarcado
     *
     * @param data Dado a ser enviado
     */
    public void sendData(byte data) {
        if(bluetooth_socket != null) {
            if(bluetooth_socket.isConnected()){
                try {
                    //  Envia o dado a ser enviado para a Thread de comunicação
                    thread_connection_bluetooth.write(data);
                }catch (IOException e)
                {
                    Toast.makeText(room_activity.getApplicationContext(), "A conexão falhou", Toast.LENGTH_LONG).show();
                    //connect();
                }
            }
        } else {
            Toast.makeText(room_activity.getApplicationContext(), "Bluetooth Desconectado", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Método para enviar dados do tipo String da Aplicação para o Sistema Embarcado
     *
     * @param infrared_code Dado a ser enviado
     */
    public void sendData(String infrared_code) {
        if(bluetooth_socket != null) {
            if(bluetooth_socket.isConnected()){
                try {
                    //  Envia o dado a ser enviado para a Thread de comunicação
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

    /**
     * TODO
     * Método para recebimento de dados Sistema Embarcado
     *
     * @return Dado Recebido do Sistema Embarcado
     */
    public int receiveData() {
        return 0;
    }

    /**
     * Método que requisita um código infravermelho do Sistema Embarcado
     * Enquanto o Sistema Embarcado não envia o código, este método permanece
     * em modo de espera
     *
     * @param infrared_layout_adapter Layout onde será exibida as informações sobre
     *                                o código infravermelho recebido
     */
    public void requestInfraredData(InfraredLayoutAdapter infrared_layout_adapter){
        if(bluetooth_socket != null) {
            if(bluetooth_socket.isConnected()){
                //  Comunicar o sistema embarcado que necessita receber dados
                //  0 ---> Sinal de envio de dados infravermelho
                sendData((byte) 0);

                //  Comunica a thread que deseja captar a mensagem bluetooth
                thread_connection_bluetooth.setInfraredFlag(true);

                //  Espera a resposta da Thread
                while (thread_connection_bluetooth.getInfraredFlag()){}

                //  Guarda a mensagem com o código infravermelho completo
                String msg = thread_connection_bluetooth.getInfraredCodeReceived();
                //System.out.println("\t\tRECEBI A MENSAGEM: " + msg);

                //  Envia a mensagem recebida pela Thread para a classe InfraredLayoutAdapter,
                //  responsável pela decodificação e exibição do código para o usuário
                infrared_layout_adapter.decodeInfraredMessageReceived(msg);
            } else {
                Toast.makeText(room_activity.getApplicationContext(), "Bluetooth Desconectado", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(room_activity.getApplicationContext(), "Bluetooth Desconectado", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Método que verifica se o aparelho possui suporte para conexão Bluetooth
     * e, se suportar, solicita ao usuário a permissão para estabelecer a conexão.
     * Após isso, o usuário será redirecionado para a Activity que trata da escolha
     * dos dispositivos pareados para a conexão propriamente dita
     */
    public void verifyConnectionStatus() {
        //  Carrega o Adaptador Padrão Bluetooth do Smartphone
        bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();

        if(bluetooth_adapter==null) {
            Toast.makeText(room_activity.getApplicationContext(), "O dispositivo não suporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            //  Inicia a activity DevicesList para escolher o dispositivo que se deseja criar conexão
            Intent intent = new Intent(room_activity.getApplicationContext(), DevicesList.class);
            room_activity.startActivityForResult(intent,STATUS_CONNECTION);

            if (bluetooth_adapter.isEnabled()) {
                Toast.makeText(room_activity.getApplicationContext(), "Bluetooth está ativado", Toast.LENGTH_LONG).show();
            } else {
                Intent enable_bluetooth_intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                room_activity.startActivity(enable_bluetooth_intent);
            }
        }
    }
}


