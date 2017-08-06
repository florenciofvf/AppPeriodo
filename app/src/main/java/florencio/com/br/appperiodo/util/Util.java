package florencio.com.br.appperiodo.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {
    public static DateFormat format_HH_mm = new SimpleDateFormat("HH:mm");
    public static DateFormat format_HH = new SimpleDateFormat("HH");
    public static DateFormat format_mm = new SimpleDateFormat("mm");

    public static String formatarHora(Long date) {
        if(date == null) {
            return "00:00";
        }

        return format_HH_mm.format(new Date(date));
    }

    public static Integer getHora(Long valor) {
        if(valor == null) {
            return 0;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(valor));

        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static Integer getMinuto(Long valor) {
        if(valor == null) {
            return 0;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(valor));

        return c.get(Calendar.MINUTE);
    }
}
