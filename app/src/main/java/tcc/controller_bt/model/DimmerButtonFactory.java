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

public class DimmerButtonFactory implements ButtonViewFactory {
    DimmerButton dimmer_button;

    public DimmerButtonFactory(DeviceControlButton control_button){
        dimmer_button = (DimmerButton) control_button;
    }

    public View generateControlButton(final Activity activity, final APIConnectionInterface manager_connection, final ViewGroup room_screen_layout) {
        final SeekBar new_seek_bar_view = new SeekBar(activity.getApplicationContext());
        new_seek_bar_view.setMax(120);

        final LinearLayout package_contents_dimmer = new LinearLayout(activity.getApplicationContext());
        package_contents_dimmer.setOrientation(LinearLayout.VERTICAL);
        package_contents_dimmer.setTag(dimmer_button.getId());

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

        final TextView dimmer_text_name = new TextView(activity.getApplicationContext());
        dimmer_text_name.setTextColor(Color.rgb(0,0,0));
        dimmer_text_name.setText(dimmer_button.getName());

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
                            dimmer_text_name.setText(dimmer_button.getName());
                        }

                        //  Atualiza os dados do botão
                        //SwitchButton updated_button = (SwitchButton) data_base.getControlButtonById(getId());
                        //setName(updated_button.getName());
                        //setLogicalPort(updated_button.getLogicalPort());

                        //  Atualiza o botão na tela do usuário (layout)
                        //((SeekBar) room_screen_layout.findViewWithTag(new_seek_bar_view.getTag())).setText(getName());

                        //  Fecha a janela popup
                        my_dialog.dismiss();
                    }
                });

                //my_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
