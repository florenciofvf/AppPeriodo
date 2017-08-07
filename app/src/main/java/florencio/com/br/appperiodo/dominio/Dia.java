package florencio.com.br.appperiodo.dominio;

import android.content.ContentValues;

import florencio.com.br.appperiodo.util.Util;

public class Dia extends Entidade {
    private Integer numero;
    private String nome;
    private String obs;
    private Mes mes;

    private long manhaIni;
    private long manhaFim;
    private String manhaIniFmt;
    private String manhaFimFmt;
    private String manhaCalFmt;

    private long tardeIni;
    private long tardeFim;
    private String tardeIniFmt;
    private String tardeFimFmt;
    private String tardeCalFmt;

    private long noiteIni;
    private long noiteFim;
    private String noiteIniFmt;
    private String noiteFimFmt;
    private String noiteCalFmt;

    private String totalFmt;
    private String credito;
    private boolean valido;
    private String debito;
    private boolean atual;
    private long total;

    public void copiar(Dia d) {
        set_id(d.get_id());

        numero = d.numero;
        nome = d.nome;
        obs = d.obs;
        mes = d.mes;

        manhaIni = d.manhaIni;
        manhaFim = d.manhaFim;
        tardeIni = d.tardeIni;
        tardeFim = d.tardeFim;
        noiteIni = d.noiteIni;
        noiteFim = d.noiteFim;
    }

    public Dia(Integer numero, Mes mes, String nome) {
        this.numero = numero;
        this.nome = nome;
        this.mes = mes;
    }

    public void calcular() {
        total = 0;

        manhaIniFmt = Util.formatarHora(manhaIni);
        manhaFimFmt = Util.formatarHora(manhaFim);
        manhaCalFmt = Util.diferencaFmt(manhaIni, manhaFim);
        total += Util.diferenca(manhaIni, manhaFim);

        tardeIniFmt = Util.formatarHora(tardeIni);
        tardeFimFmt = Util.formatarHora(tardeFim);
        tardeCalFmt = Util.diferencaFmt(tardeIni, tardeFim);
        total += Util.diferenca(tardeIni, tardeFim);

        noiteIniFmt = Util.formatarHora(noiteIni);
        noiteFimFmt = Util.formatarHora(noiteFim);
        noiteCalFmt = Util.diferencaFmt(noiteIni, noiteFim);
        total += Util.diferenca(noiteIni, noiteFim);

        totalFmt = Util.totalFmt(total);

        valido = total > 0;
        debito = Util.ZERO_ZERO;
        credito = Util.ZERO_ZERO;

        if (total > Util.OITO_HORAS && valido) {
            credito = Util.diferencaFmt(Util.OITO_HORAS, total);
        }

        if (total < Util.OITO_HORAS && valido) {
            debito = Util.diferencaFmt(total, Util.OITO_HORAS);
        }

        atual = numero == Util.DIA_ATUAL
                && mes.getNumero() == Util.MES_ATUAL
                && mes.getAno().getNumero() == Util.ANO_ATUAL;
    }

    public String getTotalFmt() {
        return totalFmt;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public long getManhaIni() {
        return manhaIni;
    }

    public void setManhaIni(long manhaIni) {
        this.manhaIni = manhaIni;
    }

    public long getManhaFim() {
        return manhaFim;
    }

    public void setManhaFim(long manhaFim) {
        this.manhaFim = manhaFim;
    }

    public String getManhaIniFmt() {
        return manhaIniFmt;
    }

    public void setManhaIniFmt(String manhaIniFmt) {
        this.manhaIniFmt = manhaIniFmt;
    }

    public String getManhaFimFmt() {
        return manhaFimFmt;
    }

    public void setManhaFimFmt(String manhaFimFmt) {
        this.manhaFimFmt = manhaFimFmt;
    }

    public String getManhaCalFmt() {
        return manhaCalFmt;
    }

    public void setManhaCalFmt(String manhaCalFmt) {
        this.manhaCalFmt = manhaCalFmt;
    }

    public long getTardeIni() {
        return tardeIni;
    }

    public void setTardeIni(long tardeIni) {
        this.tardeIni = tardeIni;
    }

    public long getTardeFim() {
        return tardeFim;
    }

    public void setTardeFim(long tardeFim) {
        this.tardeFim = tardeFim;
    }

    public String getTardeIniFmt() {
        return tardeIniFmt;
    }

    public void setTardeIniFmt(String tardeIniFmt) {
        this.tardeIniFmt = tardeIniFmt;
    }

    public String getTardeFimFmt() {
        return tardeFimFmt;
    }

    public void setTardeFimFmt(String tardeFimFmt) {
        this.tardeFimFmt = tardeFimFmt;
    }

    public String getTardeCalFmt() {
        return tardeCalFmt;
    }

    public void setTardeCalFmt(String tardeCalFmt) {
        this.tardeCalFmt = tardeCalFmt;
    }

    public long getNoiteIni() {
        return noiteIni;
    }

    public void setNoiteIni(long noiteIni) {
        this.noiteIni = noiteIni;
    }

    public long getNoiteFim() {
        return noiteFim;
    }

    public void setNoiteFim(long noiteFim) {
        this.noiteFim = noiteFim;
    }

    public String getNoiteIniFmt() {
        return noiteIniFmt;
    }

    public void setNoiteIniFmt(String noiteIniFmt) {
        this.noiteIniFmt = noiteIniFmt;
    }

    public String getNoiteFimFmt() {
        return noiteFimFmt;
    }

    public void setNoiteFimFmt(String noiteFimFmt) {
        this.noiteFimFmt = noiteFimFmt;
    }

    public String getNoiteCalFmt() {
        return noiteCalFmt;
    }

    public void setNoiteCalFmt(String noiteCalFmt) {
        this.noiteCalFmt = noiteCalFmt;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDebito() {
        return debito;
    }

    public void setDebito(String debito) {
        this.debito = debito;
    }

    public String getCredito() {
        return credito;
    }

    public void setCredito(String credito) {
        this.credito = credito;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public void setTotalFmt(String totalFmt) {
        this.totalFmt = totalFmt;
    }

    public boolean isAtual() {
        return atual;
    }

    public void setAtual(boolean atual) {
        this.atual = atual;
    }

    @Override
    public ContentValues criarContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("mes_id", mes.get_id());
        cv.put("numero", numero);
        cv.put("nome", nome);
        cv.put("obs", obs);
        cv.put("manha_ini", manhaIni);
        cv.put("manha_fim", manhaFim);
        cv.put("tarde_ini", tardeIni);
        cv.put("tarde_fim", tardeFim);
        cv.put("noite_ini", noiteIni);
        cv.put("noite_fim", noiteFim);
        return cv;
    }

    @Override
    public String toString() {
        return "MANHA_INI=" + manhaIni + ", MANHA_FIM=" + manhaFim +
                ", TARDE_INI=" + tardeIni + ", TARDE_FIM=" + tardeFim +
                ", NOITE_INI=" + noiteIni + ", NOITE_FIM=" + noiteFim;
    }
}