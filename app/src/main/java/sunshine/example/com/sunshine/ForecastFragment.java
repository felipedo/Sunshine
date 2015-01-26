package sunshine.example.com.sunshine;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;

/**
 * Created by Felipe on 18/01/2015.
 */
public class ForecastFragment extends Fragment {

    public ArrayAdapter<String> adaptador;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater Inflater){
        Inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            FetchWeatherTask task = new FetchWeatherTask("94043");
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                task.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String[] lista = {"Today - Sunny - 88/63",
                "Tomorrow - Cloudy - 70/60",
                "Monday - Rainy - 67/55",
                "Thuesday - Sunny - 91/74",
                "Wednesday - Rainy - 88/63",
                "Thursday - Sunny - 45/30",
                "Friday - Cloudy - 89/45",
                "Saturday - Cloudy - 74/55",
                "Sunday - Sunny - 89/85",
                "Today - Sunny - 88/63",
                "Tomorrow - Cloudy - 70/60",
                "Monday - Rainy - 67/55",
                "Thuesday - Sunny - 91/74",
                "Wednesday - Rainy - 88/63",
                "Thursday - Sunny - 45/30",
                "Friday - Cloudy - 89/45",
                "Saturday - Cloudy - 74/55",
                "Sunday - Sunny - 89/85"
        };

        adaptador = new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,lista);
        ListView listview = (ListView) rootView.findViewById(R.id.listview_forecast);
        listview.setAdapter(adaptador);

        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<Void, Void, String[]> {

        private String PostalCode;
        private String JSONString = "";
        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        public FetchWeatherTask(String PC){
            this.PostalCode = PC;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("Procuraissoaqui", "Entrou");
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            try {
                //adaptador = new ArrayAdapter<String>(getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,strings);
//            for(int i =0;i<strings.length;i++){
//                adaptador.insert(strings[i],0);
//            }
                adaptador.clear();
                int currentAPI = android.os.Build.VERSION.SDK_INT;
                if (currentAPI  >= Build.VERSION_CODES.HONEYCOMB){
                    // More optimal. Only refresh Adapter once.
                    adaptador.addAll(strings);
                }
                else
                {
                    // Less optimal. Refresh Adapter on each addition.
                    for (String dayForecastStr : strings)
                    {
                        adaptador.add(dayForecastStr);
                    }
                }
            }catch(Exception e){
                Log.e("Procuraissoaqui",e.getMessage());
            }


        }

        @Override
        protected String[] doInBackground(Void... params){
            try {
                GetAPI api = new GetAPI();
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.openweathermap.org")
                        .appendPath("data")
                        .appendPath("2.5")
                        .appendPath("forecast")
                        .appendPath("daily")
                        .appendQueryParameter("q",this.PostalCode)
                        .appendQueryParameter("mode","json")
                        .appendQueryParameter("units","metric")
                        .appendQueryParameter("cnt","7");
                //"http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7"
                this.JSONString = api.getJSON(builder.build().toString());
            }catch(Exception e){
                this.JSONString = null;
            }
            String[] Dias = new String[7];
            try {
                WeatherDataParser WDP = new WeatherDataParser();
                Dias = WDP.getWeatherDataFromJson(JSONString, 7);
            }catch(Exception e){
                return null;
            }
//            Log.e("Procuraissoaqui", Dias[0]);
//            Log.e("Procuraissoaqui", Dias[1]);
//            Log.e("Procuraissoaqui", Dias[2]);
//            Log.e("Procuraissoaqui", Dias[3]);
//            Log.e("Procuraissoaqui", Dias[4]);
//            Log.e("Procuraissoaqui", Dias[5]);
//            Log.e("Procuraissoaqui", Dias[6]);

            return Dias;
        }

    }
}
