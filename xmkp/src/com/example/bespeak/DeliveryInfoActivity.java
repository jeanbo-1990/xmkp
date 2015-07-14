package com.example.bespeak;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bespeak.common.FunctionConst;
import com.example.bespeak.common.JsonUtil;
import com.example.bespeak.common.ProjectApplication;
import com.example.bespeak.model.t_HydaInfo;
import com.example.bespeak.model.t_HydzInfo;

public class DeliveryInfoActivity extends Activity {

	/**
	 * ȫ����
	 */
	ProjectApplication application;

	/**
	 * �ȴ���
	 */
	Dialog dialogWait;

	/**
	 * ��ʾ��
	 */
	Toast toast;

	/**
	 * �˿�����
	 */
	EditText edit_customName;

	/**
	 * ����ѡ���
	 */
	ImageView check_delivery_men;

	/**
	 * Ůʿѡ���
	 */
	ImageView check_delivery_women;

	/**
	 * ��ϵ�绰
	 */
	TextView txt_delivery_info_phone;

	/**
	 * �Ͳ͵�ַ��Ӱ�ť
	 */
	ImageView btn_delivery_address_add;

	/**
	 * �Ͳ͵�ַѡ��
	 */
	LinearLayout ll_delivery_selectAddress;

	/**
	 * �Ͳ͵�ַ
	 */
	EditText edit_delivery_address;

	/**
	 * �Ͳ�ʱ��ϵͳĬ��ѡ��
	 */
	LinearLayout ll_time_default;

	/**
	 * �Ͳ�ʱ���Զ���ѡ��
	 */
	LinearLayout ll_time_custom;

	/**
	 * ����
	 */
	ImageView btn_delivery_form_back;

	/**
	 * ����˵�
	 */
	ImageView btn_delivery_form_go;

	/**
	 * ��ϵ���
	 */
	ListView lvAssignXQ;
	AssignAdapter assignAdapter;// ��������������
	AlertDialog.Builder buildAssign;
	AlertDialog assignDialog;// ���봰��(AlertDialog)
	View view;

	t_HydzInfo currHydz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivery_info);
		application = (ProjectApplication) getApplication();
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);// ʵ������ʾ��
		InitDialogWait();
		InitControl();// ��ʼ������ؼ�
		SetData();
		if (application.hyda.hydzList != null) {
			InitAssignDialog();
		}
	}

	/**
	 * ��ʼ�����봰��
	 */
	private void InitAssignDialog() {
		// ��ʼ��listview
		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.dialog_select_address, null);
		lvAssignXQ = (ListView) view.findViewById(R.id.lv_select_address);// ������ƷlistView
		ImageView btn_select_address_ok = (ImageView) view
				.findViewById(R.id.btn_select_address_ok);
		btn_select_address_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (t_HydzInfo hydz : application.hyda.hydzList) {
					if (hydz.IsSelect) {
						currHydz = hydz;
						break;
					}
				}
				if(currHydz==null){
					toast.setText("��ѡ��һ����ַ");
					toast.show();
					return;
				}
				application.hyda.c_Xxdz = currHydz.c_Xxdz;
				application.hyda.c_xxdzid = currHydz.c_DzID;
				application.hyda.c_XQID = currHydz.c_XQID;

				// ���渳ֵ
				edit_delivery_address.setText(currHydz.c_Xq.substring(0, 4)
						+ "**"
						+ currHydz.c_Xxdz.substring(
								currHydz.c_Xxdz.length() - 3,
								currHydz.c_Xxdz.length()));
				assignDialog.dismiss();
			}
		});
		ImageView btn_select_address_cancle = (ImageView) view
				.findViewById(R.id.btn_select_address_cancle);
		btn_select_address_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				assignDialog.dismiss();
			}
		});
		assignAdapter = new AssignAdapter();
		lvAssignXQ.setAdapter(assignAdapter);
		lvAssignXQ.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				for (t_HydzInfo hydz : application.hyda.hydzList) {
					hydz.IsSelect = false;
				}
				application.hyda.hydzList.get(arg2).IsSelect = true;
				assignAdapter.Refresh();
			}
		});
		buildAssign = new AlertDialog.Builder(DeliveryInfoActivity.this);
		
		assignDialog = buildAssign.create();
	}

	/**
	 * ��Ա��ַ��������������
	 * 
	 * @author wjb
	 * 
	 */
	class AssignAdapter extends BaseAdapter {

		AssignView assignView;
		public void Refresh() {
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return application.hyda.hydzList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return application.hyda.hydzList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int postion, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if (view == null) {
				assignView = new AssignView();
				view = LayoutInflater.from(DeliveryInfoActivity.this).inflate(
						R.layout.dialog_select_address_item, null);
				assignView.btn_select_address_clear = (ImageView) view
						.findViewById(R.id.btn_select_address_clear);
				assignView.txt_select_address_name = (TextView) view
						.findViewById(R.id.txt_select_address_name);
				assignView.btn_select_address_check = (ImageView) view
						.findViewById(R.id.btn_select_address_check);

				view.setTag(assignView);
			} else {
				assignView = (AssignView) view.getTag();
			}

			if (application.hyda.hydzList.get(postion).IsSelect) {
				assignView.btn_select_address_check
						.setBackgroundDrawable(DeliveryInfoActivity.this
								.getResources().getDrawable(
										R.drawable.redio_checked));
			} else {
				assignView.btn_select_address_check
						.setBackgroundDrawable(DeliveryInfoActivity.this
								.getResources().getDrawable(
										R.drawable.redio_unchecked));
			}
			
			assignView.btn_select_address_clear.setOnClickListener(new listView_btn_click(postion));

			// /��ֵ
			assignView.txt_select_address_name
					.setText(application.hyda.hydzList.get(postion).c_Xq
							+ "**"
							+ application.hyda.hydzList.get(postion).c_Xxdz
									.substring(application.hyda.hydzList
											.get(postion).c_Xxdz.length() - 3,
											application.hyda.hydzList
													.get(postion).c_Xxdz
													.length()));
			return view;
		}
		
		class listView_btn_click implements OnClickListener {
			
			/**
			 * ����
			 */
			private int postion;

			public listView_btn_click(int postion) {
				this.postion = postion;
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int vId = v.getId();

				// ɾ��
				if (vId == assignView.btn_select_address_clear.getId()) {
					if(application.hyda.hydzList.size()>1)
					{
					application.hyda.hydzList.remove(postion);
					notifyDataSetChanged();
					}
					else{
						toast.setText("���ٱ���һ����ַ");
						toast.show();
					}
				}
			}
		}
	}
	
	

	class AssignView {
		ImageView btn_select_address_clear;
		TextView txt_select_address_name;
		ImageView btn_select_address_check;
	}

	/**
	 * ��������
	 */
	private void SetData() {
		edit_customName.setText(application.hyda.c_Hymc);
		if (application.hyda.c_Xb == 1) {
			check_delivery_women.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.redio_checked));
			check_delivery_men.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.redio_unchecked));
		} else {
			check_delivery_men.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.redio_checked));
			check_delivery_women.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.redio_unchecked));
		}
		txt_delivery_info_phone.setText(application.hyda.c_Yddh);
		if (application.hyda.c_Hymc.length() != 0) {
			edit_delivery_address.setText(application.hyda.c_Xxdz.trim()
					.length() == 0 ? "" : application.hyda.c_XQID_Name
					.substring(0, 3)
					+ "**"
					+ application.hyda.c_Xxdz.trim().substring(
							application.hyda.c_Xxdz.length() - 4,
							application.hyda.c_Xxdz.length()));
		}
	}

	/**
	 * ��ʼ������ؼ�
	 */
	private void InitControl() {
		edit_customName = (EditText) findViewById(R.id.edit_customName);
		check_delivery_men = (ImageView) findViewById(R.id.check_delivery_men);
		/**
		 * ����ѡ��
		 */
		check_delivery_men.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				application.hyda.c_Xb = 0;
				check_delivery_men.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.redio_checked));
				check_delivery_women.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.redio_unchecked));
			}
		});
		check_delivery_women = (ImageView) findViewById(R.id.check_delivery_women);
		check_delivery_women.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				application.hyda.c_Xb = 1;
				check_delivery_men.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.redio_unchecked));
				check_delivery_women.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.redio_checked));
			}
		});
		txt_delivery_info_phone = (TextView) findViewById(R.id.txt_delivery_info_phone);
		btn_delivery_address_add = (ImageView) findViewById(R.id.btn_delivery_address_add);
		btn_delivery_address_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentAddAddress = new Intent(DeliveryInfoActivity.this,
						AddDeliveryActivity.class);
				startActivityForResult(intentAddAddress, 1);
			}
		});
		// ѡ���Ͳ͵�ַ
		ll_delivery_selectAddress = (LinearLayout) findViewById(R.id.ll_delivery_selectAddress);
		ll_delivery_selectAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ����ѡ���ַ�Ի���
				if (application.hyda.hydzList != null
						&& application.hyda.hydzList.size() != 0) {
					for (t_HydzInfo hydz : application.hyda.hydzList) {
						if(hydz.c_Xxdz.equals(application.hyda.c_Xxdz)){
							hydz.IsSelect=true;
						}
						else{
							hydz.IsSelect=false;
						}
					}
					assignDialog.show();
					Window window = assignDialog.getWindow();
					window.setContentView(view);
				}
			}
		});
		edit_delivery_address = (EditText) findViewById(R.id.edit_delivery_address);
		ll_time_default = (LinearLayout) findViewById(R.id.ll_time_default);
		ll_time_custom = (LinearLayout) findViewById(R.id.ll_time_custom);
		btn_delivery_form_back = (ImageView) findViewById(R.id.btn_delivery_form_back);
		btn_delivery_form_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_delivery_form_go = (ImageView) findViewById(R.id.btn_delivery_form_go);
		btn_delivery_form_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edit_customName.getText() == null
						|| edit_customName.getText().toString().trim().length() == 0) {
					toast.setText("������˿�����");
					toast.show();
					edit_customName.findFocus();
					return;
				}
				if (edit_delivery_address.getText() == null
						|| edit_delivery_address.getText().toString().trim()
								.length() == 0) {
					toast.setText("�������Ͳ͵�ַ");
					toast.show();
					return;
				}

				application.hyda.c_Hymc = edit_customName.getText().toString()
						.trim();
				dialogWait.show();
				// ���»�Ա��Ϣ
				new Thread() {
					public void run() {
						UpdateHyda();
					};
				}.start();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (application.hyda != null) {
			edit_delivery_address.setText(application.hyda.c_Xxdz.trim()
					.length() == 0 ? "" : application.hyda.c_XQID_Name
					.substring(0, 3)
					+ "**"
					+ application.hyda.c_Xxdz.trim().substring(
							application.hyda.c_Xxdz.length() - 4,
							application.hyda.c_Xxdz.length()));
		}
		if (application.hyda.hydzList != null) {
			InitAssignDialog();
		}
	}

	private void UpdateHyda() {
		Message message = new Message();
		message.what = 0;

		Object result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					FunctionConst.UpdateHyda);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			request.addProperty("strHyda", JsonUtil.toJson(application.hyda));
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					application.URL);
			androidHttpTransport
					.call("http://tempuri.org/UpdateHyda", envelope);
			result = (Object) envelope.getResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.obj = "����ʧ��";
			handlerUpdateData.sendMessage(message);
			return;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.obj = "����ʧ��";
			handlerUpdateData.sendMessage(message);
			return;
		}

		String strGoodsCategory = result.toString();
		if (strGoodsCategory.equals("0")) {
			message.obj = "����ʧ��";
			handlerUpdateData.sendMessage(message);
			return;
		}

		message.obj = "���³ɹ�";
		handlerUpdateData.sendMessage(message);
	}

	/**
	 * ��ѯ��Ա
	 * 
	 * @param login_no
	 */
//	private void GethydaData(String login_no) {
//		Message message = new Message();
//		message.what = 0;
//		// ��ȡ��Աjson
//		Object result = null;
//		try {
//			SoapObject request = new SoapObject("http://tempuri.org/",
//					FunctionConst.GethydaData);
//			request.addProperty("c_YddhOrc_Yj", login_no);
//			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//					SoapEnvelope.VER11);
//			envelope.dotNet = true;
//			envelope.setOutputSoapObject(request);
//			HttpTransportSE androidHttpTransport = new HttpTransportSE(
//					application.URL);
//			androidHttpTransport.call("http://tempuri.org/GethydaData",
//					envelope);
//			result = (Object) envelope.getResponse();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			message.obj = "��ȡʧ��";
//			handlerGetData.sendMessage(message);
//			return;
//		} catch (XmlPullParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			message.obj = "��ȡʧ��";
//			handlerGetData.sendMessage(message);
//			return;
//		}
//
//		if (result == null || result.toString().trim().length() == 0
//				|| result.toString().trim().equals("anyType{}")) {
//			application.hyda = new t_HydaInfo();
//			application.hyda.c_Hybm = application.hyda.c_Yddh = login_no;
//			application.hyda.IsNew = 1;
//		} else {
//			String strGoodsCategory = result.toString();
//			// ����ȡ����jsonת�ɶ���
//			application.hyda = JsonUtil.fromJson(strGoodsCategory,
//					t_HydaInfo.class);
//		}
//
//		message.obj = "��ȡ�ɹ�";
//		handlerGetData.sendMessage(message);
//	}

	/**
	 * ��ȡ��Աhandler
	 */
//	Handler handlerGetData = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 0:
//				String strResult = msg.obj.toString();
//				if (strResult.contains("��ȡ�ɹ�")) {
//					SetData();
//					if (application.hyda.hydzList != null) {
//						InitAssignDialog();
//					}
//				} else {
//					toast.setText("��Ա��¼ʧ�ܣ�");
//					toast.show();
//				}
//				dialogWait.dismiss();
//			}
//		};
//	};

	/**
	 * ���»�Աhandler
	 */
	Handler handlerUpdateData = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String strResult = msg.obj.toString();
				if (strResult.contains("���³ɹ�")) {
					application.hyda.isLogin = 1;
					LoginActivity.instance.finish();
					finish();
				} else {
					toast.setText("��Ա��Ϣ����ʧ�ܣ�");
					toast.show();
				}
				dialogWait.dismiss();
			}
		};
	};

	/**
	 * ��ʼ���ȴ���
	 */
	private void InitDialogWait() {
		// �ȴ���
		dialogWait = new Dialog(DeliveryInfoActivity.this, R.style.dialog);
		dialogWait.setCancelable(false);
		LayoutInflater inflaterWait = getLayoutInflater();
		View alertDialogViewWait = inflaterWait.inflate(
				R.layout.activity_dialog, null);
		dialogWait.setContentView(alertDialogViewWait);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delivery_info, menu);
		return true;
	}

}
