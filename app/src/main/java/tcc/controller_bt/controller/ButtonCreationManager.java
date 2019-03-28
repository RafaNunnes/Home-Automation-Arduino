package tcc.controller_bt.controller;

import android.app.Activity;
import android.widget.Button;
import android.widget.GridLayout;

import tcc.controller_bt.model.DimmerLayoutAdapter;
import tcc.controller_bt.model.InfraredLayoutAdapter;
import tcc.controller_bt.model.LayoutCreatorAdapter;
import tcc.controller_bt.model.SwitchLayoutAdapter;

public class ButtonCreationManager {
    Activity button_creation_activity;
    GridLayout design_button_layout;
    Button confirm_button;

    LayoutCreatorAdapter layout_creator_adapter;

    public ButtonCreationManager(Activity activity, GridLayout layout, Button confirm_button){
        button_creation_activity = activity;
        design_button_layout = layout;
        this.confirm_button = confirm_button;
    }

    public void setSwitchLayout() {
        layout_creator_adapter = new SwitchLayoutAdapter(button_creation_activity, design_button_layout, confirm_button);
        layout_creator_adapter.generateLayout();
    }


    public void setDimmerLayout() {
        layout_creator_adapter = new DimmerLayoutAdapter(button_creation_activity, design_button_layout, confirm_button);
        layout_creator_adapter.generateLayout();
    }

    public void setInfraredLayout() {
        layout_creator_adapter = new InfraredLayoutAdapter(button_creation_activity, design_button_layout, confirm_button);
        layout_creator_adapter.generateLayout();
    }

    public void confirmButtonAttributes() {
        layout_creator_adapter.sendNewButton();
    }
}
