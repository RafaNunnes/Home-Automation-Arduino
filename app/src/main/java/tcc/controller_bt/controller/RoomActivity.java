package tcc.controller_bt.controller;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import java.util.ArrayList;

import tcc.controller_bt.R;
import tcc.controller_bt.model.BluetoothManagerAdapter;
import tcc.controller_bt.model.APIConnectionInterface;

public class RoomActivity extends AppCompatActivity {

    Button id_digital_control_1, id_digital_control_2, id_digital_control_3, id_button_connect;
    SeekBar id_analog_control_1;

    Button id_infrared_control_1, id_infrared_control_2, id_infrared_control_3, id_infrared_control_4, id_infrared_control_5;

    private APIConnectionInterface manager_connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room);

        manager_connection = new BluetoothManagerAdapter(this);

        id_digital_control_1 = (Button) findViewById(R.id.IdControl1);
        id_digital_control_2 = (Button) findViewById(R.id.IdControl2);
        id_digital_control_3 = (Button) findViewById(R.id.IdControl3);
        id_button_connect = (Button) findViewById(R.id.IdControlConnect);
        id_analog_control_1 = (SeekBar) findViewById(R.id.IdControl4);
        id_infrared_control_1 = (Button) findViewById(R.id.IdControl5);
        id_infrared_control_2 = (Button) findViewById(R.id.IdControl6);
        id_infrared_control_3 = (Button) findViewById(R.id.IdControl7);
        id_infrared_control_4 = (Button) findViewById(R.id.IdControl8);
        id_infrared_control_5 = (Button) findViewById(R.id.IdControl9);


        id_digital_control_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte control_type = 1;
                byte logical_port = 6;
                manager_connection.sendData(control_type);
                manager_connection.sendData(logical_port);
            }
        });

        id_digital_control_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte control_type = 1;
                byte logical_port = 7;
                manager_connection.sendData(control_type);
                manager_connection.sendData(logical_port);
            }
        });

        id_digital_control_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte control_type = 1;
                byte logical_port = 8;
                manager_connection.sendData(control_type);
                manager_connection.sendData(logical_port);
            }
        });

        id_analog_control_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                manager_connection.sendData((byte) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                byte control_type = 2;
                byte logical_port = 5;

                manager_connection.sendData(control_type);
                manager_connection.sendData(logical_port);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                manager_connection.sendData((byte) -1);
            }
        });

        id_infrared_control_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String infrared_code;
                byte control_type = 3;          // Tipo de controle
                byte logical_port = 3;          // Porta Lógica
                byte format_type = 127; // Formato do código infravermelho
                byte num_bits = 32;             // Quantidade de bits

                manager_connection.sendData(control_type);
                manager_connection.sendData(logical_port);
                manager_connection.sendData(format_type);
                manager_connection.sendData(num_bits);

                infrared_code = "3772793023"; // Código enviado para Ligar/Desligar aparelho

                manager_connection.sendData(infrared_code);
            }
        });

        id_button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //manager_connection.connect();
                ArrayList<Intent> request_intentions_manager;
                request_intentions_manager = manager_connection.verifyBluetoothStatus();

                if(request_intentions_manager.size() > 0){
                    for (Intent intent : request_intentions_manager){
                        startActivityForResult(intent, 1);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle chosen_address = data.getExtras();
        String mac_address = chosen_address.getString(BluetoothManagerAdapter.EXTRA_DEVICE_ADDRESS);
        manager_connection.connect(mac_address);
    }

    @Override
    protected void onPause() {
        super.onPause();

        manager_connection.disconnect();
    }
}


