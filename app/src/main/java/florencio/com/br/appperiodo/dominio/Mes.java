package florencio.com.br.appperiodo.dominio;

import android.content.ContentValues;

public class Mes extends Entidade {
    private Integer maximoDias;
    private Integer numero;
    private String nome;
    private Ano ano;

    public Mes(Integer numero, String nome, Ano ano, Integer maximoDias) {
        this.maximoDias = maximoDias;
        this.numero = numero;
        this.nome = nome;
        this.ano = ano;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Ano getAno() {
        return ano;
    }

    public void setAno(Ano ano) {
        this.ano = ano;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getMaximoDias() {
        return maximoDias;
    }

    public void setMaximoDias(Integer maximoDias) {
        this.maximoDias = maximoDias;
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