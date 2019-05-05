package tcc.controller_bt.model;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Classe de geração dinânima de componentes view
 */
public class DynamicViews {
    Context button_creation_context;

    /**
     * Construtor da Classe.
     *
     * @param context Contexto da Activity que receberá o componente view
     */
    public DynamicViews(Context context){
        button_creation_context = context;
    }

    /**
     * Método que gera um componente TextView padronizado, com um determinado
     * Texto passado pela aplicação
     *
     * @param text Texto determinado pela aplicação
     * @return Retorna o TextView fabricado neste método
     */
    public TextView descriptionTextView(String text){
        final ViewGroup.LayoutParams layout_param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView text_view = new TextView(button_creation_context);
        text_view.setLayoutParams(layout_param);
        text_view.setText(" "+ text + " ");
        text_view.setTextColor(Color.rgb(255,255,255));
        text_view.setMaxEms(8);

        return text_view;
    }

    /**
     * Método que gera um componente EditText padronizado, para receber dados
     * de um determinado tipo escolhido pela aplicação
     *
     * @param input_type Tipo do InputText (Texto, Número.. etc.)
     * @return Retorna o EditText fabricado neste método
     */
    public EditText descriptionEditText(int input_type){
        final ViewGroup.LayoutParams layout_param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText edit_text = new EditText(button_creation_context);
        edit_text.setLayoutParams(layout_param);
        edit_text.setMinEms(5);
        edit_text.setTextColor(Color.rgb(255,255,255));
        edit_text.setInputType(input_type);

        return edit_text;
    }
}
