package tcc.controller_bt.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.ViewGroup;

import java.util.ArrayList;

import tcc.controller_bt.model.APIConnectionInterface;
import tcc.controller_bt.model.BluetoothManagerAdapter;
import tcc.controller_bt.model.ButtonViewFactory;
import tcc.controller_bt.model.DataBaseDAOImpl;
import tcc.controller_bt.model.DeviceControlButton;
import tcc.controller_bt.view.ButtonCreationActivity;

/**
 * Classe Gerente Controller(MVC) da View(MVC) RoomActivity
 */
public class RoomManager {
    public final static int BUTTON_CREATION = 2; // RequestCode

    private Activity room_activity;
    private ViewGroup room_screen_buttons;

    private APIConnectionInterface manager_connection;
    private DataBaseDAOImpl data_base;

    private ArrayList<DeviceControlButton> control_buttons;

    /**
     * Método Construtor da Classe Gerente
     *
     * @param activity Activity principal da aplicação (RoomActivity)
     * @param layout Região da RoomActivity onde se encontram os Botões
     *               de Controle criados
     */
    public RoomManager(Activity activity, ViewGroup layout){
        room_activity = activity;
        room_screen_buttons = layout;

        // Inicia o gerente de conexão através de uma instância única do objeto
        manager_connection = BluetoothManagerAdapter.getFirstInstance(activity);
        // Lista de objetos do tipo DeviceControlButton (Switch/Dimmer/Infrared)Button
        control_buttons = new ArrayList<DeviceControlButton>();

        data_base = new DataBaseDAOImpl();
        data_base.createTable();    //  Cria a tabela no Banco de dados

        /*
        Atualiza a região que contém os Botões de Controle na RoomActivity
        utilizando os botões salvos no Banco de Dados SQLite
        */
        loadRoomScreenButtons();
    }

    /**
     * Atribui ao Gerente de Conexão a função de inicializar o processo
     * de conexão entre o smartphone e o sistema embarcado
     */
    public void verifyConnectionStatus(){
        manager_connection.disconnect();
        manager_connection.verifyConnectionStatus();
    }

    /**
     * Invoca o método de conexão do Gerente de Conexão
     *
     * @param connection_content Caso trate-se de um Gerente de Conexão Bluetooth:
     *                           -> Endereço MAC do dispositivo pareado
     *                           -> (Bluetooth do Sistema Embarcado)
     */
    public void connect(String connection_content){
        manager_connection.connect(connection_content);
    }

    /**
     * Invoca o método de encerramento de conexão do Gerente de Conexão
     */
    public void disconnect(){
        manager_connection.disconnect();
    }

    /**
     * Método que lança a intent para inicializar a Activity responsável pela
     * criação de novos Botões de Controle
     */
    public void addControlButton(){
        //manager_connection.sendData((byte) 0);

        Intent intent = new Intent(room_activity.getApplicationContext(), ButtonCreationActivity.class);
        room_activity.startActivityForResult(intent,BUTTON_CREATION);
    }

    /**
     * Método que carrega e insere todos os Botões de Controle cadastrados pelo
     * usuário no Banco de Dados SQLite
     */
    public void loadRoomScreenButtons(){
        //  Carrega todos os Botões de Controle salvos no Banco de Dados
        control_buttons = data_base.getControlButtons();

        //  Para cada botão, cria-se uma View(Button/SeekBar) e insere na Tela Principal do usuário (RoomActivity)
        for(DeviceControlButton button : control_buttons){
            //  Fábrica de botões é instanciada dependendo do tipo de controle
            ButtonViewFactory factory = button.getFactory();

            //  Insere o Botão de Controle na RoomActivity
            room_screen_buttons.addView(factory.generateControlButton(room_activity, manager_connection, room_screen_buttons));
        }
    }

    /**
     * Método para atualizar a Tela Principal do usuário (RoomActivity)
     * com o novo Botão de Controle criado
     *
     * @param id ID do novo Botão de Controle criado
     */
    public void updateRoomScreen(long id){
        //  Carrega o Botão de Controle do Banco de Dados utilizando o seu ID
        DeviceControlButton control_button = data_base.getControlButtonById(id);
        control_buttons.add(control_button);

        //  Instancia-se a fábrica de botões com o tipo de Botão de Controle recebido
        ButtonViewFactory factory = control_button.getFactory();
        //  Atualiza a Tela Principal (RoomActivity) com o novo Botão de Controle
        room_screen_buttons.addView(factory.generateControlButton(room_activity, manager_connection, room_screen_buttons));
    }


}
