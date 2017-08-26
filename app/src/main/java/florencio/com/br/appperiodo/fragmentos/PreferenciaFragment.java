package florencio.com.br.appperiodo.fragmentos;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import florencio.com.br.appperiodo.R;
import florencio.com.br.appperiodo.util.Util;

public class PreferenciaFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);

        EditTextPreference edit = (EditTextPreference) findPreference(getString(R.string.comprimento_horario));
        edit.setOnPreferenceChangeListener(this);
        atualizar(edit);

        edit = (EditTextPreference) findPreference(getString(R.string.excesso_hora_extra));
        edit.setOnPreferenceChangeListener(this);
        atualizar(edit);

        edit = (EditTextPreference) findPreference(getString(R.string.tolerancia_saida));
        edit.setOnPreferenceChangeListener(this);
        atualizar(edit);

        edit = (EditTextPreference) findPreference(getString(R.string.url_importacao));
        edit.setOnPreferenceChangeListener(this);
        atualizar(edit);
    }

    @Override
    public boolean onPreferenceChange(Preference pref, Object newValue) {
        pref.setSummary(newValue.toString());

        if (pref.getKey().equals(getString(R.string.comprimento_horario))) {
            Util.OITO_HORAS = Util.parseHora2(newValue.toString());
        }

        return true;
    }

    private void atualizar(Preference pref) {
        String newValue = Util.getStringPref(getActivity(), pref.getKey(), pref.getSummary().toString());
        onPreferenceChange(pref, newValue);
    }
}
