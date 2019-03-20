package tcc.controller_bt.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tcc.controller_bt.model.APIConnectionInterface;
import tcc.controller_bt.model.BluetoothManagerAdapter;
import tcc.controller_bt.model.DataBaseDAOImpl;
import tcc.controller_bt.model.DeviceControlButton;
import tcc.controller_bt.view.ButtonCreationActivity;

public class RoomManager {
    //public static String EXTRA_MANAGER_CONNECTION = "manager_connection";
    public final static int BUTTON_CREATION = 2;

    private Activity room_activity;
    private ViewGroup room_screen_buttons;

    private APIConnectionInterface manager_connection;
    private DataBaseDAOImpl data_base;

    private ArrayList<DeviceControlButton> control_buttons;

    public RoomManager(Activity activity, ViewGroup layout){
        room_activity = activity;
        room_screen_buttons = layout;

        manager_connection = new BluetoothManagerAdapter(room_activity);
        control_buttons = new ArrayList<DeviceControlButton>();

        data_base = new DataBaseDAOImpl();
        data_base.createTable();    //  Cria a tabela

        loadRoomScreenButtons();
    }

    public void verifyConnectionStatus(){
        manager_connection.disconnect();
        manager_connection.verifyConnectionStatus();
    }

    public void connect(String connection_content){
        manager_connection.connect(connection_content);
    }

    public void disconnect(){
        manager_connection.disconnect();
    }

    public void addControlButton(){

        Intent intent = new Intent(room_activity.getApplicationContext(), ButtonCreationActivity.class);
        //it.putExtra(EXTRA_MANAGER_CONNECTION, manager_connection);
        room_activity.startActivityForResult(intent,BUTTON_CREATION);
    }

    public void loadRoomScreenButtons(){
        control_buttons = data_base.getControlButtons();

        for(DeviceControlButton button : control_buttons){
            room_screen_buttons.addView(button.generateControlButton(room_activity.getApplicationContext(), manager_connection, room_screen_buttons));
        }
    }

    public void updateRoomScreen(long id){
        //Quando retornar da activity de criação de botão, adicionar o botão na RoomActivity
        //room_screen_buttons.addView(button_received.generateControlButton(room_activity.getApplicationContext(),manager_connection,room_screen_buttons));
        control_buttons.add(data_base.getControlButtonById(id));
        room_screen_buttons.addView(data_base.getControlButtonById(id).generateControlButton(room_activity.getApplicationContext(), manager_connection, room_screen_buttons));
    }


}
