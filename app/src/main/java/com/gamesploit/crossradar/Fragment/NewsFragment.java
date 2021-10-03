package com.gamesploit.crossradar.Fragment;

import static com.gamesploit.crossradar.GccConfig.urlref.TAG_ERROR;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gamesploit.crossradar.Adapter.ItemAdapter;
import com.gamesploit.crossradar.Adapter.NewsAdapter;
import com.gamesploit.crossradar.GccConfig.urlref;
import com.gamesploit.crossradar.Helper;
import com.gamesploit.crossradar.JSONParserString;
import com.gamesploit.crossradar.LoginActivity;
import com.gamesploit.crossradar.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewsFragment extends Fragment {


    public NewsFragment() {
        // Required empty public constructor
    }

    private ArrayList<HashMap<String, String>> offersListNews;
    String title, descrion;
    RecyclerView recyclerView;
    Handler handler = new Handler();
    JSONArray jsonArray = new JSONArray();
    private boolean error;
    public ProgressDialog progressBar;
    JSONParserString jsonParser = new JSONParserString();
    private List<NewsAdapter> itemList = new ArrayList<>();
    private ItemAdapter itemAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = new ProgressDialog(getActivity());


        // Hashmap for ListView
        offersListNews = new ArrayList<>();

        new OneLoadAllProducts().execute();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        itemList = new ArrayList();
        itemAdapter = new ItemAdapter(getActivity(), itemList);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        View rootViewone = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = rootViewone.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //     initData();
        recyclerView.setAdapter(itemAdapter);

        return rootViewone;
    }


    class OneLoadAllProducts extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setMessage("Loading Latest News");
            progressBar.setCancelable(false);
            progressBar.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            JSONObject params = new JSONObject();
            String rq = null;
            try {
                params.put("", "");

                rq = jsonParser.makeHttpRequest(urlref.news, params);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return rq;
        }

        protected void onPostExecute(String s) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.dismiss();
                    try {
                        JSONObject ack = new JSONObject(s);
                        // Log.d("test", String.valueOf(ack));
                        String decData = Helper.profileDecrypt(ack.get("Data").toString(), ack.get("Hash").toString());
                        if (!Helper.verify(decData, ack.get("Sign").toString(), JSONParserString.publickey)) {
                            Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            JSONObject obj = new JSONObject(decData);
                        //      Log.d("test",obj.toString());
                            jsonArray = obj.getJSONArray("newslist");
                            error = obj.getBoolean(TAG_ERROR);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<>();
                                map.put("title", c.getString("title"));
                                map.put("description", c.getString("description"));
                                offersListNews.add(map);
                            }
                            if (!error) {
                                for (int i = 0; i < offersListNews.size(); i++) {
                                    title = offersListNews.get(i).get("title");
                                    descrion = offersListNews.get(i).get("description");
                                    //  Log.d("title", title);
                                    NewsAdapter newsAdapter = new NewsAdapter();
                                    newsAdapter.setTitle(title);
                                    newsAdapter.setDescription(descrion);
                                    itemList.add(newsAdapter);
                                    itemAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 3000);


        }
    }
}