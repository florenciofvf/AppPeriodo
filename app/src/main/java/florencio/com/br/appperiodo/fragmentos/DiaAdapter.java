package florencio.com.br.appperiodo.fragmentos;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.dominio.Dia;
import florencio.com.br.appperiodo.util.Util;

public class DiaAdapter extends BaseExpandableListAdapter {
    private final List<Dia> objetos;
    private final Context context;

    public DiaAdapter(List<Dia> objetos, Context context) {
        this.objetos = objetos;
        this.context = context;
    }

    public List<Dia> getObjetos() {
        return objetos;
    }

    public Dia getObjeto(int posicao) {
        return objetos.get(posicao);
    }

    public String gerarConteudoExportacao() {
        StringBuilder sb = new StringBuilder();

        for (Dia obj : objetos) {
            sb.append(obj.gerarConteudoEmail());
        }

        return sb.toString();
    }

    public String gerarConteudoExportacaoDados() {
        StringBuilder sb = new StringBuilder();

        for (Dia obj : objetos) {
            sb.append(obj.gerarConteudoDados());
        }

        return sb.toString();
    }

    public void importarConteudo(List<String> lista, long toleranciaSaida, long excessoExtra) {
        Iterator<String> iterator = lista.iterator();
        while (iterator.hasNext()) {
            String linha = iterator.next();
            modificarDia(linha, toleranciaSaida, excessoExtra);
            iterator.remove();
        }
    }

    private void modificarDia(String s, long toleranciaSaida, long excessoExtra) {
        for (Dia obj : objetos) {
            String[] strings = s.split(" ");
            Integer numero = getNumero(strings);
            String nome = getNome(strings);

            if (obj.getNumero().equals(numero) && obj.getNome().equals(nome)) {
                obj.importar(strings);
                obj.processar(toleranciaSaida, excessoExtra);
            }
        }
    }

    private Integer getNumero(String[] strings) {
        try {
            return Integer.valueOf(strings[0]);
        } catch (Exception e) {
            return null;
        }
    }

    private String getNome(String[] strings) {
        try {
            return strings[1];
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getGroupCount() {
        return objetos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Util.UM;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return getObjeto(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return getObjeto(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        Dia obj = getObjeto(groupPosition);
        return obj.ehNovo() ? groupPosition : obj.get_id();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getGroupId(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Dia obj = getObjeto(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dia_item_grupo_layout, null);
            convertView.setTag(new ViewHolderGrupo(convertView));
        }

        ViewHolderGrupo holder = (ViewHolderGrupo) convertView.getTag();
        holder.atualizarView(obj);
        convertView.setBackground(Util.getBackground(obj, context));

        return convertView;
    }

    private class ViewHolderGrupo {
        TextView txtManhaIni;
        TextView txtManhaFim;
        TextView txtTardeIni;
        TextView txtTardeFim;
        TextView txtNumero;
        TextView txtNome;

        ViewHolderGrupo(View view) {
            txtManhaIni = (TextView) view.findViewById(R.id.txtManhaIni);
            txtManhaFim = (TextView) view.findViewById(R.id.txtManhaFim);
            txtTardeIni = (TextView) view.findViewById(R.id.txtTardeIni);
            txtTardeFim = (TextView) view.findViewById(R.id.txtTardeFim);
            txtNumero = (TextView) view.findViewById(R.id.txtNumero);
            txtNome = (TextView) view.findViewById(R.id.txtNome);
        }

        void atualizarView(Dia obj) {
            txtNumero.setText(Util.get00(obj.getNumero().toString()));
            txtManhaIni.setText(obj.getManhaIniFmt());
            txtManhaFim.setText(obj.getManhaFimFmt());
            txtTardeIni.setText(obj.getTardeIniFmt());
            txtTardeFim.setText(obj.getTardeFimFmt());
            txtNome.setText(obj.getNome());
            visivel(txtManhaIni);
            visivel(txtManhaFim);
            visivel(txtTardeIni);
            visivel(txtTardeFim);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Dia obj = getObjeto(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dia_item_detalhe_layout, null);
            convertView.setTag(new ViewHolderDetalhe(convertView));
        }

        ViewHolderDetalhe holder = (ViewHolderDetalhe) convertView.getTag();
        holder.atualizarView(obj);

        return convertView;
    }

    private class ViewHolderDetalhe {
        LinearLayout lltTotalLei;
        LinearLayout lltManha;
        LinearLayout lltTarde;
        LinearLayout lltNoite;
        LinearLayout lltTotal;
        LinearLayout lltDebit;
        LinearLayout lltCredi;
        LinearLayout lltObser;
        LinearLayout lltClick;

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
        TextView txtTotalLei;

        ViewHolderDetalhe(View view) {
            lltTotalLei = (LinearLayout) view.findViewById(R.id.lltTotalLei);
            lltCredi = (LinearLayout) view.findViewById(R.id.lltCredito);
            lltDebit = (LinearLayout) view.findViewById(R.id.lltDebito);
            lltManha = (LinearLayout) view.findViewById(R.id.lltManha);
            lltTarde = (LinearLayout) view.findViewById(R.id.lltTarde);
            lltNoite = (LinearLayout) view.findViewById(R.id.lltNoite);
            lltTotal = (LinearLayout) view.findViewById(R.id.lltTotal);
            lltClick = (LinearLayout) view.findViewById(R.id.lltClick);
            lltObser = (LinearLayout) view.findViewById(R.id.lltObs);

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
            txtTotalLei = (TextView) view.findViewById(R.id.txtTotalLei);
        }

        void atualizarView(Dia obj) {
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
            txtTotalLei.setText(obj.getTotalLeiFmt());

            txtTotalLei.setTextColor(obj.isValido() ? Color.BLUE : Color.BLACK);
            txtTotal.setTextColor(obj.isValido() ? Color.BLUE : Color.BLACK);

            if (obj.isValido()) {
                txtDebito.setTextColor(Util.ZERO_ZERO.equals(obj.getDebito()) ? Color.BLACK : Color.RED);
                txtCredito.setTextColor(Util.ZERO_ZERO.equals(obj.getCredito()) ? Color.BLACK : Color.MAGENTA);
            } else {
                txtDebito.setTextColor(Color.BLACK);
                txtCredito.setTextColor(Color.BLACK);
            }

            AtomicInteger integer = new AtomicInteger(0);

            visivel(txtManhaIni, txtManhaFim, txtManhaCal, lltManha, integer);
            visivel(txtTardeIni, txtTardeFim, txtTardeCal, lltTarde, integer);
            visivel(txtNoiteIni, txtNoiteFim, txtNoiteCal, lltNoite, integer);
            visivel(txtTotal, lltTotal, integer);
            visivelObs(txtObs, lltObser, integer);
            visivel(txtDebito, lltDebit, integer);
            visivel(txtCredito, lltCredi, integer);
            visivel(txtTotalLei, lltTotalLei, integer);

            lltClick.setVisibility(integer.intValue() > 0 ? View.GONE : View.VISIBLE);
        }
    }

    private void visivel(TextView view1, TextView view2, TextView view3, LinearLayout llt, AtomicInteger integer) {
        int total = 0;

        view1.setVisibility(Util.ZERO_ZERO.equals(view1.getText()) ? View.GONE : View.VISIBLE);
        if (view1.getVisibility() == View.GONE) {
            total++;
        }

        view2.setVisibility(Util.ZERO_ZERO.equals(view2.getText()) ? View.GONE : View.VISIBLE);
        if (view2.getVisibility() == View.GONE) {
            total++;
        }

        view3.setVisibility(Util.ZERO_ZERO.equals(view3.getText()) ? View.GONE : View.VISIBLE);
        if (view3.getVisibility() == View.GONE) {
            total++;
        }

        llt.setVisibility(total == 3 ? View.GONE : View.VISIBLE);
        if (llt.getVisibility() == View.VISIBLE) {
            integer.incrementAndGet();
        }
    }

    private void visivel(TextView view, LinearLayout llt, AtomicInteger integer) {
        int visibilidade = Util.ZERO_ZERO.equals(view.getText()) ? View.GONE : View.VISIBLE;
        view.setVisibility(visibilidade);
        llt.setVisibility(visibilidade);
        if (llt.getVisibility() == View.VISIBLE) {
            integer.incrementAndGet();
        }
    }

    private void visivel(TextView view) {
        int visibilidade = Util.ZERO_ZERO.equals(view.getText()) ? View.GONE : View.VISIBLE;
        view.setVisibility(visibilidade);
    }

    private void visivelObs(TextView view, LinearLayout llt, AtomicInteger integer) {
        int visibilidade = Util.isVazio(view.getText()) ? View.GONE : View.VISIBLE;
        view.setVisibility(visibilidade);
        llt.setVisibility(visibilidade);
        if (llt.getVisibility() == View.VISIBLE) {
            integer.incrementAndGet();
        }
    }
}