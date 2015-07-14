package com.example.my;

import java.io.IOException;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.example.bespeak.AddDeliveryActivity;
import com.example.bespeak.R;
import com.example.bespeak.R.layout;
import com.example.bespeak.R.menu;
import com.example.bespeak.common.FunctionConst;
import com.example.bespeak.common.JsonUtil;
import com.example.bespeak.common.ProjectApplication;
import com.example.bespeak.model.t_HydaInfo;
import com.example.bespeak.model.t_XqInfo;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MemberActivity extends Activity {
	
	/**
	 * 会员编码
	 */
	TextView txt_member_hybm;
	
	/**
	 * 会员名称
	 */
	TextView txt_member_hymc;
	
	/**
	 * 电话号码
	 */
	TextView txt_member_phone;
	
	/**
	 * 账号余额
	 */
	TextView txt_member_zhye;
	
	/**
	 * 积分
	 */
	TextView txt_member_jf;
	
	/**
	 * 全局文件
	 */
	ProjectApplication application;
	
	/**
	 * 返回
	 */
	ImageView btn_member_back;
	
	/**
	 * 提示框
	 */
	Toast toast;

	/**
	 * 等待框
	 */
	Dialog dialogWait;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member);
		application=(ProjectApplication)this.getApplication();
		
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);// 实例化提示框
		InitDialogWait();
		dialogWait.show();
		new Thread(){
			public void run() {
				GetData();
			};
		}.start();
	}
	
	private void GetData(){
		Message message = new Message();
		message.what = 0;
		// 获取会员json
		Object result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					"ReLoadHY");
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			request.addProperty("c_hybm", application.hyda.c_Hybm);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					application.URL);
			androidHttpTransport.call("http://tempuri.org/ReLoadHY", envelope);
			result = (Object) envelope.getResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.obj = "获取失败";
			handlerGetData.sendMessage(message);
			return;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.obj = "获取失败";
			handlerGetData.sendMessage(message);
			return;
		}

		if (result == null || result.toString().trim().length() == 0
				|| result.toString().trim().equals("anyType{}")) {
			message.obj = "获取失败";
			handlerGetData.sendMessage(message);
			return;
		} else {
			String strXQ = result.toString();
			message.obj = strXQ;
			handlerGetData.sendMessage(message);
		}
	}
	
	/**
	 * 重查会员handler
	 */
	Handler handlerGetData = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String strResult = msg.obj.toString();
				if (!strResult.contains("获取失败")) {
					t_HydaInfo hy=JsonUtil.fromJson(strResult,
							t_HydaInfo.class);
					application.hyda.c_Hymc=hy.c_Hymc;
					application.hyda.c_Yddh=hy.c_Yddh;
					application.hyda.c_Hyye=hy.c_Hyye;
					application.hyda.c_Jf=hy.c_Jf;
					InitControl();
				} else {
					toast.setText("获取失败！");
					toast.show();
				}
				dialogWait.dismiss();
			}
		};
	};
	
	/**
	 * 初始化等待框
	 */
	private void InitDialogWait() {
		// 等待框
		dialogWait = new Dialog(MemberActivity.this, R.style.dialog);
		dialogWait.setCancelable(false);
		LayoutInflater inflaterWait = getLayoutInflater();
		View alertDialogViewWait = inflaterWait.inflate(
				R.layout.activity_dialog, null);
		dialogWait.setContentView(alertDialogViewWait);
	}
	
	private void InitControl(){
		txt_member_hybm=(TextView)findViewById(R.id.txt_member_hybm);
		txt_member_hybm.setText(application.hyda.c_Hybm);
		txt_member_hymc=(TextView)findViewById(R.id.txt_member_hymc);
		txt_member_hymc.setText(application.hyda.c_Hymc);
		txt_member_phone=(TextView)findViewById(R.id.txt_member_phone);
		txt_member_phone.setText(application.hyda.c_Yddh);
		txt_member_zhye=(TextView)findViewById(R.id.txt_member_zhye);
		txt_member_zhye.setText(Double.toString(application.hyda.c_Hyye));
		txt_member_jf=(TextView)findViewById(R.id.txt_member_jf);
		txt_member_jf.setText(Double.toString(application.hyda.c_Jf));
		
		btn_member_back=(ImageView)findViewById(R.id.btn_member_back);
		btn_member_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MemberActivity.this.finish();
			}
		});
	}
}
