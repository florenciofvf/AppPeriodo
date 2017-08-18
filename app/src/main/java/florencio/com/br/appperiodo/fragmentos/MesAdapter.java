package florencio.com.br.appperiodo.fragmentos;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Mes;
import florencio.com.br.appperiodo.util.Util;

public class MesAdapter extends BaseAdapter {
    private final List<Mes> objetos;
    private final Context context;

    public MesAdapter(List<Mes> objetos, Context context) {
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
        Mes obj = objetos.get(position);
        return obj.get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mes obj = objetos.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.mes_item_layout, null);
            convertView.setTag(new ViewHolder(convertView));
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.atualizarView(obj);

        boolean config = false;

        if (obj.isAtual()) {
            convertView.setBackground(context.getDrawable(R.drawable.bg_atual));
            config = true;
        }

        if(!config) {
            convertView.setBackground(holder.corOriginal);
        }

        return convertView;
    }

    private class ViewHolder {
        Drawable corOriginal;
        TextView txtNome;

        ViewHolder(View view) {
            corOriginal = view.getBackground();

            txtNome = (TextView) view.findViewById(R.id.txtNome);
        }

        void atualizarView(Mes obj) {
            txtNome.setText(obj.getNome());
        }
    }
}