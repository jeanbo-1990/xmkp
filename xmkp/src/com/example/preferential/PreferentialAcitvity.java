package com.example.preferential;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bespeak.R;
import com.example.bespeak.common.FunctionConst;
import com.example.bespeak.common.ImageLoader;
import com.example.bespeak.common.JsonUtil;
import com.example.bespeak.common.ProjectApplication;
import com.example.bespeak.model.t_SpdaInfo;
import com.example.car.CarAcitvity;
import com.google.gson.reflect.TypeToken;

public class PreferentialAcitvity extends Activity {
	private long exitTime = 0;

	/**
	 * 优惠图片listview
	 */
	ListView lv_per;

	/**
	 * 数据
	 */
	List<t_SpdaInfo> yhList;

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
	 * 图片加载
	 */
	ImageLoader imageLoader;

	AssignAdapter assignAdapter;// 联想数据适配器
	AlertDialog.Builder buildAssign;
	AlertDialog assignDialog;// 联想窗口(AlertDialog)

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
				if (!strResult.contains("获取失败")) {
					yhList = JsonUtil.fromJson(strResult,
							new TypeToken<List<t_SpdaInfo>>() {
							}.getType());
					assignAdapter.notifyDataSetChanged();
				} else {
					toast.setText("没有要查找的数据！");
					toast.show();
				}
				dialogWait.dismiss();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferential);
		lv_per = (ListView) findViewById(R.id.lv_per);
		SetListEmptyView();
		yhList = new ArrayList<t_SpdaInfo>();
		imageLoader = new ImageLoader(PreferentialAcitvity.this);
		application = (ProjectApplication) getApplication();
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);// 实例化提示框
		InitDialogWait();
		assignAdapter = new AssignAdapter();
		lv_per.setAdapter(assignAdapter);
		dialogWait.show();
		// 加载图片
		new Thread() {
			public void run() {
				LoadData();
			};
		}.start();
	}
	
	/**
	 * listView 数据为空的时的view
	 */
	private void SetListEmptyView() {
		TextView emptyView = new TextView(PreferentialAcitvity.this);
		emptyView.setTextColor(getResources().getColor(R.color.red));
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		emptyView.setText("没有数据");
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_per.getParent()).addView(emptyView);
		lv_per.setEmptyView(emptyView);
	}

	/**
	 * 初始化等待框
	 */
	private void InitDialogWait() {
		// 等待框
		dialogWait = new Dialog(PreferentialAcitvity.this, R.style.dialog);
		dialogWait.setCancelable(false);
		LayoutInflater inflaterWait = getLayoutInflater();
		View alertDialogViewWait = inflaterWait.inflate(
				R.layout.activity_dialog, null);
		dialogWait.setContentView(alertDialogViewWait);
	}

	/**
	 * 获取小区联想数据
	 */
	private void LoadData() {
		Message message = new Message();
		message.what = 0;
		// 获取会员json
		Object result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					"GetYHData");
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);

			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					application.URL);
			androidHttpTransport.call("http://tempuri.org/GetYHData", envelope);
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
			message.obj = result.toString();
			handlerGetData.sendMessage(message);
		}
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
			return yhList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return yhList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int postion, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			YHAssignView yhView;
			if (view == null) {
				yhView = new YHAssignView();
				view = LayoutInflater.from(PreferentialAcitvity.this).inflate(
						R.layout.preferential_list_item, null);
				yhView.img_perferential = (ImageView) view
						.findViewById(R.id.img_perferential);

				view.setTag(yhView);
			} else {
				yhView = (YHAssignView) view.getTag();
			}

			// /赋值
			imageLoader.DisplayImageAuto(yhList.get(postion).c_Xtp,
					yhView.img_perferential);
			return view;
		}
	}

	class YHAssignView {
		ImageView img_perferential;
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
