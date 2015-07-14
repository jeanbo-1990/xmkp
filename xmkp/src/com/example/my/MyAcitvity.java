package com.example.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bespeak.LoginActivity;
import com.example.bespeak.R;

public class MyAcitvity extends Activity {
	private long exitTime = 0;

	/**
	 * 重新登录
	 */
	LinearLayout ll_relogin;
	
	/**
	 * 订单查询
	 */
	LinearLayout ll_orderview;
	
	/**
	 * 订单查询
	 */
	LinearLayout ll_member;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		InitControl();
	}

	private void InitControl() {
		ll_relogin=(LinearLayout)findViewById(R.id.ll_relogin);
		ll_relogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentLogin = new Intent(MyAcitvity.this,
						LoginActivity.class);
				startActivity(intentLogin);
			}
		});
		
		ll_orderview=(LinearLayout)findViewById(R.id.ll_orderview);
		ll_orderview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentOrderView = new Intent(MyAcitvity.this,
						OrderViewActivity.class);
				startActivity(intentOrderView);
			}
		});
		ll_member=(LinearLayout)findViewById(R.id.ll_member);
		ll_member.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentMember = new Intent(MyAcitvity.this,
						MemberActivity.class);
				startActivity(intentMember);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
