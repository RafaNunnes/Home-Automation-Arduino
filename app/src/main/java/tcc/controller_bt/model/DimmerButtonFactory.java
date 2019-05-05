package tcc.controller_bt.model;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import tcc.controller_bt.R;

/**
 * Classe que implementa a Interface ButtonViewFactory, ou seja, a
 * abstração de uma Fábrica segundo o padrão de projeto Abstract Factory.
 *
 * Esta classe representa a fábrica de componentes view Button, pois o
 * tipo Botão de Controle é o Dimmer.
 */
public class DimmerButtonFactory implements ButtonViewFactory {
    DimmerButton dimmer_button;

    /**
     * Construtor da Classe.
     *
     * A fábrica de Botões de Controle possui a finalidade de transformar uma
     * abstração do Botão de Controle na forma de classe em um componente view
     * de fato para ser exibido na Tela Principal da aplicação.
     *
     * @param control_button Abstração do Botão de Controle Dimmer
     */
    public DimmerButtonFactory(DeviceControlButton control_button){
        dimmer_button = (DimmerButton) control_button;
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
        final SeekBar new_seek_bar_view = new SeekBar(activity.getApplicationContext());
        new_seek_bar_view.setMax(120);
        new_seek_bar_view.setPadding(30,5,50,20);

        final LinearLayout package_contents_dimmer = new LinearLayout(activity.getApplicationContext());
        package_contents_dimmer.setOrientation(LinearLayout.VERTICAL);
        package_contents_dimmer.setTag(dimmer_button.getId());

        /**
         * Evento OnSeekBarChangeListener do Botão de Controle Dimmer.
         *
         * Ao iniciar a interação com o componente SeekBar na Tela Principal, a aplicação
         * enviará o comando de controle do tipo Dimmer, e logo em seguida, enviará todos
         * os valores do SeekBar à medida que o usuário arrasta o cursor.
         *
         * Ao final da interação, quando o usuário solta o componente SeekBar, a aplicação
         * enviará um comando para o Sistema Embarcado informando que a comunicação terminou.
         */
        new_seek_bar_view.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                manager_connection.sendData((byte) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                manager_connection.sendData(dimmer_button.getControlType());
                manager_connection.sendData(dimmer_button.getLogicalPort());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                manager_connection.sendData((byte) -1);
            }
        });

        //  Fabricação do Texto que será exibido acima do componente SeekBar, referindo-se
        //  ao nome do Controle escolhido pelo usuário no momento de sua criação
        final TextView dimmer_text_name = new TextView(activity.getApplicationContext());
        dimmer_text_name.setTextColor(Color.rgb(255,255,255));
        dimmer_text_name.setText(dimmer_button.getName());
        dimmer_text_name.setTextSize(17);
        dimmer_text_name.setPadding(25,25,25,0);

        /**
         * Evento OnLongClick (Pressionar) do Botão de Controle fabricado.
         *
         * Ao ser pressionado o Texto com o nome do Botão de Controle, será exibida
         * uma janela para edição do botão em questão ou a sua remoção da aplicação
         * bem como do Banco de Dados
         */
        dimmer_text_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final DataBaseDAOImpl data_base = new DataBaseDAOImpl();

                //  Necessário para criar o popup de edição de botão
                final Dialog my_dialog = new Dialog(activity);
                my_dialog.setContentView(R.layout.edit_button_popup);

                GridLayout id_edit_button_layout = my_dialog.findViewById(R.id.IdEditButtonLayout);
                Button id_confirm_edit = my_dialog.findViewById(R.id.IdConfirmEdit);
                Button id_delete_button = my_dialog.findViewById(R.id.IdDeleteButton);

                final LayoutCreatorAdapter layout_creator_adapter = new DimmerLayoutAdapter(activity, id_edit_button_layout, id_confirm_edit);
                layout_creator_adapter.generateLayout();

                id_delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  Exclui o botão do banco de dados
                        if(data_base.removeControlButton(dimmer_button)){
                            //  Exclui o botão da tela do usuário (layout)
                            room_screen_layout.removeView(room_screen_layout.findViewWithTag(package_contents_dimmer.getTag()));
                        }

                        //  Fecha a janela popup
                        my_dialog.dismiss();
                    }
                });

                id_confirm_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  Salva as novas configurações no banco de dados
                        if(layout_creator_adapter.updateButton(dimmer_button)){
                            //  Atualiza o botão na tela do usuário (layout)
                            dimmer_text_name.setText(dimmer_button.getName());
                        }

                        //  Fecha a janela popup
                        my_dialog.dismiss();
                    }
                });

                my_dialog.show();

                return true;
            }
        });

        //  Adiciona o TextView com o nome do Dimmer e o SeekBar em si ao linearlayout criado
        package_contents_dimmer.addView(dimmer_text_name);
        package_contents_dimmer.addView(new_seek_bar_view);

        //  Retorna a View encapsulada
        return package_contents_dimmer;
    }
}
