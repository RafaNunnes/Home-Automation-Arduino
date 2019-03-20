package tcc.controller_bt.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DataBaseDAOImpl implements DataBaseDAO {
    private static int UID = 0;
    private static int NAME = 1;
    private static int TYPE = 2;
    private static int PORT = 3;
    private static int CODE = 4;
    private static int FORMAT = 5;
    private static int BITS = 6;

    SQLiteDatabase data_base;
    private DeviceControlButton device_control_button;

    @Override
    public void createTable() {
        data_base = DataBase.getInstance().getWritableDatabase();
        String columns = "(UID integer primary key autoincrement, NAME text," +
                "TYPE integer, PORT integer, CODE text, FORMAT integer, BITS integer)";
        String query = "CREATE TABLE IF NOT EXISTS " + DataBase.BUTTON_TABLE + columns;
        data_base.execSQL(query);
    }

    @Override
    public ArrayList<DeviceControlButton> getControlButtons() {
        data_base = DataBase.getInstance().getReadableDatabase();
        String query = "SELECT * FROM " + DataBase.BUTTON_TABLE;
        ArrayList<DeviceControlButton> buttons = new ArrayList<>();
        Cursor cursor = data_base.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {
                switch (cursor.getInt(DataBaseDAOImpl.TYPE)){
                    case DeviceControlButton.SWITCH_TYPE:
                        device_control_button = new SwitchButton(cursor.getInt(DataBaseDAOImpl.UID), cursor.getString(DataBaseDAOImpl.NAME), (byte) cursor.getInt(DataBaseDAOImpl.TYPE), (byte) cursor.getInt(DataBaseDAOImpl.PORT));
                        break;

                    case DeviceControlButton.DIMMER_TYPE:
                        device_control_button = new DimmerButton(cursor.getInt(DataBaseDAOImpl.UID), cursor.getString(DataBaseDAOImpl.NAME), (byte) cursor.getInt(DataBaseDAOImpl.TYPE), (byte) cursor.getInt(DataBaseDAOImpl.PORT));
                        break;

                    case DeviceControlButton.INFRARED_TYPE:
                        device_control_button = new InfraredButton(cursor.getInt(DataBaseDAOImpl.UID), cursor.getString(DataBaseDAOImpl.NAME), (byte) cursor.getInt(DataBaseDAOImpl.TYPE), (byte) cursor.getInt(DataBaseDAOImpl.PORT),
                                cursor.getString(DataBaseDAOImpl.CODE), (byte) cursor.getInt(DataBaseDAOImpl.FORMAT), (byte) cursor.getInt(DataBaseDAOImpl.BITS));
                        break;
                }

                //  Ao final de cada interação, adiciona o botão ao ArrayList que será retornado com todos os botões cadastrados
                buttons.add(device_control_button);

            }while(cursor.moveToNext());
        }

        cursor.close();
        return buttons;
    }

    @Override
    public DeviceControlButton getControlButtonById(long id) {
        data_base = DataBase.getInstance().getReadableDatabase();
        String query = "SELECT * FROM " + DataBase.BUTTON_TABLE + " where UID = '" + id + "'";
        Cursor cursor = data_base.rawQuery(query,null);

        if(cursor.moveToFirst()){
            switch (cursor.getInt(DataBaseDAOImpl.TYPE)){
                case DeviceControlButton.SWITCH_TYPE:
                    device_control_button = new SwitchButton(cursor.getInt(DataBaseDAOImpl.UID), cursor.getString(DataBaseDAOImpl.NAME), (byte) cursor.getInt(DataBaseDAOImpl.TYPE), (byte) cursor.getInt(DataBaseDAOImpl.PORT));
                    break;

                case DeviceControlButton.DIMMER_TYPE:
                    device_control_button = new DimmerButton(cursor.getInt(DataBaseDAOImpl.UID), cursor.getString(DataBaseDAOImpl.NAME), (byte) cursor.getInt(DataBaseDAOImpl.TYPE), (byte) cursor.getInt(DataBaseDAOImpl.PORT));
                    break;

                case DeviceControlButton.INFRARED_TYPE:
                    device_control_button = new InfraredButton(cursor.getInt(DataBaseDAOImpl.UID), cursor.getString(DataBaseDAOImpl.NAME), (byte) cursor.getInt(DataBaseDAOImpl.TYPE), (byte) cursor.getInt(DataBaseDAOImpl.PORT),
                            cursor.getString(DataBaseDAOImpl.CODE), (byte) cursor.getInt(DataBaseDAOImpl.FORMAT), (byte) cursor.getInt(DataBaseDAOImpl.BITS));
                    break;
            }
        }

        cursor.close();
        return device_control_button;
    }

    @Override
    public long addControlButton(String control_name, int control_type, int logical_port, String infrared_code, int format_type, int num_bits) {
        data_base = DataBase.getInstance().getWritableDatabase();
        ContentValues content_values = new ContentValues();

        switch (control_type){
            case DeviceControlButton.SWITCH_TYPE:
                content_values.put("NAME", control_name);
                content_values.put("TYPE", control_type);
                content_values.put("PORT", logical_port);
                break;
            case DeviceControlButton.DIMMER_TYPE:
                content_values.put("NAME", control_name);
                content_values.put("TYPE", control_type);
                content_values.put("PORT", logical_port);
                break;
            case DeviceControlButton.INFRARED_TYPE:
                content_values.put("NAME", control_name);
                content_values.put("TYPE", control_type);
                content_values.put("PORT", logical_port);
                content_values.put("CODE", infrared_code);
                content_values.put("FORMAT", format_type);
                content_values.put("BITS", num_bits);
                break;
        }

        //  Retorna o id gerado incrementalmente na tabela
        return data_base.insert(DataBase.BUTTON_TABLE, null, content_values);
    }

    @Override
    public boolean updateControlButton(DeviceControlButton control_button) {
        data_base = DataBase.getInstance().getWritableDatabase();
        ContentValues content_values = new ContentValues();

        switch (control_button.control_type){
            case DeviceControlButton.SWITCH_TYPE:
                content_values.put("NAME", control_button.getName());
                content_values.put("TYPE", control_button.getControlType());
                content_values.put("PORT", control_button.getLogicalPort());
                break;
            case DeviceControlButton.DIMMER_TYPE:
                content_values.put("NAME", control_button.getName());
                content_values.put("TYPE", control_button.getControlType());
                content_values.put("PORT", control_button.getLogicalPort());
                break;
            case DeviceControlButton.INFRARED_TYPE:
                content_values.put("NAME", control_button.getName());
                content_values.put("TYPE", control_button.getControlType());
                content_values.put("PORT", control_button.getLogicalPort());
                content_values.put("CODE", ((InfraredButton) control_button).getInfraredCode());
                content_values.put("FORMAT", ((InfraredButton) control_button).getFormatType());
                content_values.put("BITS", ((InfraredButton) control_button).getNumBits());
                break;
        }

        String where = "UID = '" + control_button.getId() + "'";

        //  Retorna True caso o número de linhas afetadas na tabela seja maior do que 0 (zero), e False caso não afete linha alguma
        return data_base.update(DataBase.BUTTON_TABLE, content_values, where, null) > 0;

    }

    @Override
    public void removeTable() {
        data_base = DataBase.getInstance().getWritableDatabase();
        String query = "DROP TABLE IF EXISTS " + DataBase.BUTTON_TABLE;
        data_base.execSQL(query);
    }

    @Override
    public boolean removeControlButton(DeviceControlButton control_button) {
        data_base = DataBase.getInstance().getWritableDatabase();
        String where = "UID = '" + control_button.getId() + "'";

        //  Retorna True caso consiga deletar o botão da tabela, e False caso contrário
        return data_base.delete(DataBase.BUTTON_TABLE, where, null) > 0;
    }
}
