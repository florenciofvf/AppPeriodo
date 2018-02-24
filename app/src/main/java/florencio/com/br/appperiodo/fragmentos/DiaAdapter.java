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

class DiaAdapter extends BaseExpandableListAdapter {
    private final List<Dia> objetos;
    private final Context context;

    DiaAdapter(List<Dia> objetos, Context context) {
        this.objetos = objetos;
        this.context = context;
    }

    List<Dia> getObjetos() {
        return objetos;
    }

    Dia getObjeto(int posicao) {
        return objetos.get(posicao);
    }

    String gerarConteudoExportacao() {
        StringBuilder sb = new StringBuilder();

        for (Dia obj : objetos) {
            sb.append(obj.gerarConteudoEmail());
        }

        return sb.toString();
    }

    String gerarConteudoExportacaoDados() {
        StringBuilder sb = new StringBuilder();

        for (Dia obj : objetos) {
            sb.append(obj.gerarConteudoDados());
        }

        return sb.toString();
    }

    void importarConteudo(List<String> lista, long toleranciaSaida, long excessoExtra) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.dia_item_grupo_cabecalho_layout, null);
            convertView.setTag(new ViewHolderGrupo(convertView));
        }

        ViewHolderGrupo holder = (ViewHolderGrupo) convertView.getTag();
        holder.atualizarView(obj);
        convertView.setBackground(Util.getBackground(obj, context));

        return convertView;
    }

    private class ViewHolderGrupo {
        final TextView txtManhaIni;
        final TextView txtManhaFim;
        final TextView txtTardeIni;
        final TextView txtTardeFim;
        final TextView txtNumero;
        final TextView txtNome;
        final TextView txtSinc;

        ViewHolderGrupo(View view) {
            txtManhaIni = view.findViewById(R.id.txtManhaIni);
            txtManhaFim = view.findViewById(R.id.txtManhaFim);
            txtTardeIni = view.findViewById(R.id.txtTardeIni);
            txtTardeFim = view.findViewById(R.id.txtTardeFim);
            txtNumero = view.findViewById(R.id.txtNumero);
            txtNome = view.findViewById(R.id.txtNome);
            txtSinc = view.findViewById(R.id.txtSinc);
        }

        void atualizarView(Dia obj) {
            txtNumero.setText(Util.get00(obj.getNumero().toString()));
            txtManhaIni.setText(obj.getManhaIniFmt());
            txtManhaFim.setText(obj.getManhaFimFmt());
            txtTardeIni.setText(obj.getTardeIniFmt());
            txtTardeFim.setText(obj.getTardeFimFmt());
            txtNome.setText(obj.getNome());
            txtSinc.setBackground(obj.isSincronizado() ? Util.getBackground(1, context) : null);
            txtSinc.setVisibility(obj.isSincronizado() ? View.VISIBLE : View.GONE);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.dia_item_grupo_detalhe_layout, null);
            convertView.setTag(new ViewHolderDetalhe(convertView));
        }

        ViewHolderDetalhe holder = (ViewHolderDetalhe) convertView.getTag();
        holder.atualizarView(obj);

        return convertView;
    }

    private class ViewHolderDetalhe {
        final LinearLayout lltTotalLei;
        final LinearLayout lltManha;
        final LinearLayout lltTarde;
        final LinearLayout lltNoite;
        final LinearLayout lltTotal;
        final LinearLayout lltDebit;
        final LinearLayout lltCredi;
        final LinearLayout lltObser;
        final LinearLayout lltClick;

        final TextView txtObs;

        final TextView txtManhaIni;
        final TextView txtManhaFim;
        final TextView txtManhaCal;

        final TextView txtTardeIni;
        final TextView txtTardeFim;
        final TextView txtTardeCal;

        final TextView txtNoiteIni;
        final TextView txtNoiteFim;
        final TextView txtNoiteCal;

        final TextView txtTotal;
        final TextView txtDebito;
        final TextView txtCredito;
        final TextView txtTotalLei;

        ViewHolderDetalhe(View view) {
            lltTotalLei = view.findViewById(R.id.lltTotalLei);
            lltCredi = view.findViewById(R.id.lltCredito);
            lltDebit = view.findViewById(R.id.lltDebito);
            lltManha = view.findViewById(R.id.lltManha);
            lltTarde = view.findViewById(R.id.lltTarde);
            lltNoite = view.findViewById(R.id.lltNoite);
            lltTotal = view.findViewById(R.id.lltTotal);
            lltClick = view.findViewById(R.id.lltClick);
            lltObser = view.findViewById(R.id.lltObs);

            txtObs = view.findViewById(R.id.txtObs);

            txtManhaIni = view.findViewById(R.id.txtManhaIni);
            txtManhaFim = view.findViewById(R.id.txtManhaFim);
            txtManhaCal = view.findViewById(R.id.txtManhaCal);

            txtTardeIni = view.findViewById(R.id.txtTardeIni);
            txtTardeFim = view.findViewById(R.id.txtTardeFim);
            txtTardeCal = view.findViewById(R.id.txtTardeCal);

            txtNoiteIni = view.findViewById(R.id.txtNoiteIni);
            txtNoiteFim = view.findViewById(R.id.txtNoiteFim);
            txtNoiteCal = view.findViewById(R.id.txtNoiteCal);

            txtTotal = view.findViewById(R.id.txtTotal);
            txtDebito = view.findViewById(R.id.txtDebito);
            txtCredito = view.findViewById(R.id.txtCredito);
            txtTotalLei = view.findViewById(R.id.txtTotalLei);
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
            txtDebito.setText(obj.getDebitoFmt());
            txtCredito.setText(obj.getCreditoFmt());
            txtTotalLei.setText(obj.getTotalLeiFmt());

            txtTotalLei.setTextColor(obj.isValido() ? Color.BLUE : Color.BLACK);
            txtTotal.setTextColor(obj.isValido() ? Color.BLUE : Color.BLACK);

            if (obj.isValido()) {
                txtDebito.setTextColor(Util.ZERO_ZERO.equals(obj.getDebitoFmt()) ? Color.BLACK : Color.RED);
                txtCredito.setTextColor(Util.ZERO_ZERO.equals(obj.getCreditoFmt()) ? Color.BLACK : Color.MAGENTA);
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