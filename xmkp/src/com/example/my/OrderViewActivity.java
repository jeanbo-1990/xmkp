package com.example.my;

import java.io.IOException;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bespeak.DeliveryInfoActivity;
import com.example.bespeak.LoginActivity;
import com.example.bespeak.R;
import com.example.bespeak.common.FunctionConst;
import com.example.bespeak.common.JsonUtil;
import com.example.bespeak.common.ProjectApplication;
import com.example.bespeak.model.ddzb;
import com.google.gson.reflect.TypeToken;

public class OrderViewActivity extends Activity {

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
	 * ��������
	 */
	List<ddzb> mainList;

	DetailAdapter detailAdapter;

	ListView lv_orderview;

	/**
	 * ����
	 */
	ImageView btn_order_veiw_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);// ʵ������ʾ��
		setContentView(R.layout.activity_order_view);
		application = (ProjectApplication) getApplication();
		btn_order_veiw_back = (ImageView) findViewById(R.id.btn_order_veiw_back);
		btn_order_veiw_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		InitDialogWait();
		lv_orderview = (ListView) findViewById(R.id.lv_orderview);
		dialogWait.show();
		new Thread() {
			public void run() {
				LoadOrder();
			};
		}.start();
	}

	/**
	 * ��ʼ��listview��������
	 */
	private void InitListView() {
		detailAdapter = new DetailAdapter();
		lv_orderview.setAdapter(detailAdapter);
		lv_orderview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Intent intentOrderViewDetail = new Intent(
						OrderViewActivity.this, OrderViewDetailActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("order", mainList.get(arg2));
				intentOrderViewDetail.putExtras(mBundle);
				startActivity(intentOrderViewDetail);

			}
		});
	}

	/**
	 * ������ϸ����������
	 * 
	 * @author wjb
	 * 
	 */
	class DetailAdapter extends BaseAdapter {

		public void Refresh() {
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mainList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return mainList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int postion, View view, ViewGroup parent) {
			// TODO Auto-generated method stub
			DetailView assignView;
			if (view == null) {
				assignView = new DetailView();
				view = LayoutInflater.from(OrderViewActivity.this).inflate(
						R.layout.order_view_list_item, null);
				assignView.txt_orderbillnumber = (TextView) view
						.findViewById(R.id.txt_orderbillnumber);
				// assignView.txt_orderstate = (TextView) view
				// .findViewById(R.id.txt_orderstate);
				assignView.txt_orderdate = (TextView) view
						.findViewById(R.id.txt_orderdate);
				assignView.iv_order_state = (ImageView) view
						.findViewById(R.id.iv_order_state);

				view.setTag(assignView);
			} else {
				assignView = (DetailView) view.getTag();
			}

			// /��ֵ
			assignView.txt_orderbillnumber.setText(mainList.get(postion).c_Djh);
			// assignView.txt_orderstate.setText(mainList.get(postion).c_Djzt);
			assignView.txt_orderdate.setText(mainList.get(postion).c_Zdsj
					.substring(0, 2)
					+ ":"
					+ mainList.get(postion).c_Zdsj.substring(2, 4)
					+ ":"
					+ mainList.get(postion).c_Zdsj.substring(4));
			
			switch (mainList.get(postion).c_Djzt) {
			case 0:
				assignView.iv_order_state.setImageDrawable(getResources()
						.getDrawable(R.drawable.order_state_ok));
				break;
			case 1:
				assignView.iv_order_state.setImageDrawable(getResources()
						.getDrawable(R.drawable.order_state_driver));
				break;
			case 2:
				assignView.iv_order_state.setImageDrawable(getResources()
						.getDrawable(R.drawable.order_state_distribution));
				break;
			case 3:
				assignView.iv_order_state.setImageDrawable(getResources()
						.getDrawable(R.drawable.order_state_over));
				break;
			}

			return view;
		}
	}

	class DetailView {
		TextView txt_orderbillnumber;
		// TextView txt_orderstate;
		TextView txt_orderdate;
		ImageView iv_order_state;
	}

	/**
	 * ��ʼ���ȴ���
	 */
	private void InitDialogWait() {
		// �ȴ���
		dialogWait = new Dialog(OrderViewActivity.this, R.style.dialog);
		dialogWait.setCancelable(false);
		LayoutInflater inflaterWait = getLayoutInflater();
		View alertDialogViewWait = inflaterWait.inflate(
				R.layout.activity_dialog, null);
		dialogWait.setContentView(alertDialogViewWait);
	}

	private void LoadOrder() {
		Message message = new Message();
		message.what = 0;

		// ��ȡ��Ʒ����json
		Object result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					FunctionConst.OrderView);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			request.addProperty("c_Hybm", application.hyda.c_Hybm);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					application.URL);
			androidHttpTransport.call("http://tempuri.org/OrderView", envelope);
			result = (Object) envelope.getResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.obj = "��ȡʧ��";
			handlerOrderView.sendMessage(message);
			return;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.obj = "��ȡʧ��";
			handlerOrderView.sendMessage(message);
			return;
		}

		if (result == null || result.toString().trim().length() == 0
				|| result.toString().trim().equals("anyType{}")) {
			message.obj = "��ȡʧ��";
			handlerOrderView.sendMessage(message);
			return;
		} else {
			String order = result.toString();
			mainList = JsonUtil.fromJson(order, new TypeToken<List<ddzb>>() {
			}.getType());
		}
		message.obj = "��ȡ�ɹ�";
		handlerOrderView.sendMessage(message);
	}

	/**
	 * ������ѯhandler
	 */
	Handler handlerOrderView = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String strResult = msg.obj.toString();
				if (strResult.contains("��ȡ�ɹ�")) {
					InitListView();
				}

				dialogWait.dismiss();
			}
		};
	};

}
