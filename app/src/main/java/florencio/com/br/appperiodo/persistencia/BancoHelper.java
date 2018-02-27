package florencio.com.br.appperiodo.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class BancoHelper extends SQLiteOpenHelper {
	private static final String NOME = "PERIODOS";
	private static final int VERSAO = 3;

	BancoHelper(Context context) {
		super(context, NOME, null, VERSAO);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("create table Ano(");
		sb.append("      _id integer not null primary key autoincrement,");
		sb.append("   numero integer not null");
		sb.append(")");
		db.execSQL(sb.toString());

		sb = new StringBuilder();
		sb.append("create table Mes(");
		sb.append("           _id integer not null primary key autoincrement,");
		sb.append("   maximo_dias integer not null,");
		sb.append("        numero integer not null,");
		sb.append("        ano_id integer not null,");
		sb.append("          nome text not null,");
		sb.append("   foreign key(ano_id) references Ano(_id)");
		sb.append(")");
		db.execSQL(sb.toString());

		sb = new StringBuilder();
		sb.append("create table Dia(");
		sb.append("           _id integer not null primary key autoincrement,");
		sb.append("        mes_id integer not null,");
		sb.append("        numero integer not null,");
		sb.append("          nome text not null,");
		sb.append("           obs text,");
		sb.append("     manha_ini integer not null,");
		sb.append("     manha_fim integer not null,");
		sb.append("     tarde_ini integer not null,");
		sb.append("     tarde_fim integer not null,");
		sb.append("     noite_ini integer not null,");
		sb.append("     noite_fim integer not null,");
		sb.append("          data integer not null,");
		sb.append("        valido integer not null,");
		sb.append("      especial integer not null,");
		sb.append("  sincronizado integer,");
		sb.append("  agendamento integer,");
		sb.append("   foreign key(mes_id) references Mes(_id)");
		sb.append(")");
		db.execSQL(sb.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("alter table Dia add column agendamento integer");
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			db.setForeignKeyConstraintsEnabled(true);
		}
	}
}