package tcc.controller_bt.controller;

import android.app.Activity;
import android.widget.Button;
import android.widget.GridLayout;

import tcc.controller_bt.model.LayoutCreatorFacade;

public class ButtonCreationManager {
    Activity button_creation_activity;
    GridLayout design_button_layout;

    LayoutCreatorFacade layout_creator_facade;

    public ButtonCreationManager(Activity activity, GridLayout layout, Button confirm_button){
        button_creation_activity = activity;
        design_button_layout = layout;

        layout_creator_facade = new LayoutCreatorFacade(button_creation_activity, design_button_layout, confirm_button);
    }

    public void setSwitchLayout() {
        layout_creator_facade.generateSwitchLayout();
    }


    public void setDimmerLayout() {
        layout_creator_facade.generateDimmerLayout();
    }

    public void setInfraredLayout() {
        layout_creator_facade.generateInfraredLayout();
    }

    public void confirmButtonAttributes() {
        layout_creator_facade.sendNewButton();
    }
}
