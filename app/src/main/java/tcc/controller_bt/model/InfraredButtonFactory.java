package tcc.controller_bt.model;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import tcc.controller_bt.R;

public class InfraredButtonFactory implements ButtonViewFactory {
    InfraredButton infrared_button;

    public InfraredButtonFactory(DeviceControlButton control_button){
        infrared_button = (InfraredButton) control_button;
    }

    public View generateControlButton(final Activity activity, final APIConnectionInterface manager_connection, final ViewGroup room_screen_layout) {
        final Button new_button_view = new Button(activity.getApplicationContext());

        new_button_view.setText(infrared_button.name_button);
        new_button_view.setTag(infrared_button.getId());

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
                            //  Atualiza os dados do botão
                            //SwitchButton updated_button = (SwitchButton) data_base.getControlButtonById(getId());
                            //setName(updated_button.getName());
                            //setLogicalPort(updated_button.getLogicalPort());

                            //  Atualiza o botão na tela do usuário (layout)
                            ((Button) room_screen_layout.findViewWithTag(new_button_view.getTag())).setText(infrared_button.getName());
                        }

                        //  Fecha a janela popup
                        my_dialog.dismiss();
                    }
                });

                //my_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                my_dialog.show();

                return true;
            }
        });

        return new_button_view;
    }
}
