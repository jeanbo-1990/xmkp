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
	 * 全局类
	 */
	ProjectApplication application;

	/**
	 * 提示框
	 */
	Toast toast;

	/**
	 * 等待框
	 */
	Dialog dialogWait;

	/**
	 * 返回
	 */
	ImageView btn_add_delivery_form_back;

	/**
	 * 确定
	 */
	TextView btn_add_delivery_form_ok;

	/**
	 * 小区
	 */
	EditText edit_addDelivery_xq;

	/**
	 * 
	 */
	Button btn_addDelivery_search;

	/**
	 * 详细地址
	 */
	EditText edt_XXDZ;

	/**
	 * 联系表格
	 */
	ListView lvAssignXQ;
	AssignAdapter assignAdapter;// 联想数据适配器
	List<t_XqInfo> assignXQ = new ArrayList<t_XqInfo>();// 联想商品的数据
	AlertDialog.Builder buildAssign;
	AlertDialog assignDialog;// 联想窗口(AlertDialog)

	t_XqInfo currXQ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_delivery);
		application = (ProjectApplication) getApplication();
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);// 实例化提示框
		InitDialogWait();
		InitControl();
		InitAssignDialog();
	}

	/**
	 * 初始化等待框
	 */
	private void InitDialogWait() {
		// 等待框
		dialogWait = new Dialog(AddDeliveryActivity.this, R.style.dialog);
		dialogWait.setCancelable(false);
		LayoutInflater inflaterWait = getLayoutInflater();
		View alertDialogViewWait = inflaterWait.inflate(
				R.layout.activity_dialog, null);
		dialogWait.setContentView(alertDialogViewWait);
	}

	/**
	 * 控件初始化
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
					toast.setText("请填写路名/小区名");
					toast.show();
					return;
				}
				if (edt_XXDZ.getText() == null
						|| edt_XXDZ.getText().toString().trim().length() == 0) {
					toast.setText("请填写详细地址");
					toast.show();
					return;
				}
				
				if(edt_XXDZ.getText().toString().length()<4){
					toast.setText("详细地址不能低于4个字符");
					toast.show();
					return;
				}
				if (currXQ == null) {
					toast.setText("请查找正确的路名/小区名");
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
				// 通过调用setResult方法返回结果给前一个activity。
				AddDeliveryActivity.this.setResult(1, intent);
				// 关闭当前activity
				AddDeliveryActivity.this.finish();

			}
		});

		edit_addDelivery_xq = (EditText) findViewById(R.id.edit_addDelivery_xq);
		edt_XXDZ = (EditText) findViewById(R.id.edt_XXDZ);

		// 小区查找按钮
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
//					toast.setText("请输入关键字");
//					toast.show();
//				}
			}
		});
	}

	/**
	 * 获取小区联想数据
	 */
	private void GetXQData(String strXQName) {
		Message message = new Message();
		message.what = 0;
		// 获取会员json
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
			// 将获取到的json转成对象
			assignXQ = JsonUtil.fromJson(strXQ,
					new TypeToken<List<t_XqInfo>>() {
					}.getType());
		}

		message.obj = "获取成功";
		handlerGetData.sendMessage(message);
	}

	/**
	 * 小区联系handler
	 */
	Handler handlerGetData = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String strResult = msg.obj.toString();
				if (strResult.contains("获取成功")) {
					assignDialog.show();
				} else {
					toast.setText("没有要查找的数据！");
					toast.show();
				}
				dialogWait.dismiss();
			}
		};
	};

	/**
	 * 初始化联想窗口
	 */
	private void InitAssignDialog() {
		// 初始化listview
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog_assign_list, null);
		lvAssignXQ = (ListView) view.findViewById(R.id.lv_assignList);// 联想商品listView

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
		title.setText("请点击选择小区/路");
		title.setTextSize(20);
		//title.setBackgroundColor(getResources().getColor(R.color.red));
		buildAssign.setCustomTitle(title);

		buildAssign.setCancelable(false);
		buildAssign.setView(view);
		assignDialog = buildAssign.create();
	}

	/**
	 * 商品联想数据适配器
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

			// /赋值
			assignView.txt_itemName.setText(assignXQ.get(postion).c_Xq);
			return view;
		}
	}

	class AssignView {
		TextView txt_itemName;
		RadioButton rb_item_check;
	}
}