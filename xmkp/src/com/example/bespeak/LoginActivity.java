package com.example.bespeak;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.example.bespeak.common.FunctionConst;
import com.example.bespeak.common.JsonUtil;
import com.example.bespeak.common.ProjectApplication;
import com.example.bespeak.model.t_HydaInfo;
import com.example.bespeak.model.t_HydzInfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	public static LoginActivity instance;

	boolean isCheck = false;

	/**
	 * ��հ�ť
	 */
	ImageView img_clear;

	/**
	 * ��¼�˺ű༭��
	 */
	EditText edit_login_no;

	/**
	 * �´��Զ���¼
	 */
	ImageView iv_isautiologin;

	/**
	 * �´ε�¼����
	 */
	LinearLayout ll_isautiologin;

	/**
	 * ��¼��ť
	 */
	LinearLayout btn_login;

	/**
	 * ��ʾ��
	 */
	Toast toast;

	/**
	 * ����
	 */
	ImageView btn_login_form_back;

	/**
	 * ��һ��
	 */
	ImageView btn_login_form_next;

	/**
	 * ����
	 */
	EditText txt_login_password;

	/**
	 * �������
	 */
	ImageView iv_ps_clear;

	private SharedPreferences sp;

	/**
	 * �ȴ���
	 */
	Dialog dialogWait;

	/**
	 * ȫ����
	 */
	ProjectApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		instance = this;
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);// ʵ������ʾ��
		InitDialogWait();
		application = (ProjectApplication) getApplication();
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);

		edit_login_no = (EditText) findViewById(R.id.txt_login_no);
		edit_login_no.setText(sp.getString("login_no", "") == null ? "" : sp
				.getString("login_no", ""));

		edit_login_no.setInputType(InputType.TYPE_CLASS_PHONE);// ֻ�������ֻ�����

		edit_login_no.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (edit_login_no.getText() != null
						&& edit_login_no.getText().length() != 0) {
					img_clear.setVisibility(View.VISIBLE);
				} else {
					img_clear.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		txt_login_password = (EditText) findViewById(R.id.txt_login_password);
		txt_login_password.setText(sp.getBoolean("AUTO_ISCHECK", false)?sp.getString("login_ps", "") == null ? ""
				: sp.getString("login_ps", ""):"");
		txt_login_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (txt_login_password.getText() != null
						&& txt_login_password.getText().length() != 0) {
					iv_ps_clear.setVisibility(View.VISIBLE);
				} else {
					iv_ps_clear.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		img_clear = (ImageView) findViewById(R.id.iv_clear);
		img_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edit_login_no.setText("");
			}
		});
		iv_ps_clear = (ImageView) findViewById(R.id.iv_ps_clear);
		iv_ps_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txt_login_password.setText("");
			}
		});
		iv_isautiologin = (ImageView) findViewById(R.id.iv_isautiologin);
		ll_isautiologin = (LinearLayout) findViewById(R.id.ll_isautiologin);
		ll_isautiologin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isCheck = isCheck == true ? false : true;
				if (isCheck) {
					iv_isautiologin.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.login_nextlogin_bg_down));
				} else {
					iv_isautiologin.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.login_nextlogin_bg));
				}
			}
		});

		btn_login = (LinearLayout) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (edit_login_no.getText() != null
						&& edit_login_no.getText().toString().trim().length() != 0) {
					if(txt_login_password.getText() == null
							|| txt_login_password.getText().toString().trim().length() == 0){
						toast.setText("����������!");
						toast.show();
						return;
					}
					if(txt_login_password.getText().toString().trim().length()<6){
						toast.setText("��������6λ��6λ���ϵ�����!");
						toast.show();
						return;
					}
					Editor editor = sp.edit();
					editor.putString("login_no", edit_login_no.getText()
							.toString().trim());
					editor.putString("login_ps", txt_login_password.getText()
							.toString().trim());
					editor.commit();

					sp.edit().putBoolean("AUTO_ISCHECK", isCheck).commit();

					// Intent intentDeliveryInfo = new
					// Intent(LoginActivity.this,
					// DeliveryInfoActivity.class);
					// intentDeliveryInfo.putExtra("login_no", edit_login_no
					// .getText().toString().trim());
					// startActivity(intentDeliveryInfo);
					dialogWait.show();
					// ��ѯ��Ա
					new Thread() {
						public void run() {
							GethydaData(edit_login_no.getText().toString()
									.trim(),txt_login_password.getText().toString().trim());
						};
					}.start();
				} else {
					toast.setText("������Ҫ�ֻ�����!");
					toast.show();
				}

			}
		});
		btn_login_form_back = (ImageView) findViewById(R.id.btn_login_form_back);
		btn_login_form_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_login_form_next = (ImageView) findViewById(R.id.btn_login_form_next);
		btn_login_form_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_login.performClick();
			}
		});

		if (sp.getBoolean("AUTO_ISCHECK", false)) {
			iv_isautiologin.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.login_nextlogin_bg_down));
			isCheck=true;
			btn_login.performClick();
		} else {
			iv_isautiologin.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.login_nextlogin_bg));
			isCheck=false;
		}

	}

	/**
	 * ��ʼ���ȴ���
	 */
	private void InitDialogWait() {
		// �ȴ���
		dialogWait = new Dialog(LoginActivity.this, R.style.dialog);
		dialogWait.setCancelable(false);
		LayoutInflater inflaterWait = getLayoutInflater();
		View alertDialogViewWait = inflaterWait.inflate(
				R.layout.activity_dialog, null);
		dialogWait.setContentView(alertDialogViewWait);
	}

	/**
	 * ��ѯ��Ա
	 * 
	 * @param login_no
	 */
	private void GethydaData(String login_no,String login_ps) {
		Message message = new Message();
		message.what = 0;
		// ��ȡ��Աjson
		Object result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					FunctionConst.GethydaData);
			request.addProperty("c_YddhOrc_Yj", login_no);
			request.addProperty("c_Hymm", login_ps);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					application.URL);
			androidHttpTransport.call("http://tempuri.org/GethydaData",
					envelope);
			result = (Object) envelope.getResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.obj = "��ȡʧ��";
			handlerGetData.sendMessage(message);
			return;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.obj = "��ȡʧ��";
			handlerGetData.sendMessage(message);
			return;
		}

		if (result == null || result.toString().trim().length() == 0
				|| result.toString().trim().equals("anyType{}")) {
			application.hyda = new t_HydaInfo();
			application.hyda.c_Hybm = application.hyda.c_Yddh = login_no;
			application.hyda.IsNew = 1;
			application.hyda.c_hymm = txt_login_password.getText().toString().trim();
		}else if(result.toString().equals("0")){
			message.obj = "0";
			handlerGetData.sendMessage(message);
			return;
		}
		else {
			String strGoodsCategory = result.toString();
			// ����ȡ����jsonת�ɶ���
			application.hyda = JsonUtil.fromJson(strGoodsCategory,
					t_HydaInfo.class);
			
		}

		message.obj = "��ȡ�ɹ�";
		handlerGetData.sendMessage(message);
	}

	/**
	 * ��ȡ��Աhandler
	 */
	Handler handlerGetData = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				dialogWait.dismiss();
				String strResult = msg.obj.toString();
				if (strResult.contains("��ȡ�ɹ�")) {
					Intent intentDeliveryInfo = new Intent(LoginActivity.this,
							DeliveryInfoActivity.class);
					startActivity(intentDeliveryInfo);
				}else if(strResult.contains("0")){
					toast.setText("�������");
					toast.show();
				}
				else {
					toast.setText("��Ա��¼ʧ�ܣ�");
					toast.show();
				}
			}
		};
	};
}
