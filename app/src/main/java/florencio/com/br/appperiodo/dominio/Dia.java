package florencio.com.br.appperiodo.dominio;

import android.content.ContentValues;

public class Dia extends Entidade {
    private Integer numero;
    private String nome;
    private String obs;
    private Mes mes;

    private Long manhaIni;
    private Long manhaFim;
    private String manhaIniFmt;
    private String manhaFimFmt;
    private String manhaCalFmt;

    private Long tardeIni;
    private Long tardeFim;
    private String tardeIniFmt;
    private String tardeFimFmt;
    private String tardeCalFmt;

    private Long noiteIni;
    private Long noiteFim;
    private String noiteIniFmt;
    private String noiteFimFmt;
    private String noiteCalFmt;

    private boolean calculado;

    public Dia(Integer numero, Mes mes) {
        this.numero = numero;
        this.mes = mes;
    }

    public void calcular() {

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

    public Long getManhaIni() {
        return manhaIni;
    }

    public void setManhaIni(Long manhaIni) {
        this.manhaIni = manhaIni;
    }

    public Long getManhaFim() {
        return manhaFim;
    }

    public void setManhaFim(Long manhaFim) {
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

    public Long getTardeIni() {
        return tardeIni;
    }

    public void setTardeIni(Long tardeIni) {
        this.tardeIni = tardeIni;
    }

    public Long getTardeFim() {
        return tardeFim;
    }

    public void setTardeFim(Long tardeFim) {
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

    public Long getNoiteIni() {
        return noiteIni;
    }

    public void setNoiteIni(Long noiteIni) {
        this.noiteIni = noiteIni;
    }

    public Long getNoiteFim() {
        return noiteFim;
    }

    public void setNoiteFim(Long noiteFim) {
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

    public boolean isCalculado() {
        return calculado;
    }

    public void setCalculado(boolean calculado) {
        this.calculado = calculado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public ContentValues criarContentValues() {
        ContentValues cv = new ContentValues();

        cv.put("mes_id", mes.get_id());
        cv.put("numero", numero);
        cv.put("obs", obs);
        cv.put("manha_ini", manhaIni);
        cv.put("manha_fim", manhaFim);
        cv.put("tarde_ini", tardeIni);
        cv.put("tarde_fim", tardeFim);
        cv.put("noite_ini", noiteIni);
        cv.put("noite_fim", noiteFim);

        return cv;
    }
}