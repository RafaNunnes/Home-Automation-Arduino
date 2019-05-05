package tcc.controller_bt.model;

import android.app.Application;
import android.content.Context;

/**
 * Classe que encapsula o Contexto da aplicação
 */
public class MyApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        super.onCreate();
    }

    /**
     * Implementação do método que retorna o contexto da aplicação.
     *
     * @return Contexto da Aplicação
     */
    public static Context getContext() {
        return mContext;
    }
}
