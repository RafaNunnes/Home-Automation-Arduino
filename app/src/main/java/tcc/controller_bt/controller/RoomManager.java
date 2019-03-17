package tcc.controller_bt.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import tcc.controller_bt.model.APIConnectionInterface;
import tcc.controller_bt.model.BluetoothManagerAdapter;
import tcc.controller_bt.model.DeviceControlButton;
import tcc.controller_bt.view.ButtonCreationActivity;

public class RoomManager {
    public static String EXTRA_MANAGER_CONNECTION = "manager_connection";
    public final static int BUTTON_CREATION = 2;

    private Activity room_activity;
    private LinearLayout room_screen_buttons;

    private APIConnectionInterface manager_connection;

    private ArrayList<View> control_buttons;

    public RoomManager(Activity activity, LinearLayout layout){
        room_activity = activity;
        room_screen_buttons = layout;

        manager_connection = new BluetoothManagerAdapter(room_activity);
        control_buttons = new ArrayList<View>();
    }

    public void verifyConnectionStatus(){
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

        /*final Button b1 = new Button(room_activity);
        int i = 0;
        b1.setId(i+1);
        b1.setText("Botão Criado");
        b1.setTag(1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b1.setText("MUDEI");
                byte control_type = 1;
                byte logical_port = 6;

                SwitchButton switch_button = new SwitchButton(control_type,logical_port);

                manager_connection.sendData(switch_button.getControlType());
                manager_connection.sendData(switch_button.getLogicalPort());

                control_type = 1;
                logical_port = 7;

                SwitchButton switch_button2 = new SwitchButton(control_type,logical_port);

                manager_connection.sendData(switch_button2.getControlType());
                manager_connection.sendData(switch_button2.getLogicalPort());

            }
        });*/
    }

    public void saveNewButton(View view){
        //TODO
        //Adiciona no arrayList
        //Salva no banco de dados
    }

    public void updateRoomScreen(DeviceControlButton button_received){
        //Quando retornar da activity de criação de botão, adicionar o botão na RoomActivity
        room_screen_buttons.addView(button_received.generateControlButton(room_activity.getApplicationContext(),manager_connection));

    }


}
