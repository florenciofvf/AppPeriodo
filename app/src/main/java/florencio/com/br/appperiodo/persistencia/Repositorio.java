package florencio.com.br.appperiodo.persistencia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.MediaController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import florencio.com.br.appperiodo.dominio.Ano;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.dominio.Mes;

public class Repositorio {
    private BancoHelper helper;

    public Repositorio(Context context) {
        helper = new BancoHelper(context);
    }

    public void salvarAno(Ano ano) {
        SQLiteDatabase db = helper.getReadableDatabase();

        int total;
        Cursor cursor = db.rawQuery("select count(*) from Ano where numero=" + ano.getNumero(), null);
        cursor.moveToNext();
        total = cursor.getInt(0);

        db.close();

        if(total == 0) {
            db = helper.getWritableDatabase();
            long _id = db.insert("Ano", null, ano.criarContentValues());
            ano.set_id(_id);

            db.insert("Mes", null, new Mes( 1,   "JANEIRO", ano, 31).criarContentValues());
            db.insert("Mes", null, new Mes( 2, "FEVEREIRO", ano, 28).criarContentValues());
            db.insert("Mes", null, new Mes( 3,     "MARÇO", ano, 31).criarContentValues());
            db.insert("Mes", null, new Mes( 4,     "ABRIL", ano, 30).criarContentValues());
            db.insert("Mes", null, new Mes( 5,      "MAIO", ano, 31).criarContentValues());
            db.insert("Mes", null, new Mes( 6,     "JUNHO", ano, 30).criarContentValues());
            db.insert("Mes", null, new Mes( 7,     "JULHO", ano, 30).criarContentValues());
            db.insert("Mes", null, new Mes( 8,    "AGOSTO", ano, 31).criarContentValues());
            db.insert("Mes", null, new Mes( 9,  "SETEMBRO", ano, 30).criarContentValues());
            db.insert("Mes", null, new Mes(10,   "OUTUBRO", ano, 31).criarContentValues());
            db.insert("Mes", null, new Mes(11,  "NOVEMBRO", ano, 30).criarContentValues());
            db.insert("Mes", null, new Mes(12,  "DEZEMBRO", ano, 31).criarContentValues());

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

            lista.add(a);
        }

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

            lista.add(m);
        }

        db.close();

        return lista;
    }

    public List<Dia> listarDias(Mes mes) {
        List<Dia> lista = new ArrayList<>();

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select _id, numero, obs, manha_ini, manha_fim, tarde_ini, tarde_fim, noite_ini, noite_fim from Dia where mes_id=" + mes.get_id(), null);

        while (cursor.moveToNext()) {
            Dia d = criarDia(cursor, mes);

            lista.add(d);
        }

        db.close();

        return lista;
    }

    private Dia criarDia(Cursor cursor, Mes mes) {
        Dia dia = new Dia(cursor.getInt(1), mes);
        dia.set_id(cursor.getLong(0));

        dia.setObs(cursor.getString(2));

        dia.setManhaIni(cursor.getLong(3));
        dia.setManhaFim(cursor.getLong(4));

        dia.setTardeIni(cursor.getLong(5));
        dia.setTardeFim(cursor.getLong(6));

        dia.setNoiteIni(cursor.getLong(7));
        dia.setNoiteFim(cursor.getLong(8));

        return dia;
    }

    public void salvarDia(Dia dia) {
        SQLiteDatabase db = helper.getReadableDatabase();

        int total = 0;

        StringBuilder sb = new StringBuilder();
        sb.append("select count(d._id) from Dia d");
        sb.append(" inner join Mes m on m._id = d.mes_id");
        sb.append(" inner join Ano a on a._id = m.ano_id");
        sb.append(" where d.numero=" + dia.getNumero());
        sb.append(" and m.numero=" + dia.getMes().getNumero());
        sb.append(" and a.numero=" + dia.getMes().getAno().getNumero());

        Cursor cursor = db.rawQuery(sb.toString(), null);
        while (cursor.moveToNext()) {
            total = cursor.getInt(0);
        }

        db.close();

        db = helper.getWritableDatabase();

        if(total == 0) {
            long _id = db.insert("Dia", null, dia.criarContentValues());
            dia.set_id(_id);
        } else {
            db.update("Dia", dia.criarContentValues(), "numero" + dia.get_id(), null);
        }

        db.close();
    }

    public List<Dia> montarDiasDoMes(Mes mes) {
        Map<Integer, Dia> mapa = new LinkedHashMap<>();

        for(int i=1; i<=mes.getMaximoDias(); i++) {
            Dia dia = new Dia(i, mes);
            dia.set_id(new Long(i));
            mapa.put(i, dia);
        }

        List<Dia> lista = listarDias(mes);

        for (Dia dia : lista) {
            mapa.put(dia.getNumero(), dia);
            dia.calcular();
        }

        return new ArrayList<>(mapa.values());
    }

}