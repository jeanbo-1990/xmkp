package com.example.bespeak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.example.bespeak.common.FunctionConst;
import com.example.bespeak.common.JsonUtil;
import com.example.bespeak.common.ProjectApplication;
import com.example.bespeak.model.t_HydzInfo;
import com.example.bespeak.model.t_XqInfo;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddDeliveryActivity extends Activity {

	/**
	 * ȫ����
	 */
	ProjectApplication application;

	/**
	 * ��ʾ��
	 */
	Toast toast;

	/**
	 * �ȴ���
	 */
	Dialog dialogWait;

	/**
	 * ����
	 */
	ImageView btn_add_delivery_form_back;

	/**
	 * ȷ��
	 */
	TextView btn_add_delivery_form_ok;

	/**
	 * С��
	 */
	EditText edit_addDelivery_xq;

	/**
	 * 
	 */
	Button btn_addDelivery_search;

	/**
	 * ��ϸ��ַ
	 */
	EditText edt_XXDZ;

	/**
	 * ��ϵ���
	 */
	ListView lvAssignXQ;
	AssignAdapter assignAdapter;// ��������������
	List<t_XqInfo> assignXQ = new ArrayList<t_XqInfo>();// ������Ʒ������
	AlertDialog.Builder buildAssign;
	AlertDialog assignDialog;// ���봰��(AlertDialog)

	t_XqInfo currXQ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_delivery);
		application = (ProjectApplication) getApplication();
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);// ʵ������ʾ��
		InitDialogWait();
		InitControl();
		InitAssignDialog();
	}

	/**
	 * ��ʼ���ȴ���
	 */
	private void InitDialogWait() {
		// �ȴ���
		dialogWait = new Dialog(AddDeliveryActivity.this, R.style.dialog);
		dialogWait.setCancelable(false);
		LayoutInflater inflaterWait = getLayoutInflater();
		View alertDialogViewWait = inflaterWait.inflate(
				R.layout.activity_dialog, null);
		dialogWait.setContentView(alertDialogViewWait);
	}

	/**
	 * �ؼ���ʼ��
	 */
	private void InitControl() {
		btn_add_delivery_form_back = (ImageView) findViewById(R.id.btn_add_delivery_form_back);
		btn_add_delivery_form_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_add_delivery_form_ok = (TextView) findViewById(R.id.btn_add_delivery_form_ok);
		btn_add_delivery_form_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edit_addDelivery_xq.getText() == null
						|| edit_addDelivery_xq.getText().toString().trim()
								.length() == 0) {
					toast.setText("����д·��/С����");
					toast.show();
					return;
				}
				if (edt_XXDZ.getText() == null
						|| edt_XXDZ.getText().toString().trim().length() == 0) {
					toast.setText("����д��ϸ��ַ");
					toast.show();
					return;
				}
				
				if(edt_XXDZ.getText().toString().length()<4){
					toast.setText("��ϸ��ַ���ܵ���4���ַ�");
					toast.show();
					return;
				}
				if (currXQ == null) {
					toast.setText("�������ȷ��·��/С����");
					toast.show();
					return;
				}
				t_HydzInfo hydz = new t_HydzInfo();
				hydz.c_Hybm = application.hyda.c_Hybm;
				hydz.c_XQID = currXQ.c_ID;
				hydz.c_Xq = currXQ.c_Xq;
				hydz.c_Xxdz = edt_XXDZ.getText().toString().trim();
				if (application.hyda.hydzList == null) {
					application.hyda.hydzList = new ArrayList<t_HydzInfo>();
				}
				application.hyda.hydzList.add(hydz);
				application.hyda.c_XQID = hydz.c_XQID;
				application.hyda.c_Xxdz = hydz.c_Xxdz;
				application.hyda.c_XQID_Name = hydz.c_Xq;
				Intent intent = new Intent();
				intent.putExtra("1", "1");
				// ͨ������setResult�������ؽ����ǰһ��activity��
				AddDeliveryActivity.this.setResult(1, intent);
				// �رյ�ǰactivity
				AddDeliveryActivity.this.finish();

			}
		});

		edit_addDelivery_xq = (EditText) findViewById(R.id.edit_addDelivery_xq);
		edt_XXDZ = (EditText) findViewById(R.id.edt_XXDZ);

		// С�����Ұ�ť
		btn_addDelivery_search = (Button) findViewById(R.id.btn_addDelivery_search);
		btn_addDelivery_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if (edit_addDelivery_xq.getText() != null
//						&& edit_addDelivery_xq.getText().toString().trim()
//								.length() != 0) {
					dialogWait.show();
					new Thread() {
						public void run() {
							GetXQData(edit_addDelivery_xq.getText().toString()
									.trim());
						};
					}.start();
//				} else {
//					toast.setText("������ؼ���");
//					toast.show();
//				}
			}
		});
	}

	/**
	 * ��ȡС����������
	 */
	private void GetXQData(String strXQName) {
		Message message = new Message();
		message.what = 0;
		// ��ȡ��Աjson
		Object result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					FunctionConst.GetXQData);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			request.addProperty("strXQName", strXQName);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					application.URL);
			androidHttpTransport.call("http://tempuri.org/GetXQData", envelope);
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
			message.obj = "��ȡʧ��";
			handlerGetData.sendMessage(message);
			return;
		} else {
			String strXQ = result.toString();
			// ����ȡ����jsonת�ɶ���
			assignXQ = JsonUtil.fromJson(strXQ,
					new TypeToken<List<t_XqInfo>>() {
					}.getType());
		}

		message.obj = "��ȡ�ɹ�";
		handlerGetData.sendMessage(message);
	}

	/**
	 * С����ϵhandler
	 */
	Handler handlerGetData = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String strResult = msg.obj.toString();
				if (strResult.contains("��ȡ�ɹ�")) {
					assignDialog.show();
				} else {
					toast.setText("û��Ҫ���ҵ����ݣ�");
					toast.show();
				}
				dialogWait.dismiss();
			}
		};
	};

	/**
	 * ��ʼ�����봰��
	 */
	private void InitAssignDialog() {
		// ��ʼ��listview
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog_assign_list, null);
		lvAssignXQ = (ListView) view.findViewById(R.id.lv_assignList);// ������ƷlistView

		assignAdapter = new AssignAdapter();
		lvAssignXQ.setAdapter(assignAdapter);
		lvAssignXQ.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				AssignView view = (AssignView) arg1.getTag();
				view.rb_item_check.setChecked(true);
				currXQ = assignXQ.get(arg2);
				edit_addDelivery_xq.setText(currXQ.c_Xq);
				assignDialog.dismiss();
			}
		});
		buildAssign = new AlertDialog.Builder(AddDeliveryActivity.this);
		TextView title = new TextView(this);
		title.setText("����ѡ��С��/·");
		title.setTextSize(20);
		//title.setBackgroundColor(getResources().getColor(R.color.red));
		buildAssign.setCustomTitle(title);

		buildAssign.setCancelable(false);
		buildAssign.setView(view);
		assignDialog = buildAssign.create();
	}

	/**
	 * ��Ʒ��������������
	 * 
	 * @author wjb
	 * 
	 */
	class AssignAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return assignXQ.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return assignXQ.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int postion, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			AssignView assignView;
			if (view == null) {
				assignView = new AssignView();
				view = LayoutInflater.from(AddDeliveryActivity.this).inflate(
						R.layout.dialog_assign_list_item, null);
				assignView.txt_itemName = (TextView) view
						.findViewById(R.id.txt_itemName);
				assignView.rb_item_check = (RadioButton) view
						.findViewById(R.id.rb_item_check);

				view.setTag(assignView);
			} else {
				assignView = (AssignView) view.getTag();
			}

			// /��ֵ
			assignView.txt_itemName.setText(assignXQ.get(postion).c_Xq);
			return view;
		}
	}

	class AssignView {
		TextView txt_itemName;
		RadioButton rb_item_check;
	}
}