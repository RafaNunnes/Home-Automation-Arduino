package tcc.controller_bt.model;

/**
 * Classe que implementa a abstração do Botão de Controle do tipo Dimmer.
 */
public class DimmerButton extends DeviceControlButton {
    //  Valor atual da barra de progresso do SeekBar
    private byte current_progress_value;

    /**
     * Construtor do Classe DimmerButton (Botão Dimmer)
     *
     * @param id ID do Botão gerado automaticamente no Banco de Dados
     * @param name Nome do Botão escolhido pelo usuário
     * @param type Tipo de controle (Dimmer)
     * @param port Porta Lógica escolhida pelo usuário para controle no Sistema Embarcado
     */
    public DimmerButton(long id, String name, byte type, byte port){
        setControlType(type);
        setLogicalPort(port);
        this.name_button = name;
        this.id = id;
    }


    /**
     * Método de retorno da Fábrica de Botões Dimmer (DimmerButtonFactory)
     *
     * @return Fábrica de Botões Dimmer
     */
    public ButtonViewFactory getFactory() {
        return new DimmerButtonFactory(DimmerButton.this);
    }

    public byte getCurrentProgressValue() {
        return current_progress_value;
    }

    public void setCurrentProgressValue(byte current_progress_value) {
        this.current_progress_value = current_progress_value;
    }
}
