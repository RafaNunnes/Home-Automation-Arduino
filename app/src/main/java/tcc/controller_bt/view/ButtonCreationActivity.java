package tcc.controller_bt.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import tcc.controller_bt.R;
import tcc.controller_bt.controller.ButtonCreationManager;

public class ButtonCreationActivity extends AppCompatActivity {
    private Button id_switch_control, id_dimmer_control, id_infrared_control, id_confirm_button;
    private GridLayout id_design_button_layout;

    public static String EXTRA_BUTTON_DATA = "new_button";
    private ButtonCreationManager button_creation_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_creation);

        id_switch_control = (Button) findViewById(R.id.IdSwitchControl);
        id_dimmer_control = (Button) findViewById(R.id.IdDimmerControl);
        id_infrared_control = (Button) findViewById(R.id.IdInfraredControl);
        id_confirm_button = (Button) findViewById(R.id.IdConfirmButton);
        id_design_button_layout = (GridLayout) findViewById(R.id.IdDesignButtonLayout);

        button_creation_manager = new ButtonCreationManager(ButtonCreationActivity.this, id_design_button_layout, id_confirm_button);

        id_switch_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_switch_control.setTextColor(Color.rgb(0,255,0));
                id_dimmer_control.setTextColor(Color.rgb(255,255,255));
                id_infrared_control.setTextColor(Color.rgb(255,255,255));
                button_creation_manager.setSwitchLayout();
            }
        });

        id_dimmer_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_switch_control.setTextColor(Color.rgb(255,255,255));
                id_dimmer_control.setTextColor(Color.rgb(0,255,0));
                id_infrared_control.setTextColor(Color.rgb(255,255,255));
                button_creation_manager.setDimmerLayout();
            }
        });

        id_infrared_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_switch_control.setTextColor(Color.rgb(255,255,255));
                id_dimmer_control.setTextColor(Color.rgb(255,255,255));
                id_infrared_control.setTextColor(Color.rgb(0,255,0));
                button_creation_manager.setInfraredLayout();
            }
        });

        id_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Criar o objeto Controle e retornar à activity Room
                button_creation_manager.confirmButtonAttributes();
            }
        });
    }
}
