package florencio.com.br.appperiodo.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.dominio.Mes;
import florencio.com.br.appperiodo.persistencia.Repositorio;
import florencio.com.br.appperiodo.util.Util;

public class DiaFragment extends Fragment implements ExpandableListView.OnChildClickListener {
    private DiaFragmentListener listener;
    private ExpandableListView listView;
    private Repositorio repositorio;
    private ProgressBar progressBar;
    private TextView txtRodape;
    private DiaAdapter adapter;
    private Mes mes;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DiaFragmentListener) {
            listener = (DiaFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " >>> DiaFragment.onAttach()");
        }
    }

    public static DiaFragment newInstance(Mes obj) {
        DiaFragment fragment = new DiaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Util.PARAMETRO, obj);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mes = (Mes) getArguments().getSerializable(Util.PARAMETRO);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dia_fragment_layout, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        listView = (ExpandableListView) view.findViewById(R.id.listView);
        listView.setOnChildClickListener(this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_contexto_dia, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.itemEmail) {
            //String conteudo = adapter.gerarConteudoEmail();
            //enviarEmail(conteudo);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        repositorio = new Repositorio(getActivity());
        txtRodape = new TextView(getActivity());
        txtRodape.setGravity(Gravity.RIGHT);
        txtRodape.setPadding(8,8,8,8);
        listView.addFooterView(txtRodape);
        new Tarefa().execute(mes);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Dia obj = (Dia) adapter.getObjeto(groupPosition);

        if (obj != null) {
            listener.clickDia(obj);
            return true;
        }

        return false;
    }

    public interface DiaFragmentListener {
        void clickDia(Dia dia);
    }

    private class Tarefa extends AsyncTask<Mes, Void, List<Dia>> {

        @Override
        protected List<Dia> doInBackground(Mes... params) {
            Mes obj = params[0];
            return repositorio.montarDiasDoMes(obj);
        }

        @Override
        protected void onPostExecute(List<Dia> objetos) {
            adapter = new DiaAdapter(objetos, getActivity());
            listView.setAdapter(adapter);
            listView.setIndicatorBounds(listView.getWidth()-60, listView.getWidth());
            progressBar.setVisibility(View.GONE);

            int total = 0;
            for (Dia d : objetos) {
                total += d.getTotal();
            }

            txtRodape.setText("TOTAL DE HORAS: " + Util.totalFmt(total));
        }
    }

    public void atualizar() {
        adapter.notifyDataSetChanged();

        int total = 0;
        for (Dia d : adapter.getObjetos()) {
            total += d.getTotal();
        }

        txtRodape.setText("TOTAL DE HORAS: " + Util.totalFmt(total));
    }

    public void enviarEmail(String conteudo) {
        Intent it = new Intent(Intent.ACTION_SENDTO);
        it.setData(Uri.parse("mailto:"));
        it.putExtra(Intent.EXTRA_TEXT, conteudo);
        it.putExtra(Intent.EXTRA_SUBJECT, "Pontos");
        it.putExtra(Intent.EXTRA_EMAIL, new String[]{"florenciovieira@gmail.com"});
        it.putExtra(Intent.EXTRA_CC, new String[]{"florenciovieira@gmail.com"});
        if (it.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(it);
        }
    }
}