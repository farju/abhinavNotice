package com.example.thedarkknight.enoticeboard;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeNotices extends AppCompatActivity {

    private static final String NOTICE_URL = "http://192.168.43.189/MyApi/examnotices.php";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private String collegeName;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notices);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        type=getIntent().getStringExtra("type");
        collegeName = getIntent().getStringExtra("collegeName");
        type=getIntent().getStringExtra("type");
        actionBar.setTitle(type+ "Notices");
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();
        loadAllNotices(collegeName,type);
    }
    private void loadAllNotices(final String collegeName,final String type)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NOTICE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray notices = new JSONArray(response);
                    for (int i = 0; i < notices.length(); i++) {
                        JSONObject noticeObject = notices.getJSONObject(i);
                        int nid = noticeObject.getInt("Nid");
                        String name = noticeObject.getString("Name");
                        String type = noticeObject.getString("Type");
                        String desc = noticeObject.getString("Description");
                        ListItem listItem = new ListItem(name, desc, type,collegeName);
                        listItems.add(listItem);
                    }
                    adapter = new NoticeAdapter(listItems, TypeNotices.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TypeNotices.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("collegeName", collegeName);
                params.put("type",type);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(TypeNotices.this,NoticeList.class);
        intent.putExtra("CollegeName",collegeName);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
