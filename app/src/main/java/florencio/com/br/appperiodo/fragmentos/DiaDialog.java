package florencio.com.br.appperiodo.fragmentos;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.util.Util;

public class DiaDialog extends DialogFragment {
    private static final String DIA_PARAM = "dia";
    private DiaDialogListener listener;
    private Button btnManhaIni;
    private Button btnManhaFim;
    private Button btnTardeIni;
    private Button btnTardeFim;
    private Button btnNoiteIni;
    private Button btnNoiteFim;
    private EditText edtObs;
    private Dia dia;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DiaDialogListener) {
            listener = (DiaDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + " >>> DiaDialog.onAttach()");
        }
    }

    public static DiaDialog newInstance(Dia obj) {
        DiaDialog fragment = new DiaDialog();
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

        TextView txtTitulo = (TextView) view.findViewById(R.id.txtTitulo);
        txtTitulo.setText(criarTitulo(dia));

        edtObs = (EditText) view.findViewById(R.id.edtObs);
        edtObs.setText(dia.getObs());

        Button btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button btnSalvar = (Button) view.findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.setObs(edtObs.getText().toString());
                listener.salvarDia(dia);
                dismiss();
            }
        });

        btnManhaIni = (Button) view.findViewById(R.id.btnManhaIni);
        btnManhaIni.setOnClickListener(new OnClick(Util.MANHA_INI, dia, btnManhaIni));

        btnManhaFim = (Button) view.findViewById(R.id.btnManhaFim);
        btnManhaFim.setOnClickListener(new OnClick(Util.MANHA_FIM, dia, btnManhaFim));

        btnTardeIni = (Button) view.findViewById(R.id.btnTardeIni);
        btnTardeIni.setOnClickListener(new OnClick(Util.TARDE_INI, dia, btnTardeIni));

        btnTardeFim = (Button) view.findViewById(R.id.btnTardeFim);
        btnTardeFim.setOnClickListener(new OnClick(Util.TARDE_FIM, dia, btnTardeFim));

        btnNoiteIni = (Button) view.findViewById(R.id.btnNoiteIni);
        btnNoiteIni.setOnClickListener(new OnClick(Util.NOITE_INI, dia, btnNoiteIni));

        btnNoiteFim = (Button) view.findViewById(R.id.btnNoiteFim);
        btnNoiteFim.setOnClickListener(new OnClick(Util.NOITE_FIM, dia, btnNoiteFim));

        return view;
    }

    private class OnClick implements View.OnClickListener {
        final String campo;

        public OnClick(String campo, Dia dia, Button button) {
            Util.atualizarText(campo, dia, button);
            this.campo = campo;
        }

        @Override
        public void onClick(View v) {
            TimePickerDialog dialog = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,
                    new AtualizaHora(campo), Util.getHora(campo, dia), Util.getMinuto(campo, dia), true);
            dialog.show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface DiaDialogListener {
        void salvarDia(Dia dia);
    }

    private String criarTitulo(Dia dia) {
        return get(dia.getNumero()) + "/" + get(dia.getMes().getNumero()) + "/" + get(dia.getMes().getAno().getNumero());
    }

    private String get(Integer i) {
        return i < 10 ? "0" + i.toString() : i.toString();
    }

    private class AtualizaHora implements TimePickerDialog.OnTimeSetListener {
        final String campo;

        public AtualizaHora(String campo) {
            this.campo = campo;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar c = criarCalendar();
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            long valor = hourOfDay == 0 && minute == 0 ? 0 : c.getTimeInMillis();

            if (Util.MANHA_INI.equals(campo)) {
                dia.setManhaIni(valor);
                Util.atualizarText(campo, dia, btnManhaIni);
            } else if (Util.MANHA_FIM.equals(campo)) {
                dia.setManhaFim(valor);
                Util.atualizarText(campo, dia, btnManhaFim);

            } else if (Util.TARDE_INI.equals(campo)) {
                dia.setTardeIni(valor);
                Util.atualizarText(campo, dia, btnTardeIni);
            } else if (Util.TARDE_FIM.equals(campo)) {
                dia.setTardeFim(valor);
                Util.atualizarText(campo, dia, btnTardeFim);

            } else if (Util.NOITE_INI.equals(campo)) {
                dia.setNoiteIni(valor);
                Util.atualizarText(campo, dia, btnNoiteIni);
            } else if (Util.NOITE_FIM.equals(campo)) {
                dia.setNoiteFim(valor);
                Util.atualizarText(campo, dia, btnNoiteFim);
            }
        }

        private Calendar criarCalendar() {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 0);
            c.set(Calendar.YEAR, 0);
            return c;
        }
    }
}