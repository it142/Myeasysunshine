package finder.flight.gr.myeasysunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static finder.flight.gr.myeasysunshine.BuildConfig.AMADEUS_API_KEY;

/**
 * Created by katerina on 27/1/2017.
 */

public class DetailFragment extends Fragment {

    ArrayAdapter<String> adapter;

    public DetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_refresh) {
            FetchWeatherTask task = new FetchWeatherTask();
            task.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        String data = "Katerina";


        List<String> list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_textview,
                list);
        adapter.add(data);
        adapter.add(MainActivity.FROM.toString());

        ListView listView = (ListView) rootView.findViewById(R.id.list_item_forecast);
        listView.setAdapter(adapter);



        // ListView l = (ListView)rootView.findViewById(R.id.list);

        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection con = null;
            BufferedReader reader = null;
            String json = "";

            Log.e("Erros",MainActivity.FROM.toString());
            MainActivity.FROM="Skata";
            Log.e("Erros",MainActivity.FROM);


            try {
                final String baseUrl = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?";
                final String apiKeyParam = "api_key";
                final String originParam = "origin";
                final String destParam = "destination";
                final String departParam = "departure_date";
                final String returnParam = "return_date";
                final String norParam = "number_of_results";



                Uri builtUri = Uri.parse(baseUrl).buildUpon()
                        .appendQueryParameter(apiKeyParam, AMADEUS_API_KEY)
                        .appendQueryParameter(originParam, MainActivity.FROM)
                        .appendQueryParameter(destParam, "ATH")
                        .appendQueryParameter(departParam, "2017-01-27")
                        .appendQueryParameter(returnParam, "2017-01-29")
                        .appendQueryParameter(norParam, "1")
                        .build();

                URL url = new URL(builtUri.toString());

                Log.i("Erros", builtUri.toString());

                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                InputStream in = con.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (in == null) return null;

                reader = new BufferedReader(new InputStreamReader(in));

                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) return null;

                json = buffer.toString();
                Log.i("Erros", json);

            } catch (Exception e) {
                Log.e("Flight", e.toString());
            } finally {
                if (con != null) con.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("Flight", e.toString());
                    }
                }
            }


            return null;
            //return json;
        }


        protected void onPostExecute(String json) {
            if (!json.equals("")) {
                try {
                    adapter.add(json);
                } catch (Exception e) {
                    Log.e("Class", e.toString());
                }
            }
        }
    }




}
