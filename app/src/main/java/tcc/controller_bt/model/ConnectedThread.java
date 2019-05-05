package tcc.controller_bt.model;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Classe que implementa o funcionamento de uma Thread.
 * Esta Thread se encarrega de tratar da comunicação Bluetooth
 * com o Socket de conexão aberto pelo Gerente de Conexão, separando
 * esta atividade do fluxo de execução principal da aplicação.
 */
public class ConnectedThread extends Thread
{
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    private final int handlerState = 0;
    private StringBuilder DataStringIN = new StringBuilder();

    //  Flag destinada à ativação da coleta de informações do sinal Infravermelho
    private boolean infrared_flag;
    private String infrared_code_received;

    /**
     * Método Construtor da classe.
     *
     * A Thread, já no primeiro momento, adquire o InputStream e OutputStream
     * do Socket aberto pelo Gerente de Conexão.
     *
     * @param socket Socket Bluetooth aberto pelo Gerente de Conexão
     */
    public ConnectedThread(BluetoothSocket socket)
    {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        infrared_flag = false;
        try
        {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    /**
     * TODO: Este objeto será responsável, futuramente, pelo recebimento, em tempo de execução,
     * TODO: dos dados dos sensores instalados no Sistema Embarcado
     */
    private Handler bluetoothIn = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == handlerState) {
                String readMessage = (String) msg.obj;
                DataStringIN.append(readMessage);

                int endOfLineIndex = DataStringIN.indexOf("#");

                if (endOfLineIndex > 0) {
                    String dataInPrint = DataStringIN.substring(0, endOfLineIndex);
                    //IdBufferIn.setText("Dados: " + dataInPrint);
                    DataStringIN.delete(0, DataStringIN.length());
                }
            }
        }
    };

    /**
     * Implementação do Método Run da Thread
     */
    public void run()
    {
        byte[] buffer = new byte[256];
        int bytes;
        String concat_message = "";

        // Se mantem em modo escuta para determinar o ingresso de novos dados
        while (true) {
            try {
                bytes = mmInStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);

                //  Se a flag de recebimento de dados infravermelho estiver ativada
                //  e alguma mensagem tenha sido recebida através da comunicação com
                //  o Socket, então concatena-se esta mensagem
                if (infrared_flag && !readMessage.isEmpty()) {
                    //  Concatena a mensagem
                    concat_message += readMessage;

                    //  Encerra a concatenação ao receber o caractere '#'
                    if (concat_message.endsWith("#")) {
                        //System.out.println("Mensagem Concatenada: " + concat_message);
                        //  Armazena a mensagem concatenada completamente em uma String
                        setInfraredCodeReceived(concat_message);

                        //  Reseta a variável concat_message para futuras mensagens
                        concat_message = "";

                        //  Desativa a flag de recebimento de códigos do infravermelho
                        setInfraredFlag(false);
                    }
                } else {
                    // Envia os dado obtidos através do handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                }
            } catch (IOException e) {
                break;
            }
        }
    }

    /**
     * Método de envio dos dados do tipo Byte para o Sistema Embarcado
     */
    public void write(byte input) throws IOException
    {
        mmOutStream.write(input);
        System.out.println("Dado Enviado: " + input);
    }

    /**
     * Método de envio dos dados do tipo String para o Sistema Embarcado
     */
    public void write(String infrared_code) throws IOException
    {
         mmOutStream.write(infrared_code.getBytes());
    }

    /**
     * Método que altera o estado da flag de recebimento de códigos de infravermelho
     *
     * @param infrared_flag Valor de ativação (True) ou desativação (False) da flag
     */
    public void setInfraredFlag(boolean infrared_flag) {
        this.infrared_flag = infrared_flag;
    }

    /**
     * Método para retorno do estado atual da flag de códigos de infravermelho
     *
     * @return Estado atual da flag do infravermelho
     */
    public boolean getInfraredFlag() {
        return infrared_flag;
    }

    /**
     * Método para retorno do código infravermelho recebido do Sistema Embarcado
     *
     * @return Código infravermelho recebido do Sistema Embarcado
     */
    public String getInfraredCodeReceived() {
        return infrared_code_received;
    }

    /**
     * Método para armazenamento do código infravermelho recebido do Sistema Embarcado
     *
     * @param infrared_code_received Código infravermelho a ser armazenado
     */
    private void setInfraredCodeReceived(String infrared_code_received) {
        this.infrared_code_received = infrared_code_received;
    }
}
