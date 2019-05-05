package tcc.controller_bt.model;

/**
 * Classe que implementa a abstração do Botão de Controle do tipo Infravermelho.
 */
public class InfraredButton extends DeviceControlButton {
    private String infrared_code;
    private byte format_type;
    private byte num_bits;

    /**
     * Construtor do Classe InfraredButton (Botão Infravermelho)
     *
     * @param id ID do Botão gerado automaticamente no Banco de Dados
     * @param name Nome do Botão escolhido pelo usuário
     * @param type Tipo de controle (Infravermelho)
     * @param port Porta Lógica escolhida pelo Sistema Embarcado para controle do Infravermelho
     * @param code Código Infravermelho recebido pelo Sistema Embarcado
     * @param format Formato do fabricante do controle infravermelho
     * @param bits Número de bits do código infravermelho
     */
    public InfraredButton(long id, String name, byte type, byte port, String code, byte format, byte bits){
        setControlType(type);
        setLogicalPort(port);
        setInfraredCode(code);
        setFormatType(format);
        setNumBits(bits);
        this.name_button = name;
        this.id = id;
    }

    /**
     * Método de retorno da Fábrica de Botões Infravermelho (InfraredButtonFactory)
     *
     * @return Fábrica de Botões Infravermelho
     */
    public ButtonViewFactory getFactory() {
        return new InfraredButtonFactory(InfraredButton.this);
    }

    public String getInfraredCode() {
        return infrared_code;
    }

    public void setInfraredCode(String infrared_code) {
        this.infrared_code = infrared_code;
    }

    public byte getFormatType() {
        return format_type;
    }

    public void setFormatType(byte format_type) {
        this.format_type = format_type;
    }

    public byte getNumBits() {
        return num_bits;
    }

    public void setNumBits(byte num_bits) {
        this.num_bits = num_bits;
    }
}
