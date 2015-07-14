package com.example.bespeak;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AboutUsActivity extends Activity {
	
	/**
	 * ·µ»Ø
	 */
	ImageView btn_aboutus_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		btn_aboutus_back=(ImageView)findViewById(R.id.btn_aboutus_back);
		btn_aboutus_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AboutUsActivity.this.finish();
			}
		});
	}
}
