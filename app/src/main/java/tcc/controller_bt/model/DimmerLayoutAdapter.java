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

public class DimmerLayoutAdapter implements LayoutCreatorAdapter{
    private Activity button_creation_activity;
    private GridLayout design_button_layout;
    private DynamicViews dynamic_views;
    private Button confirm_button;

    private DataBaseDAOImpl data_base;

    // Inputs referentes ao Circuito Dimmer
    private EditText input_name, input_logical_port;

    public DimmerLayoutAdapter(Activity activity, GridLayout layout, Button button){
        design_button_layout = layout;
        dynamic_views = new DynamicViews(activity.getApplicationContext());
        confirm_button = button;
        button_creation_activity = activity;
        data_base = new DataBaseDAOImpl();
    }

    @Override
    public void generateLayout() {
        TextView name = dynamic_views.descriptionTextView("Nome: ");
        input_name = dynamic_views.descriptionEditText(InputType.TYPE_CLASS_TEXT);
        TextView logical_port = dynamic_views.descriptionTextView("Porta Lógica: ");
        input_logical_port = dynamic_views.descriptionEditText(InputType.TYPE_CLASS_NUMBER);

        design_button_layout.removeAllViews();
        confirm_button.setEnabled(false);
        design_button_layout.setColumnCount(2);

        input_name.addTextChangedListener(dimmerTextWatcher);
        input_logical_port.addTextChangedListener(dimmerTextWatcher);

        design_button_layout.addView(name);
        design_button_layout.addView(input_name);
        design_button_layout.addView(logical_port);
        design_button_layout.addView(input_logical_port);
    }

    private TextWatcher dimmerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            boolean confirm = (!input_name.getText().toString().trim().isEmpty()
                    && !input_logical_port.getText().toString().trim().isEmpty());
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

    @Override
    public void sendNewButton() {
        long id_returned = data_base.addControlButton(input_name.getText().toString(), DeviceControlButton.DIMMER_TYPE,
                Integer.valueOf(input_logical_port.getText().toString()), null, 0, 0);

        Intent intent = new Intent();
        intent.putExtra(ButtonCreationActivity.EXTRA_BUTTON_DATA, id_returned);
        button_creation_activity.setResult(APIConnectionInterface.STATUS_CONNECTION,intent);

        button_creation_activity.finish();
    }

    @Override
    public boolean updateButton(DeviceControlButton control_button) {
        control_button.setName(input_name.getText().toString());
        control_button.setLogicalPort(Byte.valueOf(input_logical_port.getText().toString()));

        return data_base.updateControlButton(control_button);
    }
}
