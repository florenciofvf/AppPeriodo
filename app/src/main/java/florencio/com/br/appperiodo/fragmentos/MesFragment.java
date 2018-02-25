package florencio.com.br.appperiodo.fragmentos;

import android.annotation.SuppressLint;
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
import florencio.com.br.appperiodo.dominio.Mes;
import florencio.com.br.appperiodo.persistencia.Repositorio;
import florencio.com.br.appperiodo.util.Util;

public class MesFragment extends Fragment implements AdapterView.OnItemClickListener {
	private MesFragmentListener listener;
	private Repositorio repositorio;
	private ProgressBar progressBar;
	private ListView listView;
	private Ano ano;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof MesFragmentListener) {
			listener = (MesFragmentListener) context;
		} else {
			throw new RuntimeException(context.toString() + " >>> MesFragment.onAttach()");
		}
	}

	public static MesFragment newInstance(Ano obj) {
		MesFragment fragment = new MesFragment();
		Bundle args = new Bundle();
		args.putSerializable(Util.PARAMETRO, obj);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ano = (Ano) getArguments().getSerializable(Util.PARAMETRO);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.lista_ano_mes_layout, null);
		progressBar = view.findViewById(R.id.progressBar);
		listView = view.findViewById(R.id.listView);
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
		Mes obj = (Mes) listView.getAdapter().getItem(position);
		listener.exibirDias(obj);
	}

	public interface MesFragmentListener {
		void exibirDias(Mes mes);
	}

	@SuppressLint("StaticFieldLeak")
	private class Tarefa extends AsyncTask<Ano, Void, List<Mes>> {

		@Override
		protected List<Mes> doInBackground(Ano... params) {
			Ano obj = params[0];
			return repositorio.listarMeses(obj);
		}

		@Override
		protected void onPostExecute(List<Mes> objetos) {
			MesAdapter adapter = new MesAdapter(objetos, getActivity());
			listView.setAdapter(adapter);
			progressBar.setVisibility(View.GONE);
		}
	}
}