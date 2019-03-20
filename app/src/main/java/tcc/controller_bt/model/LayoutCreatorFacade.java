package tcc.controller_bt.model;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import tcc.controller_bt.view.ButtonCreationActivity;

public class LayoutCreatorFacade {
    private Activity button_creation_activity;
    private GridLayout design_button_layout;
    private DynamicViews dynamic_views;
    private Button confirm_button;

    private DataBaseDAOImpl data_base;

    // Inputs referentes ao Circuito Chaveado
    private EditText switch_input_name, switch_input_logical_port;

    // Inputs referentes ao Circuito Dimmer
    private EditText dimmer_input_name, dimmer_input_logical_port;

    // Inputs referentes ao Circuito Infravermelho
    private EditText infrared_input_name;
    int infrared_input_logical_port, infrared_input_format, infrared_input_bits;
    String infrared_input_code;

    private int button_type_flag;

    public LayoutCreatorFacade(Activity activity, GridLayout layout, Button button){
        design_button_layout = layout;
        dynamic_views = new DynamicViews(activity.getApplicationContext());
        confirm_button = button;
        button_creation_activity = activity;
        data_base = new DataBaseDAOImpl();

        //data_base.createTable();    //  Cria a tabela Botões
    }

    public void generateSwitchLayout(){
        TextView name = dynamic_views.descriptionTextView("Nome: ");
        switch_input_name = dynamic_views.descriptionEditText(InputType.TYPE_CLASS_TEXT);
        TextView logical_port = dynamic_views.descriptionTextView("Porta Lógica: ");
        switch_input_logical_port = dynamic_views.descriptionEditText(InputType.TYPE_CLASS_NUMBER);

        design_button_layout.removeAllViews();
        confirm_button.setEnabled(false);

        switch_input_name.addTextChangedListener(switchTextWatcher);
        switch_input_logical_port.addTextChangedListener(switchTextWatcher);

        design_button_layout.addView(name);
        design_button_layout.addView(switch_input_name);
        design_button_layout.addView(logical_port);
        design_button_layout.addView(switch_input_logical_port);
    }

    public void generateDimmerLayout(){
        TextView name = dynamic_views.descriptionTextView("Nome: ");
        dimmer_input_name = dynamic_views.descriptionEditText(InputType.TYPE_CLASS_TEXT);
        TextView logical_port = dynamic_views.descriptionTextView("Porta Lógica: ");
        dimmer_input_logical_port = dynamic_views.descriptionEditText(InputType.TYPE_CLASS_NUMBER);

        design_button_layout.removeAllViews();
        confirm_button.setEnabled(false);

        dimmer_input_name.addTextChangedListener(dimmerTextWatcher);
        dimmer_input_logical_port.addTextChangedListener(dimmerTextWatcher);

        design_button_layout.addView(name);
        design_button_layout.addView(dimmer_input_name);
        design_button_layout.addView(logical_port);
        design_button_layout.addView(dimmer_input_logical_port);
    }

    public void generateInfraredLayout(){
        TextView name = dynamic_views.descriptionTextView("Nome: ");
        infrared_input_name = dynamic_views.descriptionEditText(InputType.TYPE_CLASS_TEXT);
        TextView logical_port = dynamic_views.descriptionTextView("Porta Lógica: ");
        TextView input_logical_port = dynamic_views.descriptionTextView("Porta Lógica Recebida pelo Arduino");
        TextView infrared_code = dynamic_views.descriptionTextView("Código infravermelho: ");
        TextView input_infrared_code = dynamic_views.descriptionTextView("Código infravermelho Recebido pelo Arduino");
        TextView format_type = dynamic_views.descriptionTextView("Formato: ");
        TextView input_format_type = dynamic_views.descriptionTextView("Formato Recebido pelo Arduino");
        TextView num_bits = dynamic_views.descriptionTextView("Número de Bits: ");
        TextView input_num_bits = dynamic_views.descriptionTextView("Número de Bits Recebido pelo Arduino");

        design_button_layout.removeAllViews();
        confirm_button.setEnabled(false);

        infrared_input_name.addTextChangedListener(infraredTextWatcher);

        design_button_layout.addView(name);
        design_button_layout.addView(infrared_input_name);
        design_button_layout.addView(logical_port);
        design_button_layout.addView(input_logical_port);
        design_button_layout.addView(infrared_code);
        design_button_layout.addView(input_infrared_code);
        design_button_layout.addView(format_type);
        design_button_layout.addView(input_format_type);
        design_button_layout.addView(num_bits);
        design_button_layout.addView(input_num_bits);
    }

    public void setInputEmbeddedSystem(){
        //TODO
    }

    private TextWatcher switchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            boolean confirm = (!switch_input_name.getText().toString().trim().isEmpty()
                                && !switch_input_logical_port.getText().toString().trim().isEmpty());
            if(confirm){
                /*device_control_button = new SwitchButton(switch_input_name.getText().toString(),(byte) 1,
                        Byte.valueOf(switch_input_logical_port.getText().toString()));
                        */
                button_type_flag = DeviceControlButton.SWITCH_TYPE;
                confirm_button.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher dimmerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            boolean confirm = (!dimmer_input_name.getText().toString().trim().isEmpty()
                    && !dimmer_input_logical_port.getText().toString().trim().isEmpty());
            if(confirm){
                /*device_control_button = new DimmerButton(dimmer_input_name.getText().toString(),(byte) 2,
                        Byte.valueOf(dimmer_input_logical_port.getText().toString()));
                        */
                button_type_flag = DeviceControlButton.DIMMER_TYPE;
                confirm_button.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private TextWatcher infraredTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            boolean confirm = (!infrared_input_name.getText().toString().trim().isEmpty());
            if(confirm){
                /*device_control_button = new InfraredButton(infrared_input_name.getText().toString(),(byte) 3,
                        infrared_input_logical_port, infrared_input_code,
                        infrared_input_format, infrared_input_bits);
                        */
                button_type_flag = DeviceControlButton.INFRARED_TYPE;
                confirm_button.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void sendNewButton() {
        long id_returned = -1;

        switch (button_type_flag){
            case DeviceControlButton.SWITCH_TYPE:
                id_returned = data_base.addControlButton(switch_input_name.getText().toString(), DeviceControlButton.SWITCH_TYPE,
                        Integer.valueOf(switch_input_logical_port.getText().toString()), null, 0,0);
                break;

            case DeviceControlButton.DIMMER_TYPE:
                id_returned = data_base.addControlButton(dimmer_input_name.getText().toString(), DeviceControlButton.DIMMER_TYPE,
                        Integer.valueOf(dimmer_input_logical_port.getText().toString()), null, 0, 0);
                break;

            case DeviceControlButton.INFRARED_TYPE:
                id_returned = data_base.addControlButton(infrared_input_name.getText().toString(), DeviceControlButton.INFRARED_TYPE,
                        infrared_input_logical_port, infrared_input_code, infrared_input_format, infrared_input_bits);
                break;
        }

        if(id_returned != -1){
            Intent intent = new Intent();
            intent.putExtra(ButtonCreationActivity.EXTRA_BUTTON_DATA, id_returned);
            button_creation_activity.setResult(APIConnectionInterface.STATUS_CONNECTION,intent);
        }

        button_creation_activity.finish();
    }
}
