package tcc.controller_bt.model;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread
{
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public ConnectedThread(BluetoothSocket socket)
    {
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try
        {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    /*public void run()
    {
        byte[] buffer = new byte[256];
        int bytes;

        // Se mantiene en modo escucha para determinar el ingreso de datos
        while (true) {
            try {
                bytes = mmInStream.read(buffer);
                String readMessage = new String(buffer, 0, bytes);
                // Envia los datos obtenidos hacia el evento via handler
                bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }*/
    //Envio dos dados
    public void write(byte input) throws IOException
    {
        mmOutStream.write(input);

        /*if(infrared_code != null)
        {
            mmOutStream.write(infrared_code.getBytes());

            char[] code = String.valueOf(infrared_code).toCharArray();

            for(char chars = 0; chars<code.length; chars++)
            {
                System.out.println(code[chars]);
                mmOutStream.write(code[chars]);
            }
        }*/
    }
    public void write(String infrared_code) throws IOException
    {
         mmOutStream.write(infrared_code.getBytes());
    }
}
