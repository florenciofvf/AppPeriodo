package florencio.com.br.appperiodo.dominio;

import android.content.ContentValues;

import florencio.com.br.appperiodo.util.Util;

public class Ano extends Entidade {
	final private Integer numero;

	public Ano(Integer numero) {
		this.numero = numero;
	}

	public Integer getNumero() {
		return numero;
	}

	public void processar() {
		atual = numero == Util.ANO_ATUAL;
	}

	public ContentValues criarContentValues() {
		ContentValues cv = new ContentValues();
		cv.put("numero", numero);
		return cv;
	}
}