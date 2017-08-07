package florencio.com.br.appperiodo.fragmentos;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.util.Util;

public class DiaAdapter extends BaseAdapter {
    private List<Dia> objetos;
    private Context context;

    public DiaAdapter(List<Dia> objetos, Context context) {
        this.objetos = objetos;
        this.context = context;
    }

    public List<Dia> getObjetos() {
        return objetos;
    }

    @Override
    public int getCount() {
        return objetos.size();
    }

    @Override
    public Object getItem(int position) {
        return objetos.get(position);
    }

    @Override
    public long getItemId(int position) {
        Dia obj = objetos.get(position);
        return obj.get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Dia obj = objetos.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dia_layout, null);
            convertView.setTag(new ViewHolder(convertView));
        }

        final int cor = obj.getNumero() % 2 == 0 ? Color.WHITE : Color.LTGRAY;

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.atualizarView(obj);

        //convertView.setBackgroundColor(cor);

        return convertView;
    }

    private class ViewHolder {
        TextView txtNumero;
        TextView txtNome;
        TextView txtObs;

        TextView txtManhaIni;
        TextView txtManhaFim;
        TextView txtManhaCal;

        TextView txtTardeIni;
        TextView txtTardeFim;
        TextView txtTardeCal;

        TextView txtNoiteIni;
        TextView txtNoiteFim;
        TextView txtNoiteCal;

        TextView txtTotal;
        TextView txtDebito;
        TextView txtCredito;

        ViewHolder(View view) {
            txtNumero = (TextView) view.findViewById(R.id.txtNumero);
            txtNome = (TextView) view.findViewById(R.id.txtNome);
            txtObs = (TextView) view.findViewById(R.id.txtObs);

            txtManhaIni = (TextView) view.findViewById(R.id.txtManhaIni);
            txtManhaFim = (TextView) view.findViewById(R.id.txtManhaFim);
            txtManhaCal = (TextView) view.findViewById(R.id.txtManhaCal);

            txtTardeIni = (TextView) view.findViewById(R.id.txtTardeIni);
            txtTardeFim = (TextView) view.findViewById(R.id.txtTardeFim);
            txtTardeCal = (TextView) view.findViewById(R.id.txtTardeCal);

            txtNoiteIni = (TextView) view.findViewById(R.id.txtNoiteIni);
            txtNoiteFim = (TextView) view.findViewById(R.id.txtNoiteFim);
            txtNoiteCal = (TextView) view.findViewById(R.id.txtNoiteCal);

            txtTotal = (TextView) view.findViewById(R.id.txtTotal);
            txtDebito = (TextView) view.findViewById(R.id.txtDebito);
            txtCredito = (TextView) view.findViewById(R.id.txtCredito);
        }

        void atualizarView(Dia obj) {
            txtNumero.setText(get(obj.getNumero().toString()));
            txtNome.setText(obj.getNome());
            txtObs.setText(obj.getObs());

            txtManhaIni.setText(obj.getManhaIniFmt());
            txtManhaFim.setText(obj.getManhaFimFmt());
            txtManhaCal.setText(obj.getManhaCalFmt());

            txtTardeIni.setText(obj.getTardeIniFmt());
            txtTardeFim.setText(obj.getTardeFimFmt());
            txtTardeCal.setText(obj.getTardeCalFmt());

            txtNoiteIni.setText(obj.getNoiteIniFmt());
            txtNoiteFim.setText(obj.getNoiteFimFmt());
            txtNoiteCal.setText(obj.getNoiteCalFmt());

            txtTotal.setText(obj.getTotalFmt());
            txtDebito.setText(obj.getDebito());
            txtCredito.setText(obj.getCredito());

            txtTotal.setTextColor(obj.isValido() ? Color.BLUE : Color.BLACK);

            if (obj.isValido()) {
                txtDebito.setTextColor(Util.ZERO_ZERO.equals(obj.getDebito()) ? Color.BLACK : Color.RED);
                txtCredito.setTextColor(Util.ZERO_ZERO.equals(obj.getCredito()) ? Color.BLACK : 0xFF008800);
            } else {
                txtDebito.setTextColor(Color.BLACK);
                txtCredito.setTextColor(Color.BLACK);
            }
        }

        String get(String s) {
            return s.length() == 1 ? "0" + s : s;
        }
    }
}