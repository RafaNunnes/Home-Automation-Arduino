package tcc.controller_bt.model;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Interface responsável pela aplicação do padrão Abstract Factory
 * na criação dos componentes Views de cada tipo de Botão de Controle
 */
public interface ButtonViewFactory {
    /**
     * Método que fabrica um componente View(Button/SeekBar) para ser exibido
     * na Tela Principal da aplicação
     *
     * @param activity Activity onde será exibido a View fabricada (RoomActivity)
     * @param manager_connection Gerente de Conexão utilizado no Botão de Controle
     * @param room_screen_layout Layout específico da Activity onde será exibido a View fabricada
     * @return Retorno da View fabricada
     */
    public View generateControlButton(final Activity activity, final APIConnectionInterface manager_connection, final ViewGroup room_screen_layout);
}
