package malcolm.busari.s991523264;
/**
 * Name: Malcolm Busari
 * Student No: 991523264
 * Section: 1211_34780
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DownloadFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadFrag extends Fragment {

    private static final String[] paths = {"Flower", "Nature", "Sky"};
    URL ImageUrl = null;
    InputStream is = null;
    Bitmap bmImg = null;
    ImageView imageView= null;
    ProgressDialog p;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DownloadFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DownloadFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static DownloadFrag newInstance(String param1, String param2) {
        DownloadFrag fragment = new DownloadFrag();
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
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        Spinner spinner = view.findViewById(R.id.malcolmPhotoSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, paths);
        spinner.setAdapter(adapter);
        imageView = view.findViewById(R.id.malcolmIV);
        Button downloadBtn = view.findViewById(R.id.malcolmDownloadBtn);
        downloadBtn.setOnClickListener(v -> spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int position, long id) {

                int pos = parent.getSelectedItemPosition();
                if (pos == 0) {
                    AsyncTaskExample asyncTask=new AsyncTaskExample();
                    asyncTask.execute("https://upload.wikimedia.org/wikipedia/commons/5/5a/Flower_11.jpg");
                }
                else if(pos == 1){
                    AsyncTaskExample asyncTask=new AsyncTaskExample();
                    asyncTask.execute("https://upload.wikimedia.org/wikipedia/commons/b/b3/Nature_.jpg");
                }
                else if(pos == 2){
                    AsyncTaskExample asyncTask=new AsyncTaskExample();
                    asyncTask.execute("https://upload.wikimedia.org/wikipedia/commons/d/d7/Sky.jpg");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        }));

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext())
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.prompt)
                        .setIcon(R.drawable.alerticon)
                        .setCancelable(true);
                builder.setPositiveButton(
                        R.string.yes,
                        (dialog, id) -> {
                            getActivity().finish();
                            System.exit(0);
                        });
                builder.setNegativeButton(
                        R.string.no,
                        (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return view;
    }

    private class AsyncTaskExample extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p=new ProgressDialog(getContext());
            p.setMessage("Please wait...It is downloading");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {

                ImageUrl = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection) ImageUrl
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bmImg = BitmapFactory.decodeStream(is, null, options);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return bmImg;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(imageView!=null) {
                p.hide();
                imageView.setImageBitmap(bitmap);
            }else {
                p.show();
            }
        }
    }
}