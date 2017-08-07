package florencio.com.br.appperiodo.dominio;

import android.content.ContentValues;

public class Ano extends Entidade {
    final private Integer numero;

    public Ano(Integer numero) {
        this.numero = numero;
    }

    public Integer getNumero() {
        return numero;
    }

    @Override
    public ContentValues criarContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("numero", numero);
        return cv;
    }
}