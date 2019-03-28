package tcc.controller_bt.model;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import tcc.controller_bt.view.ButtonCreationActivity;

public class InfraredLayoutAdapter implements LayoutCreatorAdapter {
    private Activity button_creation_activity;
    private GridLayout design_button_layout;
    private DynamicViews dynamic_views;
    private Button confirm_button;

    private DataBaseDAOImpl data_base;

    private APIConnectionInterface manager_connection;

    // Inputs referentes ao Circuito Infravermelho
    private EditText input_name;
    int input_logical_port, input_format, input_bits;
    String input_code;

    public InfraredLayoutAdapter(Activity activity, GridLayout layout, Button button){
        design_button_layout = layout;
        dynamic_views = new DynamicViews(activity.getApplicationContext());
        confirm_button = button;
        button_creation_activity = activity;
        data_base = new DataBaseDAOImpl();
    }

    @Override
    public void generateLayout() {
        //  Remove todas as Views
        design_button_layout.removeAllViews();
        confirm_button.setEnabled(false);

        //  Inicializa o gerente de conexão bluetooth
        manager_connection = BluetoothManagerAdapter.getInstance();

        //  Gera uma mensagem para o usuário começar o mapeamento do controle remoto
        Button start_receive_button = new Button(button_creation_activity);

        final ViewGroup.LayoutParams layout_param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        start_receive_button.setLayoutParams(layout_param);
        start_receive_button.setText("Pressione para começar o mapeamento");

        design_button_layout.setColumnCount(1);
        design_button_layout.addView(start_receive_button);

        start_receive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                design_button_layout.removeAllViews();

                //  Chama o método de leitura de dados da classe gerente de conexão bluetooth
                ((BluetoothManagerAdapter) manager_connection).requestInfraredData(InfraredLayoutAdapter.this);
            }
        });
    }

    private TextWatcher infraredTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            boolean confirm = (!input_name.getText().toString().trim().isEmpty());
            if(confirm){
                confirm_button.setEnabled(true);
            } else {
                confirm_button.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void decodeInfraredMessageReceived(String message){
        //  Define as variáveis input com os valores recebidos pela conexão bluetooth
        System.out.println(message);
        String parts[] = message.split(",");

        //TODO: Garantir que a mensagem tenha chegado corretamente, caso contrário, chamar o método generateLayout novamente.

        input_logical_port = Integer.valueOf(parts[0]);
        input_code = String.valueOf(parts[1]);
        input_format = Integer.valueOf(parts[2]);
        input_bits = Integer.valueOf(parts[3]);

        setNewLayout();
    }

    public void setNewLayout(){
        //  Gera o layout para o Infravermelho com as informações coletadas na leitura de dados
        design_button_layout.setColumnCount(2);

        TextView name = dynamic_views.descriptionTextView("Nome: ");
        input_name = dynamic_views.descriptionEditText(InputType.TYPE_CLASS_TEXT);
        TextView logical_port = dynamic_views.descriptionTextView("Porta Lógica: ");
        TextView input_port = dynamic_views.descriptionTextView(String.valueOf(input_logical_port));
        TextView infrared_code = dynamic_views.descriptionTextView("Código infravermelho: ");
        TextView input_infrared_code = dynamic_views.descriptionTextView(input_code);
        TextView format_type = dynamic_views.descriptionTextView("Formato: ");
        TextView input_format_type = dynamic_views.descriptionTextView(String.valueOf(input_format));
        TextView num_bits = dynamic_views.descriptionTextView("Número de Bits: ");
        TextView input_num_bits = dynamic_views.descriptionTextView(String.valueOf(input_bits));

        design_button_layout.removeAllViews();
        confirm_button.setEnabled(false);

        input_name.addTextChangedListener(infraredTextWatcher);

        design_button_layout.addView(name);
        design_button_layout.addView(input_name);
        design_button_layout.addView(logical_port);
        design_button_layout.addView(input_port);
        design_button_layout.addView(infrared_code);
        design_button_layout.addView(input_infrared_code);
        design_button_layout.addView(format_type);
        design_button_layout.addView(input_format_type);
        design_button_layout.addView(num_bits);
        design_button_layout.addView(input_num_bits);
    }

    @Override
    public void sendNewButton() {
        long id_returned = data_base.addControlButton(input_name.getText().toString(), DeviceControlButton.INFRARED_TYPE,
                input_logical_port, input_code, input_format, input_bits);

        Intent intent = new Intent();
        intent.putExtra(ButtonCreationActivity.EXTRA_BUTTON_DATA, id_returned);
        button_creation_activity.setResult(APIConnectionInterface.STATUS_CONNECTION,intent);

        button_creation_activity.finish();
    }

    @Override
    public boolean updateButton(DeviceControlButton control_button) {
        control_button.setName(input_name.getText().toString());
        control_button.setLogicalPort((byte) input_logical_port);
        ((InfraredButton) control_button).setInfraredCode(input_code);
        ((InfraredButton) control_button).setFormatType((byte) input_format);
        ((InfraredButton) control_button).setNumBits((byte) input_bits);

        return data_base.updateControlButton(control_button);
    }
}
