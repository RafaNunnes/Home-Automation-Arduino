package tcc.controller_bt.model;

/**
 * Interface de suporte à API de conexão, utilizada para
 * padronizar os diferentes tipos de Gerentes de Conexão
 * que podem adaptados para, por exemplo, Bluetooth, Ethernet
 * ou Wi-Fi
 *
 * O comentário sobre cada método é detalhado em cada classe
 * individual que implementa esta interface
 */
public interface APIConnectionInterface {
    public static String EXTRA_DEVICE_DATA = "device_data";
    public static int STATUS_CONNECTION = 1; // RequestCode

    public void connect(String connection_content);

    public void disconnect();

    public void sendData(byte data);

    public void sendData(String data);

    public int receiveData();

    public void verifyConnectionStatus();
}
