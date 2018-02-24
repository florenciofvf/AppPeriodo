package florencio.com.br.appperiodo.dominio;

import android.content.ContentValues;
import android.util.Log;

import florencio.com.br.appperiodo.util.Util;

public class Dia extends Entidade {
	private int sincronizado;
	private Integer numero;
	private int especial;
	private String nome;
	private String obs;
	private int valido;
	private long data;
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

    private String totalLeiFmt;
    private String totalFmt;
    private String credito;
    private String debito;
    private long totalLei;
    private long total;

    public Dia(Integer numero, Mes mes, String nome) {
        this.numero = numero;
        this.nome = nome;
        this.mes = mes;
    }

    public void copiar(Dia d) {
        _id = d._id;

        sincronizado = d.sincronizado;
        especial = d.especial;
        numero = d.numero;
        valido = d.valido;
        nome = d.nome;
        data = d.data;
        obs = d.obs;
        mes = d.mes;

        manhaIni = d.manhaIni;
        manhaFim = d.manhaFim;
        tardeIni = d.tardeIni;
        tardeFim = d.tardeFim;
        noiteIni = d.noiteIni;
        noiteFim = d.noiteFim;
    }

    public void importar(String[] strings) {
        try {
            for (int i = 2; i < strings.length; i++) {
                String s = strings[i];
                String[] strings1 = s.split("=");

                if (Util.M_I.equals(strings1[0])) {
                    manhaIni = Util.parseHora(strings1[1]);
                } else if (Util.M_F.equals(strings1[0])) {
                    manhaFim = Util.parseHora(strings1[1]);
                } else if (Util.T_I.equals(strings1[0])) {
                    tardeIni = Util.parseHora(strings1[1]);
                } else if (Util.T_F.equals(strings1[0])) {
                    tardeFim = Util.parseHora(strings1[1]);
                } else if (Util.N_I.equals(strings1[0])) {
                    noiteIni = Util.parseHora(strings1[1]);
                } else if (Util.N_F.equals(strings1[0])) {
                    noiteFim = Util.parseHora(strings1[1]);
                } else if (Util.OBS.equals(strings1[0])) {
                    obs = resto(i, strings);
                }
            }
        } catch (Exception e) {
            Log.i("ERRO>>>", e.getMessage());
        }
    }

    private String resto(int indice, String[] strings) {
        StringBuilder sb = new StringBuilder();

        for (int i = indice; i < strings.length; i++) {
            sb.append(strings[i]).append(" ");
        }

        return sb.toString().substring(Util.OBS.length() + 1);
    }

    public void limparHorarios() {
        manhaIni = 0;
        manhaFim = 0;
        tardeIni = 0;
        tardeFim = 0;
        noiteIni = 0;
        noiteFim = 0;
    }

    public void processar(long toleranciaSaida, long excessoExtra) {
        totalLei = 0;
        total = 0;

        manhaCalFmt = Util.ZERO_ZERO;
        tardeCalFmt = Util.ZERO_ZERO;
        noiteCalFmt = Util.ZERO_ZERO;
        credito = Util.ZERO_ZERO;
        debito = Util.ZERO_ZERO;

        manhaIniFmt = Util.formatarHora(manhaIni);
        manhaFimFmt = Util.formatarHora(manhaFim);
        if (checado(manhaIni, manhaFim)) {
            total += Util.diferenca(manhaIni, manhaFim);
            manhaCalFmt = Util.diferencaFmt(manhaIni, manhaFim);
        }

        tardeIniFmt = Util.formatarHora(tardeIni);
        tardeFimFmt = Util.formatarHora(tardeFim);
        if (checado(tardeIni, tardeFim)) {
            total += Util.diferenca(tardeIni, tardeFim);
            tardeCalFmt = Util.diferencaFmt(tardeIni, tardeFim);
        }

        noiteIniFmt = Util.formatarHora(noiteIni);
        noiteFimFmt = Util.formatarHora(noiteFim);
        if (checado(noiteIni, noiteFim)) {
            total += Util.diferenca(noiteIni, noiteFim);
            noiteCalFmt = Util.diferencaFmt(noiteIni, noiteFim);
        }

        totalFmt = Util.totalFmt(total);

        if(total < Util.OITO_HORAS - toleranciaSaida) {
            totalLei = total;
        } else if(total > Util.OITO_HORAS + excessoExtra) {
            totalLei = total;
        } else {
            totalLei = Util.OITO_HORAS;
        }

        totalLeiFmt = Util.totalFmt(totalLei);

        if (totalLei > Util.OITO_HORAS + excessoExtra && !ehNovo()) {
            credito = Util.diferencaFmt(Util.OITO_HORAS, totalLei);
        }

        if (totalLei < Util.OITO_HORAS - toleranciaSaida && !ehNovo()) {
            debito = Util.diferencaFmt(total, Util.OITO_HORAS);
        }

        atual = numero == Util.DIA_ATUAL
                && mes.getNumero() == Util.MES_ATUAL
                && mes.getAno().getNumero() == Util.ANO_ATUAL;
    }

    private boolean checado(long ini, long fim) {
        return ini != 0 && fim != 0;
    }

    public String getTotalFmt() {
        return totalFmt;
    }

    public String getTotalLeiFmt() {
        return totalLeiFmt;
    }

    public Integer getNumero() {
        return numero;
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

    public String getManhaFimFmt() {
        return manhaFimFmt;
    }

    public String getManhaCalFmt() {
        return manhaCalFmt;
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

    public String getTardeFimFmt() {
        return tardeFimFmt;
    }

    public String getTardeCalFmt() {
        return tardeCalFmt;
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

    public String getNoiteFimFmt() {
        return noiteFimFmt;
    }

    public String getNoiteCalFmt() {
        return noiteCalFmt;
    }

    public String getNome() {
        return nome;
    }

    public String getDebito() {
        return debito;
    }

    public String getCredito() {
        return credito;
    }

    public long getTotal() {
        return total;
    }

    public long getTotalLei() {
        return totalLei;
    }

    public boolean isValido() {
        return valido == 1;
    }

    public boolean isEspecial() {
        return especial == 1;
    }

    public void setValido(int valido) {
        this.valido = valido;
    }

    public void setEspecial(int especial) {
        this.especial = especial;
    }

    public boolean isAtual() {
        return atual;
    }

    public void setData(long data) {
        this.data = data;
    }

    public boolean isSincronizado() {
        return sincronizado == 1;
    }

    public void setSincronizado(int sincronizado) {
        this.sincronizado = sincronizado;
    }

    public ContentValues criarContentValues() {
        if (data == 0) {
            data = Util.criarData(this);
        }

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
        cv.put("especial", especial);
        cv.put("valido", valido);
        cv.put("sincronizado", sincronizado);
        cv.put("data", data);
        return cv;
    }

    public String gerarConteudoEmail() {
        if (!ehNovo()) {
            return Util.get00(getNumero().toString()) + " " + getNome() + " " +
                    auxHora(Util.M_I, getManhaIniFmt()) +
                    auxHora(Util.M_F, getManhaFimFmt()) +
                    auxHora(Util.T_I, getTardeIniFmt()) +
                    auxHora(Util.T_F, getTardeFimFmt()) +
                    auxHora(Util.N_I, getNoiteIniFmt()) +
                    auxHora(Util.N_F, getNoiteFimFmt()) +
                    auxDesc(Util.OBS, getObs()) + "\n";
        }

        return "";
    }

    public String gerarConteudoDados() {
        if (!ehNovo()) {
            return Util.get00(getNumero().toString()) + " " + getNome() + " " +
                    auxHora("", getManhaIniFmt()) +
                    auxHora("", getManhaFimFmt()) +
                    auxHora("", getTardeIniFmt()) +
                    auxHora("", getTardeFimFmt()) +
                    auxHora("", getNoiteIniFmt()) +
                    auxHora("", getNoiteFimFmt()) +
                    auxDesc("", getObs()) + "\n";
        }

        return "";
    }

    private String auxHora(String prefixo, String string) {
        if (string == null || Util.ZERO_ZERO.equals(string)) {
            return "";
        }

        return prefixo.length() > 0 ? prefixo + "=" + string + " " : string + " ";
    }

    private String auxDesc(String prefixo, String string) {
        if (string == null || string.length() == 0) {
            return "";
        }

        return prefixo.length() > 0 ? prefixo + "=" + string + " " : string + " ";
    }

    @Override
    public String toString() {
        return "MANHA_INI=" + manhaIni + ", MANHA_FIM=" + manhaFim +
                ", TARDE_INI=" + tardeIni + ", TARDE_FIM=" + tardeFim +
                ", NOITE_INI=" + noiteIni + ", NOITE_FIM=" + noiteFim;
    }
}