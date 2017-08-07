package florencio.com.br.appperiodo;

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

import java.util.Calendar;

import florencio.com.br.appperiodo.dominio.Ano;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.dominio.Mes;
import florencio.com.br.appperiodo.fragmentos.AnoFragment;
import florencio.com.br.appperiodo.fragmentos.DiaAtualFragment;
import florencio.com.br.appperiodo.fragmentos.DiaDialogFragment;
import florencio.com.br.appperiodo.fragmentos.DiaFragment;
import florencio.com.br.appperiodo.fragmentos.MesFragment;
import florencio.com.br.appperiodo.persistencia.Repositorio;
import florencio.com.br.appperiodo.util.Util;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AnoFragment.AnoFragmentListener, MesFragment.MesFragmentListener,
        DiaFragment.DiaFragmentListener, DiaDialogFragment.DiaDialogListener,
        DiaAtualFragment.DiaAtualFragmentListener {

    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Repositorio repositorio;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Util.atualizarData();
        repositorio = new Repositorio(this);
        repositorio.sincronizarDia(Util.diaAtual);
        Util.diaAtual.calcular();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle.syncState();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        DiaAtualFragment fragment = DiaAtualFragment.newInstance(Util.diaAtual);
        transaction.replace(R.id.container, fragment, "FRAGMENTO");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void titulo(Ano ano, Mes mes, Dia dia) {
        if(ano != null) {
            toolbar.setTitle(ano.getNumero().toString());
        }

        if(mes != null) {
            toolbar.setTitle(mes.getNome() + "/" + mes.getAno().getNumero());
        }

        if(dia != null) {
            toolbar.setTitle(DiaDialogFragment.criarTitulo(dia));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (item.getItemId() == R.id.itemAno) {
            AnoFragment fragment = AnoFragment.newInstance(getAnoAtual());
            transaction.replace(R.id.container, fragment, "FRAGMENTO");
        } else if (item.getItemId() == R.id.itemHoje) {
            repositorio.sincronizarDia(Util.diaAtual);
            Util.diaAtual.calcular();
            DiaAtualFragment fragment = DiaAtualFragment.newInstance(Util.diaAtual);
            transaction.replace(R.id.container, fragment, "FRAGMENTO");
        }

        drawerLayout.closeDrawers();
        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }

    private Integer getAnoAtual() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    @Override
    public void exibirMeses(Ano ano) {
        titulo(ano, null, null);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        MesFragment fragment = MesFragment.newInstance(ano);
        transaction.replace(R.id.container, fragment, "FRAGMENTO");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void exibirDias(Mes mes) {
        titulo(null, mes, null);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        DiaFragment fragment = DiaFragment.newInstance(mes);
        transaction.replace(R.id.container, fragment, "FRAGMENTO");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void clickDia(Dia dia) {
        DiaDialogFragment dialog = DiaDialogFragment.newInstance(dia);
        dialog.show(getSupportFragmentManager(), "DIA_DIALOG");
    }

    @Override
    public void salvarDia(Dia dia) {
        repositorio.salvarDia(dia);
        dia.calcular();

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("FRAGMENTO");

        if (fragment instanceof DiaFragment) {
            ((DiaFragment)fragment).atualizar();
        } else if (fragment instanceof DiaAtualFragment) {
            ((DiaAtualFragment)fragment).atualizar();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("FRAGMENTO");

        if(fragment != null) {
            Object obj = fragment.getArguments().getSerializable(Util.PARAMETRO);
            if(obj instanceof Ano) {
                titulo((Ano)obj, null, null);
            } else if(obj instanceof Mes) {
                titulo(null, (Mes)obj, null);
            } else {
                toolbar.setTitle(R.string.app_name);
            }
        }
    }
}