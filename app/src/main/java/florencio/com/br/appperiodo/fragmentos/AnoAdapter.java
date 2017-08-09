package florencio.com.br.appperiodo.fragmentos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Ano;
import florencio.com.br.appperiodo.util.Util;

public class AnoAdapter extends BaseAdapter {
    private final List<Ano> objetos;
    private final Context context;

    public AnoAdapter(List<Ano> objetos, Context context) {
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
        Ano obj = objetos.get(position);
        return obj.get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ano obj = objetos.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ano_item_layout, null);
            convertView.setTag(new ViewHolder(convertView));
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.atualizarView(obj);

        boolean config = false;

        if (obj.isAtual()) {
            convertView.setBackgroundColor(Util.COR_ATUAL);
            config = true;
        }

        if(!config) {
            convertView.setBackground(holder.corOriginal);
        }

        return convertView;
    }

    private class ViewHolder {
        Drawable corOriginal;
        TextView txtNumero;

        ViewHolder(View view) {
            corOriginal = view.getBackground();

            txtNumero = (TextView) view.findViewById(R.id.txtNumero);
        }

        void atualizarView(Ano obj) {
            txtNumero.setText(obj.getNumero().toString());
        }
    }
}