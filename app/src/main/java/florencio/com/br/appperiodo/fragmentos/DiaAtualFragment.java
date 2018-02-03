package florencio.com.br.appperiodo.fragmentos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import florencio.com.br.appperiodo.MainActivity;
import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.persistencia.Repositorio;
import florencio.com.br.appperiodo.util.Lei;
import florencio.com.br.appperiodo.util.Util;

public class DiaAtualFragment extends Fragment {
    private static final String DIA_PARAM = "dia";
    private DiaAtualFragmentListener listener;
    private CheckBox chkSincronizado;
    private Repositorio repositorio;
    private TextView txtManhaCal;
    private TextView txtTardeCal;
    private TextView txtNoiteCal;
    private TextView txtCredito;
    private TextView btnManhaIni;
    private TextView btnManhaFim;
    private TextView btnTardeIni;
    private TextView btnTardeFim;
    private TextView btnNoiteIni;
    private TextView btnNoiteFim;
    private CheckBox chkEspecial;
    private TextView txtTotalLei;
    private TextView txtTitulo;
    private TextView txtDebito;
    private CheckBox chkValido;
    private Button btnDefazer;
    private TextView txtTotal;
    private EditText edtObs;
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

        txtManhaCal = (TextView) view.findViewById(R.id.txtManhaCal);
        txtTardeCal = (TextView) view.findViewById(R.id.txtTardeCal);
        txtNoiteCal = (TextView) view.findViewById(R.id.txtNoiteCal);
        chkEspecial = (CheckBox) view.findViewById(R.id.chkEspecial);
        chkSincronizado = (CheckBox) view.findViewById(R.id.chkSincronizado);
        txtTotalLei = (TextView) view.findViewById(R.id.txtTotalLei);
        txtCredito = (TextView) view.findViewById(R.id.txtCredito);
        txtTitulo = (TextView) view.findViewById(R.id.txtTitulo);
        txtDebito = (TextView) view.findViewById(R.id.txtDebito);
        chkValido = (CheckBox) view.findViewById(R.id.chkValido);
        txtTotal = (TextView) view.findViewById(R.id.txtTotal);
        edtObs = (EditText) view.findViewById(R.id.edtObs);
        txtObs = (TextView) view.findViewById(R.id.txtObs);
        txtDia = (TextView) view.findViewById(R.id.txtDia);

        Button btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.setSincronizado(chkSincronizado.isChecked() ? 1 : 0);
                dia.setEspecial(chkEspecial.isChecked() ? 1 : 0);
                dia.setValido(chkValido.isChecked() ? 1 : 0);
                dia.setObs(edtObs.getText().toString());
                btnDefazer.setEnabled(false);
                listener.salvarDia(dia);
            }
        });

        Button btnLembrete = (Button) view.findViewById(R.id.btnLembrete);
        btnLembrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarLembrete();
            }
        });

        btnDefazer = (Button) view.findViewById(R.id.btnDesfazer);
        btnDefazer.setEnabled(false);
        btnDefazer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lei lei = Util.getLei(getActivity());
                dia.limparHorarios();
                repositorio.sincronizarDia(dia, lei.getToleranciaSaida(), lei.getExcessoExtra());
                atualizar(getActivity());
                btnDefazer.setEnabled(false);
            }
        });

        btnManhaIni = (TextView) view.findViewById(R.id.btnManhaIni);
        btnManhaFim = (TextView) view.findViewById(R.id.btnManhaFim);
        btnTardeIni = (TextView) view.findViewById(R.id.btnTardeIni);
        btnTardeFim = (TextView) view.findViewById(R.id.btnTardeFim);
        btnNoiteIni = (TextView) view.findViewById(R.id.btnNoiteIni);
        btnNoiteFim = (TextView) view.findViewById(R.id.btnNoiteFim);

        return view;
    }

    private void criarLembrete() {
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Intent it = new Intent(getActivity(), MainActivity.class);
                it.putExtra(Util.VIBRAR, "100,500,200,500,300,500,400,500,500,500");

                PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, it, 0);

                AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                c.set(Calendar.SECOND, 0);

                manager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

                Toast.makeText(getActivity(), R.string.label_agendado, Toast.LENGTH_SHORT).show();
            }
        };

        Calendar c = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                listener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
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

        txtManhaCal.setText(dia.getManhaCalFmt());
        txtTardeCal.setText(dia.getTardeCalFmt());
        txtNoiteCal.setText(dia.getNoiteCalFmt());
        txtTotalLei.setText(dia.getTotalLeiFmt());
        chkEspecial.setChecked(dia.isEspecial());
        chkSincronizado.setChecked(dia.isSincronizado());
        txtCredito.setText(dia.getCredito());
        chkValido.setChecked(dia.isValido());
        txtTotal.setText(dia.getTotalFmt());
        txtTitulo.setText(criarTitulo(dia));
        txtDebito.setText(dia.getDebito());
        txtDia.setText(dia.getNome());
        txtObs.setText(dia.getObs());
        edtObs.setText(dia.getObs());

        txtTotalLei.setTextColor(dia.isValido() ? Color.BLUE : Color.BLACK);
        txtTotal.setTextColor(dia.isValido() ? Color.BLUE : Color.BLACK);

        if (dia.isValido()) {
            txtDebito.setTextColor(Util.ZERO_ZERO.equals(dia.getDebito()) ? Color.BLACK : Color.RED);
            txtCredito.setTextColor(Util.ZERO_ZERO.equals(dia.getCredito()) ? Color.BLACK : Color.MAGENTA);
        } else {
            txtDebito.setTextColor(Color.BLACK);
            txtCredito.setTextColor(Color.BLACK);
        }
    }

    private class OnClick implements View.OnClickListener {
        final String campo;

        OnClick(String campo) {
            this.campo = campo;
        }

        @Override
        public void onClick(View v) {
            TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                    new AtualizaHora(campo), Util.getHora(campo, dia), Util.getMinuto(campo, dia), true);
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

    private static String criarTitulo(Dia dia) {
        return get(dia.getNumero()) + "/" + get(dia.getMes().getNumero()) + "/" + get(dia.getMes().getAno().getNumero());
    }

    private static String get(Integer i) {
        return i < 10 ? "0" + i.toString() : i.toString();
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
            Lei lei = Util.getLei(getActivity());
            btnDefazer.setEnabled(desfazer);
            dia.processar(lei.getToleranciaSaida(), lei.getExcessoExtra());
            atualizar(getActivity());
        }
    }
}