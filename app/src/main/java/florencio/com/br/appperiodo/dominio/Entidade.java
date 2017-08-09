package florencio.com.br.appperiodo.dominio;

import android.content.ContentValues;

import java.io.Serializable;

public abstract class Entidade implements Serializable {
    protected boolean atual;
    protected Long _id;

    public final Long get_id() {
        return _id;
    }

    public final void set_id(Long _id) {
        this._id = _id;
    }

    public boolean isAtual() {
        return atual;
    }

    public void setAtual(boolean atual) {
        this.atual = atual;
    }

    public boolean ehNovo() {
        return _id == null || _id.intValue() == 0;
    }

    public abstract void processar();

    public abstract ContentValues criarContentValues();
}