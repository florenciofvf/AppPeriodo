package florencio.com.br.appperiodo.persistencia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import florencio.com.br.appperiodo.dominio.Ano;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.dominio.Mes;
import florencio.com.br.appperiodo.util.Util;

public class Repositorio {
	private final BancoHelper helper;

	public Repositorio(Context context) {
		helper = new BancoHelper(context);
	}

	public void salvarAno(Ano ano) {
		SQLiteDatabase db = helper.getReadableDatabase();

		int total;
		Cursor cursor = db.rawQuery("select count(*) from Ano where numero=" + ano.getNumero(), null);
		cursor.moveToNext();
		total = cursor.getInt(0);

		cursor.close();
		db.close();

		if (total == 0) {
			db = helper.getWritableDatabase();
			long _id = db.insert("Ano", null, ano.criarContentValues());
			ano.set_id(_id);

			db.insert("Mes", null, new Mes(1, "JANEIRO", ano, 31).criarContentValues());
			db.insert("Mes", null, new Mes(2, "FEVEREIRO", ano, 28).criarContentValues());
			db.insert("Mes", null, new Mes(3, "MARÇO", ano, 31).criarContentValues());
			db.insert("Mes", null, new Mes(4, "ABRIL", ano, 30).criarContentValues());
			db.insert("Mes", null, new Mes(5, "MAIO", ano, 31).criarContentValues());
			db.insert("Mes", null, new Mes(6, "JUNHO", ano, 30).criarContentValues());
			db.insert("Mes", null, new Mes(7, "JULHO", ano, 31).criarContentValues());
			db.insert("Mes", null, new Mes(8, "AGOSTO", ano, 31).criarContentValues());
			db.insert("Mes", null, new Mes(9, "SETEMBRO", ano, 30).criarContentValues());
			db.insert("Mes", null, new Mes(10, "OUTUBRO", ano, 31).criarContentValues());
			db.insert("Mes", null, new Mes(11, "NOVEMBRO", ano, 30).criarContentValues());
			db.insert("Mes", null, new Mes(12, "DEZEMBRO", ano, 31).criarContentValues());

			db.close();
		}
	}

	public List<Ano> listarAnos() {
		List<Ano> lista = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select _id, numero from Ano", null);

		while (cursor.moveToNext()) {
			Ano a = new Ano(cursor.getInt(1));
			a.set_id(cursor.getLong(0));
			a.processar();
			lista.add(a);
		}

		cursor.close();
		db.close();

		return lista;
	}

	public List<Mes> listarMeses(Ano ano) {
		List<Mes> lista = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select _id, numero, nome, maximo_dias from Mes where ano_id=" + ano.get_id(), null);

		while (cursor.moveToNext()) {
			Mes m = new Mes(cursor.getInt(1), cursor.getString(2), ano, cursor.getInt(3));
			m.set_id(cursor.getLong(0));
			m.processar();
			lista.add(m);
		}

		cursor.close();
		db.close();

		return lista;
	}

	private List<Dia> listarDias(Mes mes) {
		List<Dia> lista = new ArrayList<>();

		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select _id, numero, obs, manha_ini, manha_fim, tarde_ini, tarde_fim, noite_ini, noite_fim, nome, data, valido, especial, sincronizado from Dia where mes_id=" + mes.get_id(), null);

		int _id_idx = cursor.getColumnIndex("_id");
		int num_idx = cursor.getColumnIndex("numero");
		int obs_idx = cursor.getColumnIndex("obs");
		int nom_idx = cursor.getColumnIndex("nome");

		int dat_idx = cursor.getColumnIndex("data");
		int val_idx = cursor.getColumnIndex("valido");
		int esp_idx = cursor.getColumnIndex("especial");
		int sin_idx = cursor.getColumnIndex("sincronizado");

		int m_i_idx = cursor.getColumnIndex("manha_ini");
		int m_f_idx = cursor.getColumnIndex("manha_fim");

		int t_i_idx = cursor.getColumnIndex("tarde_ini");
		int t_f_idx = cursor.getColumnIndex("tarde_fim");

		int n_i_idx = cursor.getColumnIndex("noite_ini");
		int n_f_idx = cursor.getColumnIndex("noite_fim");

		while (cursor.moveToNext()) {
			Dia dia = new Dia(cursor.getInt(num_idx), mes, cursor.getString(nom_idx));

			dia.set_id(cursor.getLong(_id_idx));
			dia.setObs(cursor.getString(obs_idx));
			dia.setManhaIni(cursor.getLong(m_i_idx));
			dia.setManhaFim(cursor.getLong(m_f_idx));
			dia.setTardeIni(cursor.getLong(t_i_idx));
			dia.setTardeFim(cursor.getLong(t_f_idx));
			dia.setNoiteIni(cursor.getLong(n_i_idx));
			dia.setNoiteFim(cursor.getLong(n_f_idx));

			dia.setData(cursor.getLong(dat_idx));
			dia.setValido(cursor.getInt(val_idx));
			dia.setEspecial(cursor.getInt(esp_idx));
			dia.setSincronizado(cursor.getInt(sin_idx));

			lista.add(dia);
		}

		cursor.close();
		db.close();

		return lista;
	}

	public void sincronizarDia(Dia d, long menosHorario, long maisHorario) {
		SQLiteDatabase db = helper.getReadableDatabase();

		String sb = "";
		sb = sb.concat("select d._id, d.numero, d.obs, d.manha_ini, d.manha_fim, d.tarde_ini, d.tarde_fim, d.noite_ini, d.noite_fim, d.nome, d.mes_id, d.data, d.valido, d.especial, d.sincronizado from Dia d")
				.concat(" inner join Mes m on m._id = d.mes_id")
				.concat(" inner join Ano a on a._id = m.ano_id")
				.concat(" where d.numero=").concat(String.valueOf(d.getNumero()))
				.concat(" and m.numero=").concat(String.valueOf(d.getMes().getNumero()))
				.concat(" and a.numero=").concat(String.valueOf(d.getMes().getAno().getNumero()));

		Cursor cursor = db.rawQuery(sb, null);

		int _id_idx = cursor.getColumnIndex("_id");
		int num_idx = cursor.getColumnIndex("numero");
		int obs_idx = cursor.getColumnIndex("obs");
		int nom_idx = cursor.getColumnIndex("nome");
		int mes_idx = cursor.getColumnIndex("mes_id");

		int dat_idx = cursor.getColumnIndex("data");
		int val_idx = cursor.getColumnIndex("valido");
		int esp_idx = cursor.getColumnIndex("especial");
		int sin_idx = cursor.getColumnIndex("sincronizado");

		int m_i_idx = cursor.getColumnIndex("manha_ini");
		int m_f_idx = cursor.getColumnIndex("manha_fim");

		int t_i_idx = cursor.getColumnIndex("tarde_ini");
		int t_f_idx = cursor.getColumnIndex("tarde_fim");

		int n_i_idx = cursor.getColumnIndex("noite_ini");
		int n_f_idx = cursor.getColumnIndex("noite_fim");

		while (cursor.moveToNext()) {
			Dia dia = new Dia(cursor.getInt(num_idx), d.getMes(), cursor.getString(nom_idx));

			dia.set_id(cursor.getLong(_id_idx));
			dia.setObs(cursor.getString(obs_idx));

			dia.setManhaIni(cursor.getLong(m_i_idx));
			dia.setManhaFim(cursor.getLong(m_f_idx));
			dia.setTardeIni(cursor.getLong(t_i_idx));
			dia.setTardeFim(cursor.getLong(t_f_idx));
			dia.setNoiteIni(cursor.getLong(n_i_idx));
			dia.setNoiteFim(cursor.getLong(n_f_idx));

			d.getMes().set_id(cursor.getLong(mes_idx));

			dia.setData(cursor.getLong(dat_idx));
			dia.setValido(cursor.getInt(val_idx));
			dia.setEspecial(cursor.getInt(esp_idx));
			dia.setSincronizado(cursor.getInt(sin_idx));

			d.copiar(dia);
		}

		d.processar(menosHorario, maisHorario);
		cursor.close();
		db.close();
	}

	public void salvarDia(Dia dia, long menosHorario, long maisHorario) {
		if (dia.getMes().ehNovo()) {
			setIdMes(dia.getMes());
		}

		SQLiteDatabase db = helper.getReadableDatabase();

		String sb = "";
		sb = sb.concat("select count(d._id) from Dia d")
				.concat(" inner join Mes m on m._id = d.mes_id")
				.concat(" inner join Ano a on a._id = m.ano_id")
				.concat(" where d.numero=").concat(String.valueOf(dia.getNumero()))
				.concat(" and m.numero=").concat(String.valueOf(dia.getMes().getNumero()))
				.concat(" and a.numero=").concat(String.valueOf(dia.getMes().getAno().getNumero()));

		Cursor cursor = db.rawQuery(sb, null);

		int total = 0;

		while (cursor.moveToNext()) {
			total = cursor.getInt(0);
		}

		cursor.close();
		db.close();

		db = helper.getWritableDatabase();

		if (total == 0) {
			long _id = db.insert("Dia", null, dia.criarContentValues());
			dia.set_id(_id);
		} else {
			db.update("Dia", dia.criarContentValues(), "_id=" + dia.get_id(), null);
		}

		dia.processar(menosHorario, maisHorario);
		db.close();
	}

	public void excluirDia(Dia d) {
		if(!d.ehNovo()) {
			SQLiteDatabase db = helper.getWritableDatabase();
			db.delete("Dia", "_id=" + d.get_id(), null);
			db.close();
		}
	}

	private void setIdMes(Mes mes) {
		SQLiteDatabase db = helper.getReadableDatabase();

		String sb = "";
		sb = sb.concat("select m._id from Mes m")
				.concat(" inner join Ano a on a._id = m.ano_id")
				.concat(" and m.numero=").concat(String.valueOf(mes.getNumero()))
				.concat(" and a.numero=").concat(String.valueOf(mes.getAno().getNumero()));

		Cursor cursor = db.rawQuery(sb, null);

		while (cursor.moveToNext()) {
			mes.set_id(cursor.getLong(0));
		}

		cursor.close();
		db.close();
	}

	public List<Dia> montarDiasDoMes(Mes mes, long menosHorario, long maisHorario) {
		Map<Integer, Dia> mapa = new LinkedHashMap<>();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, mes.getAno().getNumero());
		c.set(Calendar.MONTH, mes.getNumero() - 1);
		c.set(Calendar.DATE, 1);

		int indice = c.get(Calendar.DAY_OF_WEEK) - 1;

		for (int i = 1; i <= mes.getMaximoDias(); i++) {
			String nome = Util.NOME_DIAS[indice % 7];
			Dia dia = new Dia(i, mes, nome);
			mapa.put(i, dia);
			indice++;
		}

		List<Dia> lista = listarDias(mes);

		for (Dia dia : lista) {
			mapa.put(dia.getNumero(), dia);
		}

		lista = new ArrayList<>(mapa.values());

		for (Dia d : lista) {
			d.processar(menosHorario, maisHorario);
		}

		return lista;
	}
}