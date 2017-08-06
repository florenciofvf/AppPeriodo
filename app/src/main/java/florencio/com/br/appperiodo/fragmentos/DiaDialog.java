package florencio.com.br.appperiodo.fragmentos;

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
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.util.Util;

public class DiaDialog extends DialogFragment {
    private final String MANHA_INI = "MANHA_INI";
    private final String MANHA_FIM = "MANHA_FIM";
    private final String TARDE_INI = "TARDE_INI";
    private final String TARDE_FIM = "TARDE_FIM";
    private final String NOITE_INI = "NOITE_INI";
    private final String NOITE_FIM = "NOITE_FIM";
    private static final String DIA_PARAM = "dia";
    private DiaDialogListener listener;
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

        Button btnCancelar = (Button)view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button btnSalvar = (Button)view.findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.salvarDia(dia);
            }
        });

        view.findViewById(R.id.btnManhaIni).setOnClickListener(new OnClick(MANHA_INI));
        view.findViewById(R.id.btnManhaFim).setOnClickListener(new OnClick(MANHA_FIM));

        view.findViewById(R.id.btnTardeIni).setOnClickListener(new OnClick(TARDE_INI));
        view.findViewById(R.id.btnTardeFim).setOnClickListener(new OnClick(TARDE_FIM));

        view.findViewById(R.id.btnNoiteIni).setOnClickListener(new OnClick(NOITE_INI));
        view.findViewById(R.id.btnNoiteFim).setOnClickListener(new OnClick(NOITE_FIM));

        return view;
    }

    private class OnClick implements View.OnClickListener {
        final String campo;

        public OnClick(String campo) {
            this.campo = campo;
        }

        @Override
        public void onClick(View v) {
            TimePickerDialog dialog = new TimePickerDialog(getActivity(), new AtualizaHora(campo), getHora(campo), getMinuto(campo), true);
            dialog.show();
        }

        Integer getHora(String campo) {
            if(MANHA_INI.equals(campo)) {
                return Util.getHora(dia.getManhaIni());
            } else if(MANHA_FIM.equals(campo)) {
                return Util.getHora(dia.getManhaFim());

            } else if(TARDE_INI.equals(campo)) {
                return Util.getHora(dia.getTardeIni());
            } else if(TARDE_FIM.equals(campo)) {
                return Util.getHora(dia.getTardeFim());

            } else if(NOITE_INI.equals(campo)) {
                return Util.getHora(dia.getNoiteIni());
            } else if(NOITE_FIM.equals(campo)) {
                return Util.getHora(dia.getNoiteFim());
            }
            return 0;
        }

        Integer getMinuto(String campo) {
            if(MANHA_INI.equals(campo)) {
                return Util.getMinuto(dia.getManhaIni());
            } else if(MANHA_FIM.equals(campo)) {
                return Util.getMinuto(dia.getManhaFim());

            } else if(TARDE_INI.equals(campo)) {
                return Util.getMinuto(dia.getTardeIni());
            } else if(TARDE_FIM.equals(campo)) {
                return Util.getMinuto(dia.getTardeFim());

            } else if(NOITE_INI.equals(campo)) {
                return Util.getMinuto(dia.getNoiteIni());
            } else if(NOITE_FIM.equals(campo)) {
                return Util.getMinuto(dia.getNoiteFim());
            }
            return 0;
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

            if(MANHA_INI.equals(campo)) {
                dia.setManhaIni(c.getTimeInMillis());
            } else if(MANHA_FIM.equals(campo)) {
                dia.setManhaFim(c.getTimeInMillis());

            } else if(TARDE_INI.equals(campo)) {
                dia.setTardeIni(c.getTimeInMillis());
            } else if(TARDE_FIM.equals(campo)) {
                dia.setTardeFim(c.getTimeInMillis());

            } else if(NOITE_INI.equals(campo)) {
                dia.setNoiteIni(c.getTimeInMillis());
            } else if(NOITE_FIM.equals(campo)) {
                dia.setNoiteFim(c.getTimeInMillis());
            }
        }

        private Calendar criarCalendar() {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 0);
            c.set(Calendar.YEAR, 0);
            return c;
        }
    }
}