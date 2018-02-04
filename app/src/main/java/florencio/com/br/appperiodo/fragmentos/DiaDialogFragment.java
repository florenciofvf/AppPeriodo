package florencio.com.br.appperiodo.fragmentos;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.util.Util;

public class DiaDialogFragment extends DialogFragment {
    private static final String DIA_PARAM = "dia";
    private DiaDialogListener listener;
    private CheckBox chkSincronizado;
    private TextView btnManhaIni;
    private TextView btnManhaFim;
    private TextView btnTardeIni;
    private TextView btnTardeFim;
    private TextView btnNoiteIni;
    private TextView btnNoiteFim;
    private CheckBox chkEspecial;
    private CheckBox chkValido;
    private EditText edtObs;
    private Dia dia;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DiaDialogListener) {
            listener = (DiaDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " >>> DiaDialogFragment.onAttach()");
        }
    }

    public static DiaDialogFragment newInstance(Dia obj) {
        DiaDialogFragment fragment = new DiaDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(DIA_PARAM, obj);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dia = (Dia) getArguments().getSerializable(DIA_PARAM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dia_dialog_layout, null);

        TextView txtTitulo = view.findViewById(R.id.txtTitulo);
        txtTitulo.setText(criarTitulo(dia));

        edtObs = view.findViewById(R.id.edtObs);
        edtObs.setText(dia.getObs());

        chkSincronizado = view.findViewById(R.id.chkSincronizado);
        chkSincronizado.setChecked(dia.isSincronizado());

        chkEspecial = view.findViewById(R.id.chkEspecial);
        chkEspecial.setChecked(dia.isEspecial());

        chkValido = view.findViewById(R.id.chkValido);
        chkValido.setChecked(dia.isValido());

        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button btnSalvar = view.findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.setSincronizado(chkSincronizado.isChecked() ? 1 : 0);
                dia.setEspecial(chkEspecial.isChecked() ? 1 : 0);
                dia.setValido(chkValido.isChecked() ? 1 : 0);
                dia.setObs(edtObs.getText().toString());
                listener.salvarDia(dia);
                dismiss();
            }
        });

        btnManhaIni = view.findViewById(R.id.btnManhaIni);
        btnManhaIni.setOnClickListener(new OnClick(Util.MANHA_INI, dia, btnManhaIni));

        btnManhaFim = view.findViewById(R.id.btnManhaFim);
        btnManhaFim.setOnClickListener(new OnClick(Util.MANHA_FIM, dia, btnManhaFim));

        btnTardeIni = view.findViewById(R.id.btnTardeIni);
        btnTardeIni.setOnClickListener(new OnClick(Util.TARDE_INI, dia, btnTardeIni));

        btnTardeFim = view.findViewById(R.id.btnTardeFim);
        btnTardeFim.setOnClickListener(new OnClick(Util.TARDE_FIM, dia, btnTardeFim));

        btnNoiteIni = view.findViewById(R.id.btnNoiteIni);
        btnNoiteIni.setOnClickListener(new OnClick(Util.NOITE_INI, dia, btnNoiteIni));

        btnNoiteFim = view.findViewById(R.id.btnNoiteFim);
        btnNoiteFim.setOnClickListener(new OnClick(Util.NOITE_FIM, dia, btnNoiteFim));

        return view;
    }

    private class OnClick implements View.OnClickListener {
        final String campo;

        OnClick(String campo, Dia dia, TextView button) {
            Util.atualizarText(campo, dia, button, getActivity());
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

    public interface DiaDialogListener {
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

            if (Util.MANHA_INI.equals(campo)) {
                dia.setManhaIni(valor);
                Util.atualizarText(campo, dia, btnManhaIni, getActivity());
            } else if (Util.MANHA_FIM.equals(campo)) {
                dia.setManhaFim(valor);
                Util.atualizarText(campo, dia, btnManhaFim, getActivity());

            } else if (Util.TARDE_INI.equals(campo)) {
                dia.setTardeIni(valor);
                Util.atualizarText(campo, dia, btnTardeIni, getActivity());
            } else if (Util.TARDE_FIM.equals(campo)) {
                dia.setTardeFim(valor);
                Util.atualizarText(campo, dia, btnTardeFim, getActivity());

            } else if (Util.NOITE_INI.equals(campo)) {
                dia.setNoiteIni(valor);
                Util.atualizarText(campo, dia, btnNoiteIni, getActivity());
            } else if (Util.NOITE_FIM.equals(campo)) {
                dia.setNoiteFim(valor);
                Util.atualizarText(campo, dia, btnNoiteFim, getActivity());
            }
        }
    }
}