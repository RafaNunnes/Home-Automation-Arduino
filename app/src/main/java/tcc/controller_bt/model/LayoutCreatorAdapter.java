package tcc.controller_bt.model;

/**
 * Interface de Layout de Criação dos Botões de Controle.
 *
 * Cada tipo de Botão de Controle necessita gerar um Layout
 * de exibição para o usuário digitar os Inputs referentes
 * ao tipo de controle (Chaveado/Dimmer/Infravermelho)
 *
 * Por meio desta interface, cada Botão de Controle poderá
 * ter o seu Layout de exibição de Inputs personalizado e
 * adaptado ao seu tipo de Controle
 */
public interface LayoutCreatorAdapter {
    public void generateLayout();

    public void sendNewButton();

    public boolean updateButton(DeviceControlButton control_button);
}
