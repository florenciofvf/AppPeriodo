package florencio.com.br.appperiodo.util;

import android.view.View;
import android.widget.Button;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import florencio.com.br.appperiodo.dominio.Ano;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.dominio.Mes;

public class Util {
    public static final String[] NOME_DIAS = {"DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SAB"};
    public static DateFormat format_HH_mm = new SimpleDateFormat("HH:mm");
    public static final long OITO_HORAS = 1000L * 60L * 60L * 8L;
    public static final String PARAMETRO = "parametro";
    public static final String MANHA_INI = "MANHA_INI";
    public static final String MANHA_FIM = "MANHA_FIM";
    public static final String TARDE_INI = "TARDE_INI";
    public static final String TARDE_FIM = "TARDE_FIM";
    public static final String NOITE_INI = "NOITE_INI";
    public static final String NOITE_FIM = "NOITE_FIM";
    public static final String ZERO_ZERO = "00:00";
    public static final int COR_ATUAL = 0xFF88EE88;

    public static int ANO_ATUAL;
    public static int MES_ATUAL;
    public static int DIA_ATUAL;
    public static Dia diaAtual;

    public static void atualizarData() {
        Calendar c = Calendar.getInstance();
        ANO_ATUAL = c.get(Calendar.YEAR);
        MES_ATUAL = c.get(Calendar.MONTH) + 1;
        DIA_ATUAL = c.get(Calendar.DATE);

        int indice = c.get(Calendar.DAY_OF_WEEK) - 1;
        String nome = NOME_DIAS[indice];

        Ano a = new Ano(ANO_ATUAL);
        Mes m = new Mes(MES_ATUAL, null, a, null);
        Dia d = new Dia(DIA_ATUAL, m, nome);

        diaAtual = d;
    }

    public static long criarData(Dia dia) {
        Calendar c = Calendar.getInstance();

        //c.set(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.YEAR, dia.getMes().getAno().getNumero());
        c.set(Calendar.MONTH, dia.getMes().getNumero() - 1);
        c.set(Calendar.DATE, dia.getNumero());

        return c.getTimeInMillis();
    }

    public static String formatarHora(long milisegundos) {
        if (milisegundos == 0) {
            return ZERO_ZERO;
        }

        return format_HH_mm.format(new Date(milisegundos));
    }

    public static Integer getHora(long milisegundos) {
        if (milisegundos == 0) {
            return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(milisegundos));

        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static Integer getMinuto(long milisegundos) {
        if (milisegundos == 0) {
            return Calendar.getInstance().get(Calendar.MINUTE);
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(milisegundos));

        return c.get(Calendar.MINUTE);
    }

    public static long diferenca(long inicio, long termino) {
        //return inicio == 0 || termino == 0 ? 0 : termino - inicio;
        return termino - inicio;
    }

    public static String diferencaFmt(long inicio, long termino) {
        long dif = diferenca(inicio, termino);
        return totalFmt(dif);
    }

    public static String totalFmt(long milisegundos) {
        if (milisegundos == 0) {
            return ZERO_ZERO;
        }

        long min = getMinutos(milisegundos);
        min = min % 60;

        long hor = getHoras(milisegundos);

        return get(hor) + ":" + get(min);
    }

    private static String get(long valor) {
        return valor < 10 ? "0" + valor : "" + valor;
    }

    public static long getMinutos(long milisegundos) {
        return milisegundos / 1000 / 60;
    }

    public static long getHoras(long milisegundos) {
        return milisegundos / 1000 / 60 / 60;
    }

    public static Integer getHora(String campo, Dia dia) {
        if (MANHA_INI.equals(campo)) {
            return getHora(dia.getManhaIni());
        } else if (MANHA_FIM.equals(campo)) {
            return getHora(dia.getManhaFim());
        } else if (TARDE_INI.equals(campo)) {
            return getHora(dia.getTardeIni());
        } else if (TARDE_FIM.equals(campo)) {
            return getHora(dia.getTardeFim());
        } else if (NOITE_INI.equals(campo)) {
            return getHora(dia.getNoiteIni());
        } else if (NOITE_FIM.equals(campo)) {
            return getHora(dia.getNoiteFim());
        }

        return 0;
    }

    public static Integer getMinuto(String campo, Dia dia) {
        if (MANHA_INI.equals(campo)) {
            return getMinuto(dia.getManhaIni());
        } else if (MANHA_FIM.equals(campo)) {
            return getMinuto(dia.getManhaFim());
        } else if (TARDE_INI.equals(campo)) {
            return getMinuto(dia.getTardeIni());
        } else if (TARDE_FIM.equals(campo)) {
            return getMinuto(dia.getTardeFim());
        } else if (NOITE_INI.equals(campo)) {
            return getMinuto(dia.getNoiteIni());
        } else if (NOITE_FIM.equals(campo)) {
            return getMinuto(dia.getNoiteFim());
        }

        return 0;
    }

    public static void atualizarText(String campo, Dia dia, Button button) {
        if (Util.MANHA_INI.equals(campo)) {
            button.setText(Util.formatarHora(dia.getManhaIni()));
        } else if (Util.MANHA_FIM.equals(campo)) {
            button.setText(Util.formatarHora(dia.getManhaFim()));
        } else if (Util.TARDE_INI.equals(campo)) {
            button.setText(Util.formatarHora(dia.getTardeIni()));
        } else if (Util.TARDE_FIM.equals(campo)) {
            button.setText(Util.formatarHora(dia.getTardeFim()));
        } else if (Util.NOITE_INI.equals(campo)) {
            button.setText(Util.formatarHora(dia.getNoiteIni()));
        } else if (Util.NOITE_FIM.equals(campo)) {
            button.setText(Util.formatarHora(dia.getNoiteFim()));
        }
    }
}