package sunny.example.com.c_bake;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public ArrayList<Item> isItem;
    FloatingActionButton floatingActionButton;
    RecyclerView myItemRecycler;
    String ITEM_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myItemRecycler = findViewById(R.id.item_recycler);
        floatingActionButton = findViewById(R.id.fab);
        requestQueue = Volley.newRequestQueue(this);
        getRespies(requestQueue);
        //Picasso.with(this).load(R.drawable.food).into(floatingActionButton);
        myItemRecycler.setFocusable(true);
        myItemRecycler.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));
    }

    private void getRespies(RequestQueue requestQueue) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, ITEM_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                ArrayList<Item> items = new ArrayList<>();
                try {

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject itemObject = array.getJSONObject(i);
                        Gson gson = new GsonBuilder().create();
                        Item it = gson.fromJson(String.valueOf(itemObject), Item.class);
                        items.add(it);
                    }
                    isItem = items;
                    myItemRecycler.setAdapter(new RecyclerViewAdapter(MainActivity.this, isItem));
                    Toast.makeText(MainActivity.this, "Connected To Internet", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private int numberOfColumns() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2)
            return 2;
        return nColumns;
    }
}