package tcc.controller_bt.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Implementação da Interface de CRUD do Banco de Dados
 */
public class DataBaseDAOImpl implements DataBaseDAO {
    //  Macros para utilização no Banco de Dados
    private static int UID = 0;
    private static int NAME = 1;
    private static int TYPE = 2;
    private static int PORT = 3;
    private static int CODE = 4;
    private static int FORMAT = 5;
    private static int BITS = 6;

    SQLiteDatabase data_base;
    private DeviceControlButton device_control_button;

    /**
     * Método que cria uma tabela, caso ainda não exista, no Banco de Dados
     */
    public void createTable() {
        data_base = DataBase.getInstance().getWritableDatabase();
        String columns = "(UID integer primary key autoincrement, NAME text," +
                "TYPE integer, PORT integer, CODE text, FORMAT integer, BITS integer)";
        String query = "CREATE TABLE IF NOT EXISTS " + DataBase.BUTTON_TABLE + columns;
        data_base.execSQL(query);
    }

    /**
     * Método de leitura do Banco de Dados com a finalidade de carregar todos
     * os Botões de Controle cadastrados e retorná-los para a aplicação
     *
     * @return Lista com todos os Botões de Controle cadastrados
     */
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
        //  Retorna o ArrayList com todos os Botões de Controle
        return buttons;
    }

    /**
     * Método de leitura do Banco de Dados com a finalidade de buscar um
     * Botão de Controle pelo seu respectivo ID e retorná-los para a aplicação
     *
     * @param id ID do Botão de Controle que se deseja buscar
     * @return Botão de Controle com o ID especificado
     */
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
        //  Retorna o Botão de Controle com o ID especificado
        return device_control_button;
    }

    /**
     * Método que adiciona um Botão de Controle ao Banco de Dados
     *
     * @param control_name Nome do Botão de Controle que será cadastrado
     * @param control_type Tipo de controle do Botão de Controle
     * @param logical_port Porta Lógica do Sistema Embarcado controlada pelo Botão de Controle
     * @param infrared_code Código Infravermelho, caso o Botão de Controle seja do tipo Infravermelho
     * @param format_type Formato do fabricante do infravermelho, caso o Botão de Controle seja do tipo Infravermelho
     * @param num_bits Número de bits do código infravermelho, caso o Botão de Controle seja do tipo Infravermelho
     *
     * @return ID gerado pelo automaticamente pelo Banco de Dados para o Botão de Controle adicionado
     */
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

        //  Retorna o ID gerado incrementalmente na tabela
        return data_base.insert(DataBase.BUTTON_TABLE, null, content_values);
    }

    /**
     * Método para atualização de um Botão de Controle já cadastrado no Banco de Dados
     *
     * @param control_button Botão de Controle atualizado pela aplicação e que deve ser atualizado
     *                       no Banco de Dados
     *
     * @return True, em caso de sucesso na atualização, e False, caso contrário
     */
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

    /**
     * Método para remoção de uma Tabela, caso a mesma exista, no Banco de Dados
     */
    public void removeTable() {
        data_base = DataBase.getInstance().getWritableDatabase();
        String query = "DROP TABLE IF EXISTS " + DataBase.BUTTON_TABLE;
        data_base.execSQL(query);
    }

    /**
     * Método para remoção de um Botão de Controle, caso o mesmo exista, no Banco de Dados
     *
     * @param control_button Botão de Controle a ser removido
     *
     * @return True, em caso de sucesso na remoção, e False, caso contrário
     */
    public boolean removeControlButton(DeviceControlButton control_button) {
        data_base = DataBase.getInstance().getWritableDatabase();
        String where = "UID = '" + control_button.getId() + "'";

        //  Retorna True caso consiga deletar o botão da tabela, e False caso contrário
        return data_base.delete(DataBase.BUTTON_TABLE, where, null) > 0;
    }
}
