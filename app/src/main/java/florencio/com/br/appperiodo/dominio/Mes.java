package florencio.com.br.appperiodo.dominio;

import android.content.ContentValues;

import florencio.com.br.appperiodo.util.Util;

public class Mes extends Entidade {
    private final Integer maximoDias;
    private final Integer numero;
    private final String nome;
    private final Ano ano;

    public Mes(Integer numero, String nome, Ano ano, Integer maximoDias) {
        this.maximoDias = maximoDias;
        this.numero = numero;
        this.nome = nome;
        this.ano = ano;
    }

    public String getNome() {
        return nome;
    }

    public Ano getAno() {
        return ano;
    }

    public Integer getNumero() {
        return numero;
    }

    public Integer getMaximoDias() {
        return maximoDias;
    }

    public void processar(long toleranciaSaida, long excessoExtra) {
        atual = numero == Util.MES_ATUAL
                && getAno().getNumero() == Util.ANO_ATUAL;
    }

    @Override
    public ContentValues criarContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("maximo_dias", maximoDias);
        cv.put("ano_id", ano.get_id());
        cv.put("numero", numero);
        cv.put("nome", nome);
        return cv;
    }
}