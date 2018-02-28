package florencio.com.br.appperiodo.fragmentos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import florencio.com.br.appperiodo.MainActivity;
import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.persistencia.Repositorio;
import florencio.com.br.appperiodo.util.Tolerancia;
import florencio.com.br.appperiodo.util.Util;

public class DiaAtualFragment extends Fragment {
	private static final String DIA_PARAM = "dia";
	private DiaAtualFragmentListener listener;
	private CheckBox chkSincronizado;
	private Repositorio repositorio;
	private TextView txtAgendamento;
	private TextView txtManhaCal;
	private TextView txtTardeCal;
	private TextView txtNoiteCal;

	private TextView btnManhaIni;
	private TextView btnManhaFim;
	private TextView btnTardeIni;
	private TextView btnTardeFim;
	private TextView btnNoiteIni;
	private TextView btnNoiteFim;

	private TextView lblManhaIni;
	private TextView lblManhaFim;
	private TextView lblTardeIni;
	private TextView lblTardeFim;
	private TextView lblNoiteIni;
	private TextView lblNoiteFim;

	private CheckBox chkEspecial;
	private TextView txtTotalLei;
	private TextView txtCredito;
	private TextView txtTitulo;
	private TextView txtDebito;
	private CheckBox chkValido;
	private Button btnDefazer;
	private TextView txtTotal;
	private TextView txtObs;
	private TextView txtDia;
	private Dia dia;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof DiaAtualFragmentListener) {
			listener = (DiaAtualFragmentListener) context;
		} else {
			throw new RuntimeException(context.toString() + " >>> DiaAtualFragment.onAttach()");
		}
	}

	public static DiaAtualFragment newInstance(Dia obj) {
		DiaAtualFragment fragment = new DiaAtualFragment();
		Bundle args = new Bundle();
		args.putSerializable(DIA_PARAM, obj);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		dia = (Dia) getArguments().getSerializable(DIA_PARAM);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dia_atual_layout, null);

		chkSincronizado = view.findViewById(R.id.chkSincronizado);
		txtAgendamento = view.findViewById(R.id.txtAgendamento);
		txtManhaCal = view.findViewById(R.id.txtManhaCal);
		txtTardeCal = view.findViewById(R.id.txtTardeCal);
		txtNoiteCal = view.findViewById(R.id.txtNoiteCal);
		chkEspecial = view.findViewById(R.id.chkEspecial);
		txtTotalLei = view.findViewById(R.id.txtTotalLei);
		txtCredito = view.findViewById(R.id.txtCredito);
		txtTitulo = view.findViewById(R.id.txtTitulo);
		txtDebito = view.findViewById(R.id.txtDebito);
		chkValido = view.findViewById(R.id.chkValido);
		txtTotal = view.findViewById(R.id.txtTotal);
		txtObs = view.findViewById(R.id.txtObs);
		txtDia = view.findViewById(R.id.txtDia);

		Button btnSalvar = view.findViewById(R.id.btnSalvar);
		btnSalvar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dia.setSincronizado(chkSincronizado.isChecked() ? 1 : 0);
				dia.setEspecial(chkEspecial.isChecked() ? 1 : 0);
				dia.setValido(chkValido.isChecked() ? 1 : 0);
				btnDefazer.setEnabled(false);
				listener.salvarDia(dia);
			}
		});

		Button btnLembrete = view.findViewById(R.id.btnLembrete);
		btnLembrete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				criarLembrete();
			}
		});

		btnDefazer = view.findViewById(R.id.btnDesfazer);
		btnDefazer.setEnabled(false);
		btnDefazer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Tolerancia tolerancia = Util.getTolerancia(getActivity());
				dia.limparHorarios();
				repositorio.sincronizarDia(dia, tolerancia.getMenosHorario(), tolerancia.getMaisHorario());
				atualizar(getActivity());
				btnDefazer.setEnabled(false);
			}
		});

		btnManhaIni = view.findViewById(R.id.btnManhaIni);
		btnManhaFim = view.findViewById(R.id.btnManhaFim);
		btnTardeIni = view.findViewById(R.id.btnTardeIni);
		btnTardeFim = view.findViewById(R.id.btnTardeFim);
		btnNoiteIni = view.findViewById(R.id.btnNoiteIni);
		btnNoiteFim = view.findViewById(R.id.btnNoiteFim);

		lblManhaIni = view.findViewById(R.id.lblManhaIni);
		lblManhaFim = view.findViewById(R.id.lblManhaFim);
		lblTardeIni = view.findViewById(R.id.lblTardeIni);
		lblTardeFim = view.findViewById(R.id.lblTardeFim);
		lblNoiteIni = view.findViewById(R.id.lblNoiteIni);
		lblNoiteFim = view.findViewById(R.id.lblNoiteFim);

		lblManhaIni.setPaintFlags(lblManhaIni.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		lblManhaFim.setPaintFlags(lblManhaFim.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		lblTardeIni.setPaintFlags(lblTardeIni.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		lblTardeFim.setPaintFlags(lblTardeFim.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		lblNoiteIni.setPaintFlags(lblNoiteIni.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		lblNoiteFim.setPaintFlags(lblNoiteFim.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

		return view;
	}

	private void criarLembrete() {
		TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Intent it = new Intent(getActivity(), MainActivity.class);
				it.putExtra(Util.VIBRAR, Util.getStringPref(getActivity(), R.string.vibrar_key, R.string.vibrar_default));

				PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, it, 0);

				AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

				Calendar c = Calendar.getInstance();
				c.set(Calendar.HOUR_OF_DAY, hourOfDay);
				c.set(Calendar.MINUTE, minute);
				c.set(Calendar.SECOND, 0);

				if (manager != null) {
					manager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
				}

				Toast.makeText(getActivity(), R.string.label_agendado, Toast.LENGTH_SHORT).show();
			}
		};

		Calendar c = Calendar.getInstance();
		TimePickerDialog dialog = new TimePickerDialog(getActivity(), listener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
		dialog.show();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		btnManhaIni.setOnClickListener(new OnClick(Util.MANHA_INI));
		btnManhaFim.setOnClickListener(new OnClick(Util.MANHA_FIM));
		btnTardeIni.setOnClickListener(new OnClick(Util.TARDE_INI));
		btnTardeFim.setOnClickListener(new OnClick(Util.TARDE_FIM));
		btnNoiteIni.setOnClickListener(new OnClick(Util.NOITE_INI));
		btnNoiteFim.setOnClickListener(new OnClick(Util.NOITE_FIM));

		repositorio = new Repositorio(getActivity());
		atualizar(getActivity());
	}

	public void atualizar(Context context) {
		Util.atualizarText(Util.MANHA_INI, dia, btnManhaIni, context);
		Util.atualizarText(Util.MANHA_FIM, dia, btnManhaFim, context);
		Util.atualizarText(Util.TARDE_INI, dia, btnTardeIni, context);
		Util.atualizarText(Util.TARDE_FIM, dia, btnTardeFim, context);
		Util.atualizarText(Util.NOITE_INI, dia, btnNoiteIni, context);
		Util.atualizarText(Util.NOITE_FIM, dia, btnNoiteFim, context);

		txtTotalLei.setTextColor(dia.isValido() ? Color.BLUE : Color.BLACK);
		txtTotal.setTextColor(dia.isValido() ? Color.BLUE : Color.BLACK);
		//txtAgendamento.setText(Util.formatarHora(dia.getAgendamento()));
		chkSincronizado.setChecked(dia.isSincronizado());
		txtManhaCal.setText(dia.getManhaCalFmt());
		txtTardeCal.setText(dia.getTardeCalFmt());
		txtNoiteCal.setText(dia.getNoiteCalFmt());
		txtTotalLei.setText(dia.getTotalLeiFmt());
		chkEspecial.setChecked(dia.isEspecial());
		txtTitulo.setText(Util.criarTitulo(dia));
		txtCredito.setText(dia.getCreditoFmt());
		txtDebito.setText(dia.getDebitoFmt());
		chkValido.setChecked(dia.isValido());
		txtTotal.setText(dia.getTotalFmt());
		txtDia.setText(dia.getNome());
		txtObs.setText(dia.getObs());

		if (dia.isValido()) {
			txtCredito.setTextColor(Util.ZERO_ZERO.equals(dia.getCreditoFmt()) ? Color.BLACK : Color.MAGENTA);
			txtDebito.setTextColor(Util.ZERO_ZERO.equals(dia.getDebitoFmt()) ? Color.BLACK : Color.RED);
		} else {
			txtCredito.setTextColor(Color.BLACK);
			txtDebito.setTextColor(Color.BLACK);
		}
	}

	private class OnClick implements View.OnClickListener {
		final String campo;

		OnClick(String campo) {
			this.campo = campo;
		}

		@Override
		public void onClick(View v) {
			TimePickerDialog dialog = new TimePickerDialog(getActivity(), new AtualizaHora(campo), Util.getHora(campo, dia), Util.getMinuto(campo, dia), true);
			dialog.show();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	public interface DiaAtualFragmentListener {
		void salvarDia(Dia dia);
	}

	private class AtualizaHora implements TimePickerDialog.OnTimeSetListener {
		final String campo;

		AtualizaHora(String campo) {
			this.campo = campo;
		}

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			Calendar c = Util.criarCalendarZero();
			c.set(Calendar.HOUR_OF_DAY, hourOfDay);
			c.set(Calendar.MINUTE, minute);

			long valor = hourOfDay == 0 && minute == 0 ? 0 : c.getTimeInMillis();
			boolean desfazer = false;

			if (Util.MANHA_INI.equals(campo)) {
				desfazer = valor != dia.getManhaIni();
				dia.setManhaIni(valor);

			} else if (Util.MANHA_FIM.equals(campo)) {
				desfazer = valor != dia.getManhaFim();
				dia.setManhaFim(valor);

			} else if (Util.TARDE_INI.equals(campo)) {
				desfazer = valor != dia.getTardeIni();
				dia.setTardeIni(valor);

			} else if (Util.TARDE_FIM.equals(campo)) {
				desfazer = valor != dia.getTardeFim();
				dia.setTardeFim(valor);

			} else if (Util.NOITE_INI.equals(campo)) {
				desfazer = valor != dia.getNoiteIni();
				dia.setNoiteIni(valor);

			} else if (Util.NOITE_FIM.equals(campo)) {
				desfazer = valor != dia.getNoiteFim();
				dia.setNoiteFim(valor);
			}

			Tolerancia tolerancia = Util.getTolerancia(getActivity());
			btnDefazer.setEnabled(desfazer);
			dia.processar(tolerancia.getMenosHorario(), tolerancia.getMaisHorario());
			atualizar(getActivity());
		}
	}
}