package tcc.controller_bt.model;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import tcc.controller_bt.R;

public class InfraredButton extends DeviceControlButton {
    private String infrared_code;
    private byte format_type;
    private byte num_bits;

    public InfraredButton(long id, String name, byte type, byte port, String code, byte format, byte bits){
        setControlType(type);
        setLogicalPort(port);
        setInfraredCode(code);
        setFormatType(format);
        setNumBits(bits);
        this.name_button = name;
        this.id = id;
    }

    public String getInfraredCode() {
        return infrared_code;
    }

    public void setInfraredCode(String infrared_code) {
        this.infrared_code = infrared_code;
    }

    public byte getFormatType() {
        return format_type;
    }

    public void setFormatType(byte format_type) {
        this.format_type = format_type;
    }

    public byte getNumBits() {
        return num_bits;
    }

    public void setNumBits(byte num_bits) {
        this.num_bits = num_bits;
    }

    @Override
    public View generateControlButton(final Activity activity, final APIConnectionInterface manager_connection, final ViewGroup room_screen_layout) {
        final Button new_button = new Button(activity.getApplicationContext());

        new_button.setText(name_button);
        new_button.setTag(getId());

        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(getControlType());
                System.out.println(getLogicalPort());
                System.out.println(getFormatType());
                System.out.println(getNumBits());
                System.out.println(getInfraredCode());

                manager_connection.sendData(getControlType());
                manager_connection.sendData(getLogicalPort());
                manager_connection.sendData(getFormatType());
                manager_connection.sendData(getNumBits());
                manager_connection.sendData(getInfraredCode());
                /*manager_connection.sendData((byte) 3);
                manager_connection.sendData((byte) 3);
                manager_connection.sendData((byte) 127);
                manager_connection.sendData((byte) 32);
                manager_connection.sendData("3772793023");*/
            }
        });

        new_button.setOnLongClickListener(new View.OnLongClickListener() {
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
                        if(data_base.removeControlButton(InfraredButton.this)){
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
                        if(layout_creator_adapter.updateButton(InfraredButton.this)){
                            //  Atualiza os dados do botão
                            //SwitchButton updated_button = (SwitchButton) data_base.getControlButtonById(getId());
                            //setName(updated_button.getName());
                            //setLogicalPort(updated_button.getLogicalPort());

                            //  Atualiza o botão na tela do usuário (layout)
                            ((Button) room_screen_layout.findViewWithTag(new_button.getTag())).setText(getName());
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

        return new_button;
    }
}
