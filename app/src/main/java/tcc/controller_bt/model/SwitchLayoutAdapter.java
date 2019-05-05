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

/**
 * Classe que adapta o Layout de Criação/Edição de Botões de Controle
 * para o estilo de entrada de dados do Controle Chaveado, implementando
 * a Interface LayoutCreatorAdapter
 */
public class SwitchLayoutAdapter implements LayoutCreatorAdapter {
    private Activity button_creation_activity;
    private GridLayout design_button_layout;
    private DynamicViews dynamic_views;
    private Button confirm_button;

    private DataBaseDAOImpl data_base;

    // Inputs referentes ao Circuito Chaveado
    private EditText input_name, input_logical_port;

    /**
     * Método Construtor.
     * Esta classe precisa receber como parâmetros a Activity onde será exibido as informações
     * para entrada de dados do usuário para o Botão de Controle do tipo Chaveado, bem como
     * a região (Layout) onde será exibida tais informações.
     * É necessário também o recebimento do botão de confirmação deste Layout para que se possa
     * habilitar-lo sempre que os inputs tenham sido inseridos da forma correta, e desabilitar-lo
     * caso contrário.
     *
     * @param activity Activity de Criação de Botões de Controle (ButtonCreationActivity)
     * @param layout Layout onde será adaptado para recepção dos Inputs referentes ao Controle Chaveado
     * @param button Botão de confirmação de Inputs
     */
    public SwitchLayoutAdapter(Activity activity, GridLayout layout, Button button){
        design_button_layout = layout;
        dynamic_views = new DynamicViews(activity.getApplicationContext());
        confirm_button = button;
        button_creation_activity = activity;
        data_base = new DataBaseDAOImpl();
    }


    /**
     * Método responsável por gerar o Layout que suporte os Inputs referentes ao Botão de Controle
     * Chaveado
     */
    public void generateLayout() {
        TextView name = dynamic_views.descriptionTextView("Nome: ");
        input_name = dynamic_views.descriptionEditText(InputType.TYPE_CLASS_TEXT);
        TextView logical_port = dynamic_views.descriptionTextView("Porta Lógica: ");
        input_logical_port = dynamic_views.descriptionEditText(InputType.TYPE_CLASS_NUMBER);

        //  Remove todas as Views
        design_button_layout.removeAllViews();
        //  Desabilita o botão de confirmação
        confirm_button.setEnabled(false);
        design_button_layout.setColumnCount(2);

        input_name.addTextChangedListener(TextWatcher);
        input_logical_port.addTextChangedListener(TextWatcher);

        //  Configura cada variável que será exibida no Layout
        design_button_layout.addView(name);
        design_button_layout.addView(input_name);
        design_button_layout.addView(logical_port);
        design_button_layout.addView(input_logical_port);
    }

    /**
     * Objeto responsável pelo monitoramento do Input feito pelo usuário, com a finalidade
     * de habilitar o botão de confirmação
     */
    private TextWatcher TextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            boolean confirm = (!input_name.getText().toString().trim().isEmpty()
                    && !input_logical_port.getText().toString().trim().isEmpty());
            if(confirm){
                //  Habilita o botão de confirmação caso os campos de Input sejam diferentes de nulo
                confirm_button.setEnabled(true);
            } else {
                confirm_button.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    /**
     * Método que salva o botão no Banco de Dados SQLite, e finaliza a Activity (ButtonCreationActivity)
     * retornando o ID do Botão de Controle recém-criado
     */
    public void sendNewButton() {
        //  Adiciona o Botão de Controle ao Banco de Dados
        long id_returned = data_base.addControlButton(input_name.getText().toString(), DeviceControlButton.SWITCH_TYPE,
                Integer.valueOf(input_logical_port.getText().toString()), null, 0,0);

        //  Configura o retorno da Activity (ButtonCreationActivity)
        Intent intent = new Intent();
        intent.putExtra(ButtonCreationActivity.EXTRA_BUTTON_DATA, id_returned);
        button_creation_activity.setResult(APIConnectionInterface.STATUS_CONNECTION,intent);

        //  Encerra a Activity (ButtonCreationActivity)
        button_creation_activity.finish();
    }

    /**
     * Método para atualização das informações do Botão de Controle do tipo Chaveado.
     *
     * @param control_button Botão de Controle que deve ser atualizado
     * @return Status de confirmação do sucesso (True) ou fracasso (False) da atualização
     *          do Botão de Controle
     */
    public boolean updateButton(DeviceControlButton control_button) {
        control_button.setName(input_name.getText().toString());
        control_button.setLogicalPort(Byte.valueOf(input_logical_port.getText().toString()));

        return data_base.updateControlButton(control_button);
    }
}
