package florencio.com.br.appperiodo.dominio;

import android.content.ContentValues;

public class Ano extends Entidade {
    private Integer numero;

    public Ano(Integer numero) {
        this.numero = numero;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    @Override
    public ContentValues criarContentValues() {
        ContentValues cv = new ContentValues();

        cv.put("numero", numero);

        return cv;
    }
}