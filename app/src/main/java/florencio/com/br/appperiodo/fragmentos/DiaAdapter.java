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

public class DiaAdapter extends BaseAdapter {
    private List<Dia> objetos;
    private Context context;

    public DiaAdapter(List<Dia> objetos, Context context) {
        this.objetos = objetos;
        this.context = context;
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

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.atualizarView(obj);

        convertView.setBackgroundColor(obj.getNumero() % 2 == 0 ? Color.GRAY : Color.LTGRAY);

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

            txtDebito.setText(obj.getNoiteCalFmt());
            txtCredito.setText(obj.getNoiteCalFmt());
        }

        String get(String s) {
            return s.length() == 1 ? "0" + s : s;
        }
    }
}