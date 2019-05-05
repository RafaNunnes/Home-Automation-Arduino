package tcc.controller_bt.view;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

import tcc.controller_bt.R;
import tcc.controller_bt.model.APIConnectionInterface;
import tcc.controller_bt.model.BluetoothManagerAdapter;

/**
 * Activity responsável pela exibição dos dispositivos de conexão Bluetooth
 * pareados, assim como a sua efetiva escolha por parte do usuário.
 */
public class DevicesList extends AppCompatActivity {

    ListView id_list_devices;

    private BluetoothAdapter bluetooth_adapter;
    private ArrayAdapter paired_devices_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices_list);

        id_list_devices = (ListView) findViewById(R.id.IdLista);
    }

    @Override
    protected void onResume() {
        super.onResume();

        paired_devices_list = new ArrayAdapter(this,R.layout.devices_name);

        id_list_devices.setAdapter(paired_devices_list);
        id_list_devices.setOnItemClickListener(device_click_listener);

        //  Obtem o adaptador local bluetooth
        bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();

        //  Obtem o conjunto de dispositivos pareados
        Set<BluetoothDevice> paired_devices = bluetooth_adapter.getBondedDevices();

        if(paired_devices.size() > 0){
            //  Para cada dispositivo pareado, adiciona-se o mesmo na lista de exibição para o usuário
            for(BluetoothDevice device : paired_devices){
                paired_devices_list.add(device.getName() + "\n" + device.getAddress());
            }
        }

    }

    private AdapterView.OnItemClickListener device_click_listener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView av, View v, int arg2, long arg3) {
            //  Obter o endereço MAC do dispositivo, são os últimos 17 caracteres da View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            //  Configura o retorno da Activity (DevicesList)
            Intent intent = new Intent();
            intent.putExtra(BluetoothManagerAdapter.EXTRA_DEVICE_DATA, address);
            setResult(APIConnectionInterface.STATUS_CONNECTION,intent);

            //  Encerra a Activity (DevicesList)
            finish();
        }
    };
}
