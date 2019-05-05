package tcc.controller_bt.model;

/**
 * Classe que implementa a abstração do Botão de Controle do tipo Chaveado.
 */
public class SwitchButton extends DeviceControlButton {
    /**
     * Construtor do Classe SwitchButton (Botão Chaveado)
     *
     * @param id ID do Botão gerado automaticamente no Banco de Dados
     * @param name Nome do Botão escolhido pelo usuário
     * @param type Tipo de controle (Chaveado)
     * @param port Porta Lógica escolhida pelo usuário para controle no Sistema Embarcado
     */
    public SwitchButton(long id, String name, byte type, byte port){
        setControlType(type);
        setLogicalPort(port);
        setName(name);
        this.id = id;
    }

    /**
     * Método de retorno da Fábrica de Botões Chaveados (SwitchButtonFactory)
     *
     * @return Fábrica de Botões Chaveados
     */
    public ButtonViewFactory getFactory() {
        return new SwitchButtonFactory(SwitchButton.this);
    }
}
