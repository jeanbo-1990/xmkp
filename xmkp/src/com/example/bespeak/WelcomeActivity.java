package com.example.bespeak;

import com.example.bespeak.common.ProjectApplication;
import com.example.bespeak.common.UpdateManager;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class WelcomeActivity extends Activity {
	
	private UpdateManager mUpdateManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		//���������汾�Ƿ���Ҫ����
        mUpdateManager = new UpdateManager(this,(ProjectApplication) getApplication());
        mUpdateManager.checkUpdateInfo();
	}
}
