package florencio.com.br.appperiodo.dominio;

import android.content.ContentValues;

import java.io.Serializable;

public abstract class Entidade implements Serializable {
    boolean atual;
    Long _id;

    public final Long get_id() {
        return _id;
    }

    public final void set_id(Long _id) {
        this._id = _id;
    }

    public boolean isAtual() {
        return atual;
    }

    public boolean ehNovo() {
        return _id == null || _id.intValue() == 0;
    }

    public abstract void processar(long toleranciaSaida, long excessoExtra);

    public abstract ContentValues criarContentValues();
}