package tcc.controller_bt.model;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread
{
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    private final int handlerState = 0;
    private StringBuilder DataStringIN = new StringBuilder();

    private boolean infrared_flag;
    private String infrared_code_received;

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

    private Handler bluetoothIn = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == handlerState) {
                String readMessage = (String) msg.obj;
                DataStringIN.append(readMessage);

                int endOfLineIndex = DataStringIN.indexOf("#");

                if (endOfLineIndex > 0) {
                    String dataInPrint = DataStringIN.substring(0, endOfLineIndex);
                    //IdBufferIn.setText("Dato: " + dataInPrint);//<-<- PARTE A MODIFICAR >->->
                    DataStringIN.delete(0, DataStringIN.length());
                }
            }
        }
    };

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

                if (infrared_flag && !readMessage.isEmpty()) {
                    concat_message += readMessage;
                    if (concat_message.endsWith("#")) {
                        System.out.println("Mensagem Concatenada: " + concat_message);
                        //infrared_layout_adapter.decodeInfraredMessageReceived(concat_message);
                        setInfraredCodeReceived(concat_message);
                        concat_message = "";
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

    //Envio dos dados
    public void write(byte input) throws IOException
    {
        mmOutStream.write(input);
    }

    public void write(String infrared_code) throws IOException
    {
         mmOutStream.write(infrared_code.getBytes());
    }

    public void setInfraredFlag(boolean infrared_flag) {
        this.infrared_flag = infrared_flag;
    }

    public boolean getInfraredFlag() {
        return infrared_flag;
    }

    public String getInfraredCodeReceived() {
        return infrared_code_received;
    }

    private void setInfraredCodeReceived(String infrared_code_received) {
        this.infrared_code_received = infrared_code_received;
    }
}
