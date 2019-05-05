package tcc.controller_bt.model;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe que encapsula o Banco de Dados SQLite
 */
public class DataBase extends SQLiteOpenHelper {
    private static String DB_NAME = "DB";
    private static int DB_VERSION = 1;
    // Nome da Tabela onde se encontram os Botões de Controle criados
    public static String BUTTON_TABLE = "BUTTON_TABLE";

    private static DataBase instance; // Singleton

    private DataBase() {
        super(MyApp.getContext(), DB_NAME, null, DB_VERSION);
    }

    /**
     * Método que aplica o padrão Singleton no acesso ao Banco de Dados
     *
     * @return Instância única do Banco de Dados para toda a aplicação
     */
    public static DataBase getInstance(){
        if(instance==null)
        {
            instance = new DataBase();
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Método de encerramento da instância do Banco de Dados
     */
    public synchronized void close() {
        instance = null;
        super.close();
    }
}
