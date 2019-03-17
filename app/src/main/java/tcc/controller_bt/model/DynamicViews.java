package tcc.controller_bt.model;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class DynamicViews {
    Context button_creation_context;

    public DynamicViews(Context context){
        button_creation_context = context;
    }

    public TextView descriptionTextView(String text){
        final ViewGroup.LayoutParams layout_param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView text_view = new TextView(button_creation_context);
        text_view.setLayoutParams(layout_param);
        text_view.setText(" "+ text + " ");
        text_view.setTextColor(Color.rgb(0,0,0));
        text_view.setMaxEms(8);

        return text_view;
    }

    public EditText descriptionEditText(int input_type){
        final ViewGroup.LayoutParams layout_param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText edit_text = new EditText(button_creation_context);
        edit_text.setMinEms(2);
        edit_text.setTextColor(Color.rgb(0,0,0));
        edit_text.setInputType(input_type);

        return edit_text;
    }
}
