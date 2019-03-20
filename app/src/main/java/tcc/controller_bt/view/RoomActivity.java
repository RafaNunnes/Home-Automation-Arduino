package tcc.controller_bt.view;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import tcc.controller_bt.R;
import tcc.controller_bt.controller.RoomManager;
import tcc.controller_bt.model.APIConnectionInterface;
import tcc.controller_bt.model.DeviceControlButton;

public class RoomActivity extends AppCompatActivity {

    private Button id_button_connect, id_button_add_itens;
    private LinearLayout room_buttons_layout;

    private RoomManager room_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room);

        room_buttons_layout = (LinearLayout) findViewById(R.id.IdRoomLayout);
        id_button_connect = (Button) findViewById(R.id.IdControlConnect);
        id_button_add_itens = (Button) findViewById(R.id.IdAddButton);

        room_manager = new RoomManager(RoomActivity.this, room_buttons_layout);


        id_button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                room_manager.verifyConnectionStatus();
            }
        });

        id_button_add_itens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                room_manager.addControlButton();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle chosen_data;

        switch (requestCode){
            case APIConnectionInterface.STATUS_CONNECTION:
                if(data != null)
                {
                    chosen_data = data.getExtras();
                    String data_received = chosen_data.getString(APIConnectionInterface.EXTRA_DEVICE_DATA);
                    room_manager.connect(data_received);
                }
                break;

            case RoomManager.BUTTON_CREATION:
                if(data != null){
                    chosen_data = data.getExtras();
                    long id_button = chosen_data.getLong(ButtonCreationActivity.EXTRA_BUTTON_DATA);
                    room_manager.updateRoomScreen(id_button);
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        room_manager.disconnect();
    }

    /*protected void onPause() {
        super.onPause();

        room_manager.disconnect();
    }*/
}


