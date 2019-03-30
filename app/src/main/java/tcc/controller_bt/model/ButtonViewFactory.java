package tcc.controller_bt.model;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public interface ButtonViewFactory {
    public View generateControlButton(final Activity activity, final APIConnectionInterface manager_connection, final ViewGroup room_screen_layout);
}
