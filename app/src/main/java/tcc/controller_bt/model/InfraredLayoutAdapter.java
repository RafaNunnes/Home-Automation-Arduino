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

/**
 * Classe que adapta o Layout de Criação/Edição de Botões de Controle
 * para o estilo de entrada de dados do Controle Infravermelho, implementando
 * a Interface LayoutCreatorAdapter
 */
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

    /**
     * Método Construtor.
     * Esta classe precisa receber como parâmetros a Activity onde será exibido as informações
     * para entrada de dados do usuário para o Botão de Controle do tipo Infravermelho, bem como
     * a região (Layout) onde será exibida tais informações.
     * É necessário também o recebimento do botão de confirmação deste Layout para que se possa
     * habilitar-lo sempre que os inputs tenham sido inseridos da forma correta, e desabilitar-lo
     * caso contrário.
     *
     * @param activity Activity de Criação de Botões de Controle (ButtonCreationActivity)
     * @param layout Layout onde será adaptado para recepção dos Inputs referentes ao Controle Infravermelho
     * @param button Botão de confirmação de Inputs
     */
    public InfraredLayoutAdapter(Activity activity, GridLayout layout, Button button){
        design_button_layout = layout;
        dynamic_views = new DynamicViews(activity.getApplicationContext());
        confirm_button = button;
        button_creation_activity = activity;
        data_base = new DataBaseDAOImpl();
    }

    /**
     * Método responsável por gerar o Layout que suporte os Inputs referentes ao Botão de Controle
     * Infravermelho
     */
    public void generateLayout() {
        //  Remove todas as Views
        design_button_layout.removeAllViews();
        //  Desabilita o botão de confirmação
        confirm_button.setEnabled(false);

        //  Inicializa o gerente de conexão bluetooth
        manager_connection = BluetoothManagerAdapter.getInstance();

        //  Botão que será exibido no Layout para que o usuário comece o mapeamento Infravermelho
        Button start_receive_button = new Button(button_creation_activity);

        //  Preparação visual do botão de mapeamento
        final ViewGroup.LayoutParams layout_param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        start_receive_button.setLayoutParams(layout_param);
        start_receive_button.setText("Toque para começar o mapeamento");

        design_button_layout.setColumnCount(1);

        //  Gera uma mensagem para o usuário começar o mapeamento do controle remoto
        design_button_layout.addView(start_receive_button);

        //  Evento OnCLick do botão de mapeamento
        start_receive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                design_button_layout.removeAllViews();

                // TODO: Repassar o método requestInfraredData para a interface APIConnectionInterface
                //  Chama o método de leitura de dados da referida classe Gerente de Conexão
                ((BluetoothManagerAdapter) manager_connection).requestInfraredData(InfraredLayoutAdapter.this);
            }
        });
    }

    /**
     * Objeto responsável pelo monitoramento do Input feito pelo usuário, com a finalidade
     * de habilitar o botão de confirmação
     */
    private TextWatcher infraredTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            boolean confirm = (!input_name.getText().toString().trim().isEmpty());
            if(confirm){
                //  Habilita o botão de confirmação caso o campo de Input seja diferente de nulo
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
     * Método que decodifica uma mensagem de Infravermelho eviada pelo Sistema Embarcado.
     *
     * A mensagem Infravermelha possui uma estrutura formada por 4 campos separados por
     * vírgulas, e este método possui a função de quebrar cada campo em seus respectivos
     * formatos, que serão atribuidos aos Inputs do usuário de forma automática.
     *
     * Formato da Mensagem:
     * parts[0] => Porta Lógica do Infravermelho atribuida pelo Sistema Embarcado
     * parts[1] => Código Infravermelho, captado pelo Sistema Embarcado
     * parts[2] => Formato do distribuidor do código Infravermelho, captado pelo Sistema Embarcado
     * parts[3] => Número de Bits do código Infravermelho, captado pelo Sistema Embarcado
     *
     * @param message Mensagem para ser decodificada
     */
    public void decodeInfraredMessageReceived(String message){
        //  Define as variáveis input com os valores recebidos pela conexão bluetooth
        System.out.println(message);
        String parts[] = message.split(",");

        //TODO: Garantir que a mensagem tenha chegado corretamente, caso contrário, chamar o método generateLayout novamente.

        input_logical_port = Integer.valueOf(parts[0]);
        input_code = String.valueOf(parts[1]);
        input_format = Integer.valueOf(parts[2]);
        input_bits = Integer.valueOf(parts[3]);

        //  Atualiza o Layout da Activity de Criação de Botões de Controle (ButtonCreationActivity)
        //  com as informações do Infravermelho recebidas do Sistema Embarcado
        setNewLayout();
    }

    /**
     * Implementação do método que atualiza o Layout da ButtonCreationActivity
     * com as informações do Infravermelho recebidas do Sistema Embarcado
     */
    public void setNewLayout(){
        //  Gera o layout para o Infravermelho com as informações coletadas na leitura de dados
        design_button_layout.setColumnCount(2);

        //  Configura cada variável que será exibida no Layout
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

        //  Remove todos os elementos de Layout
        design_button_layout.removeAllViews();
        confirm_button.setEnabled(false);

        input_name.addTextChangedListener(infraredTextWatcher);

        //  Adiciona o novo Layout
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

    /**
     * Método que salva o botão no Banco de Dados SQLite, e finaliza a Activity (ButtonCreationActivity)
     * retornando o ID do Botão de Controle recém-criado
     */
    public void sendNewButton() {
        //  Adiciona o Botão de Controle ao Banco de Dados
        long id_returned = data_base.addControlButton(input_name.getText().toString(), DeviceControlButton.INFRARED_TYPE,
                input_logical_port, input_code, input_format, input_bits);

        //  Configura o retorno da Activity (ButtonCreationActivity)
        Intent intent = new Intent();
        intent.putExtra(ButtonCreationActivity.EXTRA_BUTTON_DATA, id_returned);
        button_creation_activity.setResult(APIConnectionInterface.STATUS_CONNECTION,intent);

        //  Encerra a Activity (ButtonCreationActivity)
        button_creation_activity.finish();
    }

    /**
     * Método para atualização das informações do Botão de Controle do tipo Infravermelho.
     *
     * @param control_button Botão de Controle que deve ser atualizado
     * @return Status de confirmação do sucesso (True) ou fracasso (False) da atualização
     *          do Botão de Controle
     */
    public boolean updateButton(DeviceControlButton control_button) {
        control_button.setName(input_name.getText().toString());
        control_button.setLogicalPort((byte) input_logical_port);
        ((InfraredButton) control_button).setInfraredCode(input_code);
        ((InfraredButton) control_button).setFormatType((byte) input_format);
        ((InfraredButton) control_button).setNumBits((byte) input_bits);

        return data_base.updateControlButton(control_button);
    }
}
