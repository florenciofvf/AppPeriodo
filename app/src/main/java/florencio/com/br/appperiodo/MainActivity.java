package florencio.com.br.appperiodo;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.Calendar;

import florencio.com.br.appperiodo.dominio.Ano;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.dominio.Mes;
import florencio.com.br.appperiodo.fragmentos.AnoFragment;
import florencio.com.br.appperiodo.fragmentos.DiaDialog;
import florencio.com.br.appperiodo.fragmentos.DiaFragment;
import florencio.com.br.appperiodo.fragmentos.MesFragment;
import florencio.com.br.appperiodo.persistencia.Repositorio;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AnoFragment.AnoFragmentListener, MesFragment.MesFragmentListener,
        DiaFragment.DiaFragmentListener, DiaDialog.DiaDialogListener {

    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Repositorio repositorio;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        repositorio = new Repositorio(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        drawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (item.getItemId() == R.id.itemAno) {
            AnoFragment fragment = AnoFragment.newInstance(getAnoAtual());
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
        }

        drawerLayout.closeDrawers();
        transaction.commit();
        return true;
    }

    private Integer getAnoAtual() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    @Override
    public void exibirMeses(Ano ano) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        MesFragment fragment = MesFragment.newInstance(ano);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void exibirDias(Mes mes) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        DiaFragment fragment = DiaFragment.newInstance(mes);
        transaction.replace(R.id.container, fragment, "DIA_FRAGMENT");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void clickDia(Dia dia) {
        DiaDialog dialog = DiaDialog.newInstance(dia);
        dialog.show(getSupportFragmentManager(), "DIA_DIALOG");
    }

    @Override
    public void salvarDia(Dia dia) {
        repositorio.salvarDia(dia);
        dia.calcular();

        FragmentManager manager = getSupportFragmentManager();
        DiaFragment fragment = (DiaFragment) manager.findFragmentByTag("DIA_FRAGMENT");

        if (fragment != null) {
            fragment.atualizar();
        }
    }
}