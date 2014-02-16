package com.bitaurant.activity;
import com.bitaurant.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class SelectTypeActivity extends BaseActivity{

	Button restaurantButton;
	Button customerButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_type);
		
		restaurantButton = (Button) findViewById(R.id.restaurant_button);
		customerButton = (Button) findViewById(R.id.customer_button);
		
		restaurantButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SelectTypeActivity.this, ActiveListActivity.class);
				startActivity(intent);
				
			}
		});
		
		customerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SelectTypeActivity.this, CustomerHomeActivity.class);
				startActivity(intent);
			}
		});
	}
}
