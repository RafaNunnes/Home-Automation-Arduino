package tcc.controller_bt.view;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import tcc.controller_bt.R;
import tcc.controller_bt.controller.RoomManager;
import tcc.controller_bt.model.APIConnectionInterface;

/**
 * Classe Principal da Aplicação, responsável pelo armazenamento dos
 * botões criados pelo usuário, bem como o direcionamento para as
 * Activitys de Criação de Botão e de Conexão com o Sistema Embarcado
 */
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

        // Inicia a classe que gerencia todos os eventos desta activity
        room_manager = new RoomManager(RoomActivity.this, room_buttons_layout);

        // Evento onClick do Botão Conectar
        id_button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // A classe gerente trata o evento de conexão com seu respectivo gerente
                room_manager.verifyConnectionStatus();
            }
        });

        // Evento onClick do Botão Adicionar
        id_button_add_itens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // A classe gerente trata o evento de Adicionar Botão de Controle
                room_manager.addControlButton();
            }
        });
    }

    /**
     * Método para tratamento dos resultados das Activitys
     *
     * @param requestCode Código identificador repassado por quem solicitou a Intent
     * @param resultCode Código de resultado
     * @param data Conteúdo repassado pela Activity
     */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle chosen_data;

        switch (requestCode){
            /**
             requestCode => 1
             Código enviado pelo Gerente de Conexão para recebimento de informação de Conexão
             data => Caso o Gerente de Conexão seja do tipo Bluetooth:
                    -> Representa então o endereço MAC do Bluetooth Pareado
             */
            case APIConnectionInterface.STATUS_CONNECTION:
                if(data != null)
                {
                    chosen_data = data.getExtras();
                    String data_received = chosen_data.getString(APIConnectionInterface.EXTRA_DEVICE_DATA);
                    // Ao receber o endereço MAC, caso a Classe Gerente de Conexão for a Bluetooth,
                    // aciona a conexão do dispositivo
                    room_manager.connect(data_received);
                }
                break;
            /**
             requestCode => 2
             Código enviado pelo Gerente de Criação de Botões para recebimento de ID
             data => ID do Botão de Controle criado
             */
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

        // Encerra a conexão ao fechar a aplicação
        room_manager.disconnect();
    }

    /*protected void onPause() {
        super.onPause();

        room_manager.disconnect();
    }*/
}


