package florencio.com.br.appperiodo.fragmentos;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Ano;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.dominio.Mes;
import florencio.com.br.appperiodo.persistencia.Repositorio;

public class DiaFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String MES_PARAM = "mes";
    private DiaFragmentListener listener;
    private Repositorio repositorio;
    private ProgressBar progressBar;
    private ListView listView;
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
        args.putSerializable(MES_PARAM, obj);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mes = (Mes) getArguments().getSerializable(MES_PARAM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        repositorio = new Repositorio(getActivity());
        new Tarefa().execute(mes);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Dia obj = (Dia) listView.getAdapter().getItem(position);
        listener.clickDia(obj);
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
            DiaAdapter adapter = new DiaAdapter(objetos, getActivity());
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
        }
    }
}