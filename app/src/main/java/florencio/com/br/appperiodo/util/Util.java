package florencio.com.br.appperiodo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Ano;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.dominio.Mes;

public class Util {
	private static final String[] NOME_MESES = {"JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"};
	private static final DateFormat format_HH_mm = new SimpleDateFormat("HH:mm", Locale.getDefault());
	public static final String[] NOME_DIAS = {"DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SAB"};
	public static long OITO_HORAS = 1000L * 60L * 60L * 8L;
	public static final String PARAMETRO = "parametro";
	public static final String MANHA_INI = "MANHA_INI";
	public static final String MANHA_FIM = "MANHA_FIM";
	public static final String TARDE_INI = "TARDE_INI";
	public static final String TARDE_FIM = "TARDE_FIM";
	public static final String NOITE_INI = "NOITE_INI";
	public static final String NOITE_FIM = "NOITE_FIM";
	public static final String ZERO_ZERO = "00:00";
	public static final String VIBRAR = "VIBRAR";
	public static final String QUEBRA = "\n";
	public static final String M_I = "M_I";
	public static final String M_F = "M_F";
	public static final String T_I = "T_I";
	public static final String T_F = "T_F";
	public static final String N_I = "N_I";
	public static final String N_F = "N_F";
	public static final String OBS = "OBS";
	public static final String VAZIO = "";
	public static final int UM = 1;

	public static int ANO_ATUAL;
	public static int MES_ATUAL;
	public static int DIA_ATUAL;
	public static Dia diaAtual;

	public static Integer getAnoAtual() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public static void atualizarData() {
		Calendar c = Calendar.getInstance();

		ANO_ATUAL = c.get(Calendar.YEAR);
		MES_ATUAL = c.get(Calendar.MONTH) + 1;
		DIA_ATUAL = c.get(Calendar.DATE);

		int indice = c.get(Calendar.DAY_OF_WEEK) - 1;

		Ano ano = new Ano(ANO_ATUAL);
		Mes mes = new Mes(MES_ATUAL, NOME_MESES[MES_ATUAL - 1], ano, -1);
		diaAtual = new Dia(DIA_ATUAL, mes, NOME_DIAS[indice]);
	}

	public static long criarData(Dia dia) {
		Calendar c = Calendar.getInstance();

		c.set(Calendar.YEAR, dia.getMes().getAno().getNumero());
		c.set(Calendar.MONTH, dia.getMes().getNumero() - 1);
		c.set(Calendar.DATE, dia.getNumero());

		return c.getTimeInMillis();
	}

	public static Calendar criarCalendarZero() {
		Calendar c = Calendar.getInstance();

		c.set(Calendar.DAY_OF_MONTH, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.YEAR, 0);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.DATE, 0);

		return c;
	}

	public static String formatarHora(long milisegundos) {
		if (milisegundos == 0) {
			return ZERO_ZERO;
		}

		return format_HH_mm.format(new Date(milisegundos));
	}

	private static Integer getHora(long milisegundos) {
		if (milisegundos == 0) {
			return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		}

		Calendar c = Calendar.getInstance();
		c.setTime(new Date(milisegundos));

		return c.get(Calendar.HOUR_OF_DAY);
	}

	private static Integer getMinuto(long milisegundos) {
		if (milisegundos == 0) {
			return Calendar.getInstance().get(Calendar.MINUTE);
		}

		Calendar c = Calendar.getInstance();
		c.setTime(new Date(milisegundos));

		return c.get(Calendar.MINUTE);
	}

	public static long diferenca(long inicio, long termino) {
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

		long hor = getHoras(milisegundos);
		long min = getMinutos(milisegundos) % 60;

		return get00(hor) + ":" + get00(min);
	}

	private static String get00(long valor) {
		return valor < 10 ? "0" + valor : "" + valor;
	}

	private static String get00(Integer valor) {
		return valor < 10 ? "0" + valor.toString() : valor.toString();
	}

	public static String get00(String s) {
		return s.length() == 1 ? "0" + s : s;
	}

	private static long getMinutos(long milisegundos) {
		return milisegundos / 1000 / 60;
	}

	private static long getHoras(long milisegundos) {
		return milisegundos / 1000 / 60 / 60;
	}

	public static String criarTitulo(Dia dia) {
		return get00(dia.getNumero()) + "/" + get00(dia.getMes().getNumero()) + "/" + get00(dia.getMes().getAno().getNumero());
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

	public static void atualizarText(String campo, Dia dia, TextView button, Context context) {
		if (Util.MANHA_INI.equals(campo)) {
			button.setText(Util.formatarHora(dia.getManhaIni()));
			button.setBackground(getBackground(dia.getManhaIni(), context));

		} else if (Util.MANHA_FIM.equals(campo)) {
			button.setText(Util.formatarHora(dia.getManhaFim()));
			button.setBackground(getBackground(dia.getManhaFim(), context));

		} else if (Util.TARDE_INI.equals(campo)) {
			button.setText(Util.formatarHora(dia.getTardeIni()));
			button.setBackground(getBackground(dia.getTardeIni(), context));

		} else if (Util.TARDE_FIM.equals(campo)) {
			button.setText(Util.formatarHora(dia.getTardeFim()));
			button.setBackground(getBackground(dia.getTardeFim(), context));

		} else if (Util.NOITE_INI.equals(campo)) {
			button.setText(Util.formatarHora(dia.getNoiteIni()));
			button.setBackground(getBackground(dia.getNoiteIni(), context));

		} else if (Util.NOITE_FIM.equals(campo)) {
			button.setText(Util.formatarHora(dia.getNoiteFim()));
			button.setBackground(getBackground(dia.getNoiteFim(), context));
		}
	}

	private static Drawable getBackground(long l, Context context) {
		return l != 0 ? context.getDrawable(R.drawable.bg_360_atual) : context.getDrawable(R.drawable.bg_360_padrao);
	}

	public static boolean isVazio(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	public static Drawable getBackground(Dia dia, Context context) {
		if (dia != null) {
			if (dia.isEspecial()) {
				return context.getDrawable(R.drawable.bg_especial);
			}

			if (dia.isAtual()) {
				return context.getDrawable(R.drawable.bg_atual);
			}

			if ("SAB".equals(dia.getNome())) {
				return context.getDrawable(R.drawable.bg_sabado);
			}

			if ("DOM".equals(dia.getNome())) {
				return context.getDrawable(R.drawable.bg_domingo);
			}

			return context.getDrawable(R.drawable.bg_padrao);
		}

		return null;
	}

	public static long parseHora(String string) {
		String[] strings = string.split(":");

		int horas = Integer.parseInt(strings[0]);
		int minutos = Integer.parseInt(strings[1]);

		Calendar c = criarCalendarZero();
		c.set(Calendar.HOUR_OF_DAY, horas);
		c.set(Calendar.MINUTE, minutos);

		return c.getTimeInMillis();
	}

	public static long parseHora2(String string) {
		String[] strings = string.split(":");

		long horas = 1000L * 60L * 60L * Long.parseLong(strings[0]);
		long minutos = 1000L * 60L * Long.parseLong(strings[1]);

		return horas + minutos;
	}

	public static Tolerancia getTolerancia(Context context) {
		String stringTolerancia = getStringPref(context, R.string.tolerancia_saida, R.string.tolerancia_saida_default);
		String stringExcesso = getStringPref(context, R.string.excesso_hora_extra, R.string.excesso_hora_extra_default);
		return new Tolerancia(parseHora2(stringTolerancia), parseHora2(stringExcesso));
	}

	public static void atualizarComprimentoHorario(Context context) {
		String string = getStringPref(context, R.string.comprimento_horario, R.string.comprimento_horario_default);
		OITO_HORAS = parseHora2(string);
	}

	public static String getStringPref(Context context, String chave, String padrao) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(chave, padrao);
	}

	public static String getStringPref(Context context, int chave, int padrao) {
		return getStringPref(context, context.getString(chave), context.getString(padrao));
	}

	public static long[] getArrayLong(String vibrar) {
		String[] strings = vibrar.split(",");
		long[] array = new long[strings.length];

		for (int i = 0; i < strings.length; i++) {
			array[i] = Long.parseLong(strings[i]);
		}

		return array;
	}

	public static void requireNonNull(Object[] objetos) {
		if (objetos == null || objetos.length == 0) {
			throw new NullPointerException();
		}

		for (Object obj : objetos) {
			Objects.requireNonNull(obj);
		}
	}
}