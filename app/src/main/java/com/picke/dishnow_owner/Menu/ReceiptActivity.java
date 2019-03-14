package com.picke.dishnow_owner.Menu;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.kakao.usermgmt.response.model.User;
import com.picke.dishnow_owner.Owner_User.ReservationClass;
import com.picke.dishnow_owner.Owner_User.UserAuthClass;
import com.picke.dishnow_owner.R;
import com.picke.dishnow_owner.Utility.RecyclerAdapter_onwait;
import com.picke.dishnow_owner.Utility.RecyclerAdapter_receipt;
import com.picke.dishnow_owner.Utility.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReceiptActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private String feed_url = "http://dishnow.kr/ResHistoryShow.php";
    private RecyclerAdapter_receipt adapter;
    private UserAuthClass userAuthClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        Toolbar toolbar = findViewById(R.id.receipt_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestQueue = VolleySingleton.getmInstance(getApplicationContext()).getRequestQueue();
        userAuthClass = UserAuthClass.getInstance(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.receipt_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter_receipt();
        recyclerView.setAdapter(adapter);

        final StringRequest StringRequest= new StringRequest( Request.Method.POST, "http://dishnow.kr/ResHistoryShow.php", response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++) {
                    ReservationClass reservationClass = new ReservationClass();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    reservationClass.setUid(jsonObject.getString("user_id"));
                    reservationClass.setPeople(jsonObject.getString("res_people"));
                    reservationClass.setDate(jsonObject.getString("res_date"));
                    reservationClass.setTime(jsonObject.getString("res_time"));
                    adapter.addItem(reservationClass);
                }
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d( "meenseek", e.getLocalizedMessage() );
                e.printStackTrace();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "meenseek", error.getLocalizedMessage() );
            }
        } ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put( "m_id", userAuthClass.getUid());
                return params;
            }
        };

        requestQueue.add(StringRequest);
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
