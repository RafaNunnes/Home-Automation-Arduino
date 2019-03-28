package tcc.controller_bt.model;

import android.app.Activity;
import android.app.Dialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.SeekBar;

import tcc.controller_bt.R;

public class DimmerButton extends DeviceControlButton {
    private byte current_progress_value;

    public DimmerButton(long id, String name, byte type, byte port){
        setControlType(type);
        setLogicalPort(port);
        this.name_button = name;
        this.id = id;
    }

    public byte getCurrentProgressValue() {
        return current_progress_value;
    }

    public void setCurrentProgressValue(byte current_progress_value) {
        this.current_progress_value = current_progress_value;
    }

    @Override
    public View generateControlButton(final Activity activity, final APIConnectionInterface manager_connection, final ViewGroup room_screen_layout) {
        final SeekBar new_button = new SeekBar(activity.getApplicationContext());
        new_button.setTag(getId());
        new_button.setMax(120);

        new_button.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                manager_connection.sendData((byte) progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                manager_connection.sendData(getControlType());
                manager_connection.sendData(getLogicalPort());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                manager_connection.sendData((byte) -1);
            }
        });

        /*new_button.setOnLongClickListener(new View.OnLongClickListener() {
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
                        if(data_base.removeControlButton(DimmerButton.this)){
                            //  Exclui o botão da tela do usuário (layout)
                            room_screen_layout.removeView(room_screen_layout.findViewWithTag(new_button.getTag()));
                        }

                        //  Fecha a janela popup
                        my_dialog.dismiss();
                    }
                });

                id_confirm_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  Salva as novas configurações no banco de dados
                        layout_creator_adapter.updateButton(DimmerButton.this);
                            //  Atualiza os dados do botão
                            //SwitchButton updated_button = (SwitchButton) data_base.getControlButtonById(getId());
                            //setName(updated_button.getName());
                            //setLogicalPort(updated_button.getLogicalPort());

                            //  Atualiza o botão na tela do usuário (layout)
                            //((SeekBar) room_screen_layout.findViewWithTag(new_button.getTag())).setText(getName());

                        //  Fecha a janela popup
                        my_dialog.dismiss();
                    }
                });

                //my_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                my_dialog.show();

                return true;
            }
        });*/

        return new_button;
    }
}
