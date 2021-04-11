package malcolm.busari.s991523264;
/**
 * Name: Malcolm Busari
 * Student No: 991523264
 * Section: 1211_34780
 */

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebServiceFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebServiceFrag extends Fragment {

    TextView textView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WebServiceFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WebServiceFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static WebServiceFrag newInstance(String param1, String param2) {
        WebServiceFrag fragment = new WebServiceFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_web_service, container, false);
        textView = view.findViewById(R.id.malcolmWebServiceTV2);
        EditText editText = view.findViewById(R.id.malcolmWebServiceET);
        editText.setError("Zip Code must not be more than 5 digits");
        Button button = view.findViewById(R.id.malcolmWebServiceBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeather(view);
            }
        });
        return view;
    }

    public void getWeather(View view)
    {
        EditText txtZip = view.findViewById(R.id.malcolmWebServiceET);
        String zipCode = txtZip.getText().toString();

        String url = "https://api.openweathermap.org/data/2.5/weather?";
        url+="zip="+zipCode;
        url+="&appid=b80f6f5167c80356df75e150f49b0952";
        Log.d("URL",url);
        new ReadJSONFeedTask().execute(url);
    }

    public String readJSONFeed(String address) {
        URL url = null;
        try {
            url = new URL(address);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        };
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream content = new BufferedInputStream(
                    urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return stringBuilder.toString();
    }

    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            return readJSONFeed(urls[0]);
        }
        protected void onPostExecute(String result) {
            try {
                JSONObject weatherJson = new JSONObject(result);
                JSONArray dataArray1= weatherJson.getJSONArray("weather");
                String strResults="Weather\n";
                for (int i = 0; i < dataArray1.length(); i++) {
                    JSONObject jsonObject = dataArray1.getJSONObject(i);
                    strResults +="id: "+jsonObject.getString("id");
                    strResults +="\nmain: "+jsonObject.getString("main");
                    strResults +="\ndescription: "+jsonObject.getString("description");
                }
                JSONObject dataObject= weatherJson.getJSONObject("main");
                strResults +="\ntemp: "+dataObject.getString("temp");
//                strResults +="\nlon: "+dataObject.getString("lon");
//                strResults +="\nlat: "+dataObject.getString("lat");
                strResults +="\nhumidity: "+dataObject.getString("humidity");
//                strResults +="\nname: "+dataObject.getString("name");
                textView.setText(strResults);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}