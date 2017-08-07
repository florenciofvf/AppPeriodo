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
import florencio.com.br.appperiodo.persistencia.Repositorio;
import florencio.com.br.appperiodo.util.Util;

public class AnoFragment extends Fragment implements AdapterView.OnItemClickListener {
    private AnoFragmentListener listener;
    private Repositorio repositorio;
    private ProgressBar progressBar;
    private ListView listView;
    private Integer ano;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AnoFragmentListener) {
            listener = (AnoFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " >>> AnoFragment.onAttach()");
        }
    }

    public static AnoFragment newInstance(Integer ano) {
        AnoFragment fragment = new AnoFragment();
        Bundle args = new Bundle();
        args.putInt(Util.PARAMETRO, ano);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ano = getArguments().getInt(Util.PARAMETRO);
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
        new Tarefa().execute(ano);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Ano obj = (Ano) listView.getAdapter().getItem(position);
        listener.exibirMeses(obj);
    }

    public interface AnoFragmentListener {
        void exibirMeses(Ano ano);
    }

    private class Tarefa extends AsyncTask<Integer, Void, List<Ano>> {

        @Override
        protected List<Ano> doInBackground(Integer... params) {
            Ano obj = new Ano(params[0]);
            repositorio.salvarAno(obj);
            return repositorio.listarAnos();
        }

        @Override
        protected void onPostExecute(List<Ano> objetos) {
            AnoAdapter adapter = new AnoAdapter(objetos, getActivity());
            listView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
        }
    }
}