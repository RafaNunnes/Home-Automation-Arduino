package tcc.controller_bt.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SwitchButton extends DeviceControlButton {

    public SwitchButton(long id, String name, byte type, byte port){
        setControlType(type);
        setLogicalPort(port);
        this.name_button = name;
        this.id = id;
    }

    @Override
    public View generateControlButton(final Context context, final APIConnectionInterface manager_connection, final ViewGroup room_linear_layout) {
        final Button new_button = new Button(context);

        new_button.setText(name_button);
        new_button.setTag(1);

        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager_connection.sendData(getControlType());
                manager_connection.sendData(getLogicalPort());
            }
        });

        /*new_button.setOnTouchListener(new View.OnTouchListener() {
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

        return new_button;
    }
}
