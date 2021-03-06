package florencio.com.br.appperiodo.fragmentos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.dominio.Mes;
import florencio.com.br.appperiodo.persistencia.Repositorio;
import florencio.com.br.appperiodo.util.Tolerancia;
import florencio.com.br.appperiodo.util.Util;

public class DiaFragment extends Fragment implements ExpandableListView.OnChildClickListener {
	private ExpandableListView expandableListView;
	private DiaFragmentListener listener;
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
		progressBar = view.findViewById(R.id.progressBar);
		expandableListView = view.findViewById(R.id.expandableListView);
		expandableListView.setOnChildClickListener(this);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_contexto_dia, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.itemExportar) {
			String conteudo = adapter.gerarConteudoExportacao();
			enviarEmail(conteudo);

		} else if (item.getItemId() == R.id.itemExportarDados) {
			String conteudo = adapter.gerarConteudoExportacaoDados();
			enviarEmail(conteudo);

		} else if (item.getItemId() == R.id.itemImportar) {
			String url = Util.getStringPref(getActivity(), R.string.url_importacao_key, R.string.url_importacao_default);
			new TarefaImportar().execute(url);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		repositorio = new Repositorio(getActivity());
		txtRodape = new TextView(getActivity());
		txtRodape.setGravity(Gravity.RIGHT);
		txtRodape.setPadding(8, 8, 8, 8);
		expandableListView.addFooterView(txtRodape);
		new Tarefa().execute(mes);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Dia obj = adapter.getObjeto(groupPosition);

		if (obj != null) {
			listener.clickDia(obj);
			return true;
		}

		return false;
	}

	public interface DiaFragmentListener {
		void clickDia(Dia dia);
	}

	@SuppressLint("StaticFieldLeak")
	private class Tarefa extends AsyncTask<Mes, Void, List<Dia>> {

		@Override
		protected List<Dia> doInBackground(Mes... params) {
			Mes obj = params[0];
			Tolerancia tolerancia = Util.getTolerancia(getActivity());
			return repositorio.montarDiasDoMes(obj, tolerancia.getMenosHorario(), tolerancia.getMaisHorario());
		}

		@Override
		protected void onPostExecute(List<Dia> objetos) {
			adapter = new DiaAdapter(objetos, getActivity());
			expandableListView.setAdapter(adapter);
			expandableListView.setIndicatorBounds(expandableListView.getWidth() - 60, expandableListView.getWidth());
			progressBar.setVisibility(View.GONE);

			atualizar();
		}
	}

	public void atualizar() {
		adapter.notifyDataSetChanged();

		int totalLei = 0;
		int credito = 0;
		int debito = 0;
		int total = 0;

		for (Dia d : adapter.getObjetos()) {
			totalLei += d.getTotalLei();
			credito += d.getCredito();
			debito += d.getDebito();
			total += d.getTotal();
		}

		String sb = "SINCRONIZADO|VALIDO|COM_ID|OBSERVACAO" + Util.QUEBRA +
				" TOTAL: " + Util.totalFmt(total) + Util.QUEBRA +
				" VÁLIDO: " + Util.totalFmt(totalLei) + Util.QUEBRA +
				" DÉBITO: " + Util.totalFmt(debito) + Util.QUEBRA +
				" CRÉDITO: " + Util.totalFmt(credito) + Util.QUEBRA +
				getSaldo(debito, credito);

		txtRodape.setText(sb);
	}

	private String getSaldo(int debito, int credito) {
		if (debito == credito) {
			return Util.VAZIO;
		}

		if (debito > credito) {
			return " NEGATIVO: " + Util.totalFmt(debito - credito);
		} else {
			return " POSITIVO: " + Util.totalFmt(credito - debito);
		}
	}

	public void atualizarExclusao() {
		new Tarefa().execute(mes);
	}

	private void enviarEmail(String conteudo) {
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

	@SuppressLint("StaticFieldLeak")
	private class TarefaImportar extends AsyncTask<String, Void, List<String>> {

		@Override
		protected List<String> doInBackground(String... params) {
			List<String> resposta = new ArrayList<>();
			String s = params[0];

			try {
				URL url = new URL(s);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				conn.setDoOutput(false);

				conn.connect();

				int resp = conn.getResponseCode();

				if (resp == HttpURLConnection.HTTP_OK) {
					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String linha = br.readLine();

					while (linha != null) {
						if (linha.length() > 0) {
							resposta.add(linha);
						}
						linha = br.readLine();
					}

					br.close();
				}

			} catch (Exception e) {
				Log.i("ERRO>>>", e.getMessage());
			}

			return resposta;
		}

		@Override
		protected void onPostExecute(List<String> lista) {
			Tolerancia tolerancia = Util.getTolerancia(getActivity());
			adapter.importarConteudo(lista, tolerancia.getMenosHorario(), tolerancia.getMaisHorario());
			atualizar();
		}
	}
}