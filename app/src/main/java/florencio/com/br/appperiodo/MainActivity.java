package florencio.com.br.appperiodo;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import florencio.com.br.appperiodo.dominio.Ano;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.dominio.Mes;
import florencio.com.br.appperiodo.fragmentos.AnoFragment;
import florencio.com.br.appperiodo.fragmentos.DiaAtualFragment;
import florencio.com.br.appperiodo.fragmentos.DiaDialogFragment;
import florencio.com.br.appperiodo.fragmentos.DiaFragment;
import florencio.com.br.appperiodo.fragmentos.MesFragment;
import florencio.com.br.appperiodo.persistencia.Repositorio;
import florencio.com.br.appperiodo.util.Tolerancia;
import florencio.com.br.appperiodo.util.Util;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
		AnoFragment.AnoFragmentListener, MesFragment.MesFragmentListener,
		DiaFragment.DiaFragmentListener, DiaDialogFragment.DiaDialogListener,
		DiaAtualFragment.DiaAtualFragmentListener {

	private DrawerLayout drawerLayout;
	private Repositorio repositorio;
	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		Intent it = getIntent();

		if (it != null) {
			String vibrar = it.getStringExtra(Util.VIBRAR);

			if (!Util.isVazio(vibrar)) {
				Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

				if (vibrator != null && vibrator.hasVibrator()) {
					vibrator.vibrate(Util.getArrayLong(vibrar), -1);
				}
			}
		}

		Util.atualizarComprimentoHorario(this);
		Util.atualizarData();

		repositorio = new Repositorio(this);

		Tolerancia tolerancia = Util.getTolerancia(this);
		repositorio.sincronizarDia(Util.diaAtual, tolerancia.getMenosHorario(), tolerancia.getMaisHorario());

		drawerLayout = findViewById(R.id.drawerLayout);
		toolbar = findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);
		ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
		NavigationView navigationView = findViewById(R.id.navigationView);
		navigationView.setNavigationItemSelectedListener(this);
		drawerToggle.syncState();

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		DiaAtualFragment fragment = DiaAtualFragment.newInstance(Util.diaAtual);
		transaction.replace(R.id.container, fragment, "FRAGMENTO");
		transaction.addToBackStack(null);
		transaction.commit();
	}

	private void titulo(Ano ano, Mes mes) {
		if (ano != null) {
			toolbar.setTitle(ano.getNumero().toString());
		}

		if (mes != null) {
			toolbar.setTitle(mes.getNome() + "/" + mes.getAno().getNumero());
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.itemAno) {
			FragmentManager manager = getSupportFragmentManager();
			Fragment f = manager.findFragmentByTag("FRAGMENTO");
			FragmentTransaction transaction = manager.beginTransaction();

			AnoFragment fragment = AnoFragment.newInstance(Util.getAnoAtual());
			transaction.replace(R.id.container, fragment, "FRAGMENTO");

			if (f != null) {
				transaction.addToBackStack(null);
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			}
			transaction.commit();

		} else if (item.getItemId() == R.id.itemHoje) {
			FragmentManager manager = getSupportFragmentManager();
			Fragment f = manager.findFragmentByTag("FRAGMENTO");
			FragmentTransaction transaction = manager.beginTransaction();

			Tolerancia tolerancia = Util.getTolerancia(this);
			repositorio.sincronizarDia(Util.diaAtual, tolerancia.getMenosHorario(), tolerancia.getMaisHorario());

			DiaAtualFragment fragment = DiaAtualFragment.newInstance(Util.diaAtual);
			transaction.replace(R.id.container, fragment, "FRAGMENTO");

			if (f != null) {
				transaction.addToBackStack(null);
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			}
			transaction.commit();

		} else if (item.getItemId() == R.id.itemPref) {
			startActivity(new Intent(this, PreferenciaActivity.class));
		}

		drawerLayout.closeDrawers();

		return true;
	}

	@Override
	public void exibirMeses(Ano ano) {
		titulo(ano, null);

		FragmentManager manager = getSupportFragmentManager();
		Fragment f = manager.findFragmentByTag("FRAGMENTO");
		FragmentTransaction transaction = manager.beginTransaction();
		MesFragment fragment = MesFragment.newInstance(ano);
		transaction.replace(R.id.container, fragment, "FRAGMENTO");

		if (f != null) {
			transaction.addToBackStack(null);
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		}

		transaction.commit();
	}

	@Override
	public void exibirDias(Mes mes) {
		titulo(null, mes);

		FragmentManager manager = getSupportFragmentManager();
		Fragment f = manager.findFragmentByTag("FRAGMENTO");
		FragmentTransaction transaction = manager.beginTransaction();
		DiaFragment fragment = DiaFragment.newInstance(mes);
		transaction.replace(R.id.container, fragment, "FRAGMENTO");

		if (f != null) {
			transaction.addToBackStack(null);
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		}

		transaction.commit();
	}

	@Override
	public void clickDia(Dia dia) {
		DiaDialogFragment dialog = DiaDialogFragment.newInstance(dia);
		dialog.show(getSupportFragmentManager(), "DIA_DIALOG");
	}

	@Override
	public void salvarDia(Dia dia) {
		Tolerancia tolerancia = Util.getTolerancia(this);
		repositorio.salvarDia(dia, tolerancia.getMenosHorario(), tolerancia.getMaisHorario());

		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentByTag("FRAGMENTO");

		if (fragment instanceof DiaFragment) {
			((DiaFragment) fragment).atualizar();
		} else if (fragment instanceof DiaAtualFragment) {
			((DiaAtualFragment) fragment).atualizar(this);
		}

		Toast.makeText(this, R.string.label_registro_salvo, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		FragmentManager manager = getSupportFragmentManager();
		Fragment fragment = manager.findFragmentByTag("FRAGMENTO");

		if (fragment != null) {
			Object obj = fragment.getArguments().getSerializable(Util.PARAMETRO);
			if (obj instanceof Ano) {
				titulo((Ano) obj, null);
			} else if (obj instanceof Mes) {
				titulo(null, (Mes) obj);
			} else {
				toolbar.setTitle(R.string.app_name);
			}
		}
	}
}