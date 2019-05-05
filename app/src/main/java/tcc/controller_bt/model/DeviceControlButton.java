package tcc.controller_bt.model;

/**
 * Classe que abstrai os tipos de Botões de Controle
 * sendo eles atualmente: Chaveado, Dimmer e Infravermelho.
 *
 * Esta classe define abstratamente os tipos de Botões de Controle
 * contendo seus respectivos campos de ID, Nome, Tipo e Portas Lógicas
 * controláveis, bem como seus métodos Get e Set.
 */
public abstract class DeviceControlButton {
    public final static int SWITCH_TYPE = 1;
    public final static int DIMMER_TYPE = 2;
    public final static int INFRARED_TYPE = 3;

    protected long id;  //  ID do Botão de Controle
    protected String name_button;   //  Nome do Botão de Controle
    protected byte control_type;    //  Tipo do Botão de Controle
    protected byte logical_port;    //  Porta Lógica acionada pelo Botão de Controle

    public String getName(){
        return name_button;
    }

    public void setName(String name){
        this.name_button = name;
    }

    public long getId(){
        return id;
    }

    protected void setControlType(byte control_type) {
        this.control_type = control_type;
    }

    protected void setLogicalPort(byte logical_port) {
        this.logical_port = logical_port;
    }

    public byte getControlType() {
        return control_type;
    }

    public byte getLogicalPort() {
        return logical_port;
    }

    /**
     * Método onde cada Botão de Controle informará a sua respectiva
     * Fábrica no momento de criação do seu componente View na
     * Tela Principal da aplicação.
     *
     * @return Fábrica do Botão de Controle
     */
    public abstract ButtonViewFactory getFactory();
}
