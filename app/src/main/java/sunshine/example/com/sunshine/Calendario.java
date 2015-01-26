package sunshine.example.com.sunshine;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Felipe on 17/01/2015.
 */
public class Calendario {
    Calendar c;
    SimpleDateFormat f;
    public Calendario(){
        c = Calendar.getInstance();
    }
    public String getMes(){
        f = new SimpleDateFormat("MMMM");
        return f.format(c.getTime());
    }
    public String getDia(){
        f = new SimpleDateFormat("DD");
        return f.format(c.getTime());
    }
}
