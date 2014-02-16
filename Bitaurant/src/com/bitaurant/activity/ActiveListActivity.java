package com.bitaurant.activity;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bitaurant.R;
import com.bitaurant.adapter.OpenTablesAdapter;
import com.bitaurant.models.OpenTable;
import com.google.gson.Gson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class ActiveListActivity extends BaseActivity{

	ListView activeListView;
	OpenTablesAdapter mAdapter;
	
	ArrayList<OpenTable> openTablesList = new ArrayList<OpenTable>();
	
	ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_active_list);
		
		activeListView = (ListView) findViewById(R.id.listView1);
		
		RequestQueue queue = Volley.newRequestQueue(this);
		final String url = "http://hardindd.com/stage/bitaurant/j/get-orders";
		 
		// prepare the Request
		StringRequest getRequest = new StringRequest(url, 
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						pd.dismiss();
						
						response = Html.fromHtml(response).toString();
						Gson gson = new Gson();
						OpenTable[] oList;
						oList = gson.fromJson(response, OpenTable[].class);
						
						openTablesList = new ArrayList(Arrays.asList(oList));
						openTablesList.add(new OpenTable("+ Add new order", ""));
						mAdapter = new OpenTablesAdapter(ActiveListActivity.this, openTablesList);
						activeListView.setAdapter(mAdapter);
					}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				pd.dismiss();
				Toast.makeText(ActiveListActivity.this, "Error Loading", Toast.LENGTH_SHORT).show();
			}
		});
		 
		// add it to the RequestQueue   
		pd = ProgressDialog.show(this, "", "Loading Open Tables...");
		queue.add(getRequest);
		
		activeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 != openTablesList.size()-1){
					Intent intent = new Intent(ActiveListActivity.this, QRCodeActivity.class);
					intent.putExtra("id", openTablesList.get(arg2).id);
					startActivity(intent);
				}else{
					Toast.makeText(ActiveListActivity.this, "Add order here.", Toast.LENGTH_LONG).show();
				}
			}
		});
		
	}
}
