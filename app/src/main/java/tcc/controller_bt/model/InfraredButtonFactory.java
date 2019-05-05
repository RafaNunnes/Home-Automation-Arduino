package tcc.controller_bt.model;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import tcc.controller_bt.R;

/**
 * Classe que implementa a Interface ButtonViewFactory, ou seja, a
 * abstração de uma Fábrica segundo o padrão de projeto Abstract Factory.
 *
 * Esta classe representa a fábrica de componentes view SeekBar, pois o
 * tipo Botão de Controle é o Infravermelho.
 */
public class InfraredButtonFactory implements ButtonViewFactory {
    InfraredButton infrared_button;

    /**
     * Construtor da Classe.
     *
     * A fábrica de Botões de Controle possui a finalidade de transformar uma
     * abstração do Botão de Controle na forma de classe em um componente view
     * de fato para ser exibido na Tela Principal da aplicação.
     *
     * @param control_button Abstração do Botão de Controle Infravermelho
     */
    public InfraredButtonFactory(DeviceControlButton control_button){
        infrared_button = (InfraredButton) control_button;
    }

    /**
     * Método de fabricação do componente view
     *
     * @param activity Activity onde será exibido a View fabricada (RoomActivity)
     * @param manager_connection Gerente de Conexão utilizado no Botão de Controle
     * @param room_screen_layout Layout específico da Activity onde será exibido a View fabricada
     *
     * @return Componente view fabricado
     */
    public View generateControlButton(final Activity activity, final APIConnectionInterface manager_connection, final ViewGroup room_screen_layout) {
        final Button new_button_view = new Button(activity.getApplicationContext());

        new_button_view.setText(infrared_button.name_button);
        new_button_view.setTag(infrared_button.getId());

        /**
         * Evento onClick do Botão de Controle fabricado.
         *
         * Ao receber um evento de Click, o botão enviará as informações de
         * controle para o Sistema Embarcado tratá-las
         */
        new_button_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*System.out.println(getControlType());
                System.out.println(getLogicalPort());
                System.out.println(getFormatType());
                System.out.println(getNumBits());
                System.out.println(getInfraredCode());*/

                manager_connection.sendData(infrared_button.getControlType());
                manager_connection.sendData(infrared_button.getLogicalPort());
                manager_connection.sendData(infrared_button.getFormatType());
                manager_connection.sendData(infrared_button.getNumBits());
                manager_connection.sendData(infrared_button.getInfraredCode());
            }
        });

        /**
         * Evento OnLongClick (Pressionar) do Botão de Controle fabricado.
         *
         * Ao ser pressionado o Botão de Controle, será exibida uma janela para
         * edição do botão em questão ou a sua remoção da aplicação bem como do
         * Banco de Dados
         */
        new_button_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final DataBaseDAOImpl data_base = new DataBaseDAOImpl();

                //  Necessário para criar o popup de edição de botão
                final Dialog my_dialog = new Dialog(activity);
                my_dialog.setContentView(R.layout.edit_button_popup);

                GridLayout id_edit_button_layout = my_dialog.findViewById(R.id.IdEditButtonLayout);
                Button id_confirm_edit = my_dialog.findViewById(R.id.IdConfirmEdit);
                Button id_delete_button = my_dialog.findViewById(R.id.IdDeleteButton);

                final LayoutCreatorAdapter layout_creator_adapter = new InfraredLayoutAdapter(activity, id_edit_button_layout, id_confirm_edit);
                layout_creator_adapter.generateLayout();

                id_delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  Exclui o botão do banco de dados
                        if(data_base.removeControlButton(infrared_button)){
                            //  Exclui o botão da tela do usuário (layout)
                            room_screen_layout.removeView(room_screen_layout.findViewWithTag(new_button_view.getTag()));
                        }

                        //  Fecha a janela popup
                        my_dialog.dismiss();
                    }
                });

                id_confirm_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  Salva as novas configurações no banco de dados
                        if(layout_creator_adapter.updateButton(infrared_button)){
                            //  Atualiza o botão na tela do usuário (layout)
                            ((Button) room_screen_layout.findViewWithTag(new_button_view.getTag())).setText(infrared_button.getName());
                        }

                        //  Fecha a janela popup
                        my_dialog.dismiss();
                    }
                });

                my_dialog.show();

                return true;
            }
        });

        return new_button_view;
    }
}
