package tcc.controller_bt.controller;

import android.app.Activity;
import android.widget.Button;
import android.widget.GridLayout;

import tcc.controller_bt.model.DimmerLayoutAdapter;
import tcc.controller_bt.model.InfraredLayoutAdapter;
import tcc.controller_bt.model.LayoutCreatorAdapter;
import tcc.controller_bt.model.SwitchLayoutAdapter;

/**
 * Classe Gerente da View(MVC) de Criação de Botões de Controle (ButtonCreationActivity).
 */
public class ButtonCreationManager {
    Activity button_creation_activity;
    GridLayout design_button_layout;
    Button confirm_button;

    LayoutCreatorAdapter layout_creator_adapter;

    /**
     * Construtor da Classe.
     *
     * @param activity Activity onde será exibido o layout de cada Input dos Botões de Controle
     * @param layout Layout específico dentro da Activity acima
     * @param confirm_button Botão de confirmação dos Inputs do usuário
     */
    public ButtonCreationManager(Activity activity, GridLayout layout, Button confirm_button){
        button_creation_activity = activity;
        design_button_layout = layout;
        this.confirm_button = confirm_button;
    }

    /**
     * Configura o Layout para exibição das caixas de texto de entrada de dados
     * do usuário referente ao Botão de Controle Chaveado
     */
    public void setSwitchLayout() {
        layout_creator_adapter = new SwitchLayoutAdapter(button_creation_activity, design_button_layout, confirm_button);
        layout_creator_adapter.generateLayout();
    }

    /**
     * Configura o Layout para exibição das caixas de texto de entrada de dados
     * do usuário referente ao Botão de Controle Dimmer
     */
    public void setDimmerLayout() {
        layout_creator_adapter = new DimmerLayoutAdapter(button_creation_activity, design_button_layout, confirm_button);
        layout_creator_adapter.generateLayout();
    }

    /**
     * Configura o Layout para exibição das caixas de texto de entrada de dados
     * do usuário referente ao Botão de Controle Infravermelho
     */
    public void setInfraredLayout() {
        layout_creator_adapter = new InfraredLayoutAdapter(button_creation_activity, design_button_layout, confirm_button);
        layout_creator_adapter.generateLayout();
    }

    /**
     * Método que finaliza a criação do Botão de Controle, enviando o novo botão
     * para a Activity responsável pela sua exibição para o usuário (RoomActivity)
     */
    public void confirmButtonAttributes() {
        layout_creator_adapter.sendNewButton();
    }
}
