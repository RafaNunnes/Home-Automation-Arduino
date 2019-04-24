package tcc.controller_bt.model;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import tcc.controller_bt.R;

public class SwitchButtonFactory implements ButtonViewFactory {
    SwitchButton switch_button;

    public SwitchButtonFactory(DeviceControlButton control_button){
        switch_button = (SwitchButton) control_button;
    }

    public View generateControlButton(final Activity activity, final APIConnectionInterface manager_connection, final ViewGroup room_screen_layout) {
        //final Button new_button_view = new Button(context);
        final Button new_button_view = new Button(activity.getApplicationContext());

        new_button_view.setText(switch_button.name_button);
        //  Tag utilizada para editar/remover o botão
        new_button_view.setTag(switch_button.getId());
        new_button_view.setPadding(35,35,35,35);

        new_button_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager_connection.sendData(switch_button.getControlType());
                manager_connection.sendData(switch_button.getLogicalPort());
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

                final LayoutCreatorAdapter layout_creator_adapter = new SwitchLayoutAdapter(activity, id_edit_button_layout, id_confirm_edit);
                layout_creator_adapter.generateLayout();

                id_delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  Exclui o botão do banco de dados
                        if(data_base.removeControlButton(switch_button)){
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
                        if(layout_creator_adapter.updateButton(switch_button)){
                            //  Atualiza os dados do botão
                            //SwitchButton updated_button = (SwitchButton) data_base.getControlButtonById(getId());
                            //setName(updated_button.getName());
                            //setLogicalPort(updated_button.getLogicalPort());

                            //  Atualiza o botão na tela do usuário (layout)
                            ((Button) room_screen_layout.findViewWithTag(new_button_view.getTag())).setText(switch_button.getName());
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

        /*new_button_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motion_event) {
                final int x = (int) motion_event.getRawX();
                final int y = (int) motion_event.getRawY();
                int xDelta=0, yDelta=0;

                switch (motion_event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_UP:
                        Toast.makeText(context,
                                "I'm here!", Toast.LENGTH_SHORT)
                                .show();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;
                }

                room_linear_layout.invalidate();
                return true;
            }
        });*/

        return new_button_view;
    }
}
