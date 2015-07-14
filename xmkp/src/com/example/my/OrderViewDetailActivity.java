package com.example.my;

import com.example.bespeak.R;
import com.example.bespeak.R.layout;
import com.example.bespeak.R.menu;
import com.example.bespeak.model.ddmx;
import com.example.bespeak.model.ddzb;
import com.example.my.OrderViewActivity.DetailAdapter;
import com.example.my.OrderViewActivity.DetailView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class OrderViewDetailActivity extends Activity {

	/**
	 * 明细列表
	 */
	private ListView lv_orderviewdetail;

	/**
	 * 总金额
	 */
	private TextView txt_orderview_totalmoney;

	/**
	 * 会员姓名和电话
	 */
	private TextView txt_order_view_phone;

	/**
	 * 配送地址
	 */
	private TextView txt_order_view_address;

	DetailAdapter detailAdapter;
	ddzb main;

	/**
	 * 返回
	 */
	ImageView btn_order_veiw_detial_back;

	/**
	 * 配送单号
	 */
	TextView txt_peisong_bill;

	/**
	 * 配送时间
	 */
	TextView txt_peisong_date;

	/**
	 * 配送员名称
	 */
	TextView txt_peisong_name;

	/**
	 * 配送员电话
	 */
	TextView txt_peisong_phone;

	/**
	 * 配送信息块
	 */
	LinearLayout ll_peisong;

	/**
	 * 配送费
	 */
	TextView txt_view_detail_peisongfei;

	/**
	 * 配送费
	 */
	double peisongfei = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_view_detail);

		main = (ddzb) this.getIntent().getSerializableExtra("order");
		// 移除配送费
		for (ddmx detail : main.detailList) {
			if (detail.c_Spbm.equals("999999")) {
				peisongfei = detail.c_Ddje;
				main.detailList.remove(detail);
			}
		}
		InitControl();
		InitListView();
	}

	/**
	 * 初始化listview数据适配
	 */
	private void InitListView() {
		detailAdapter = new DetailAdapter();
		lv_orderviewdetail.setAdapter(detailAdapter);
	}

	/**
	 * 订购明细数据适配器
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
			return main.detailList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return main.detailList.get(arg0);
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
				view = LayoutInflater.from(OrderViewDetailActivity.this)
						.inflate(R.layout.order_view_detail_list_item, null);
				assignView.txt_order_detail_name = (TextView) view
						.findViewById(R.id.txt_order_detail_name);
				assignView.txt_order_detail_money = (TextView) view
						.findViewById(R.id.txt_order_detail_money);
				assignView.txt_order_detail_amount = (TextView) view
						.findViewById(R.id.txt_order_detail_amount);

				view.setTag(assignView);
			} else {
				assignView = (DetailView) view.getTag();
			}

			// /赋值
			assignView.txt_order_detail_name.setText(main.detailList
					.get(postion).c_Spmc);
			assignView.txt_order_detail_money.setText("￥"
					+ main.detailList.get(postion).c_Ddje);
			assignView.txt_order_detail_amount.setText(main.detailList
					.get(postion).c_Ddsl + "份");

			return view;
		}
	}

	class DetailView {
		TextView txt_order_detail_name;
		TextView txt_order_detail_money;
		TextView txt_order_detail_amount;
	}

	/**
	 * 初始化控件
	 */
	private void InitControl() {
		lv_orderviewdetail = (ListView) findViewById(R.id.lv_orderviewdetail);
		txt_orderview_totalmoney = (TextView) findViewById(R.id.txt_orderview_totalmoney);
		txt_orderview_totalmoney.setText(Double.toString(main.c_Ddgz));
		txt_order_view_phone = (TextView) findViewById(R.id.txt_order_view_phone);
		txt_order_view_phone
				.setText(main.c_Hymc.substring(0, 1) + "**"
						+ main.c_Hybm.substring(0, 3) + "**"
						+ main.c_Hybm.substring(7));
		txt_order_view_address = (TextView) findViewById(R.id.txt_order_view_address);
		txt_order_view_address.setText(main.c_Ss + main.c_Qs + main.c_Xq + "**"
				+ main.c_Xxdz.substring(main.c_Xxdz.length() - 4));

		btn_order_veiw_detial_back = (ImageView) findViewById(R.id.btn_order_veiw_detial_back);
		btn_order_veiw_detial_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		txt_peisong_bill = (TextView) findViewById(R.id.txt_peisong_bill);
		txt_peisong_date = (TextView) findViewById(R.id.txt_peisong_date);
		txt_peisong_name = (TextView) findViewById(R.id.txt_peisong_name);
		txt_peisong_phone = (TextView) findViewById(R.id.txt_peisong_phone);
		ll_peisong = (LinearLayout) findViewById(R.id.ll_peisong);
		if (main.c_Djzt == 2) {
			ll_peisong.setVisibility(View.VISIBLE);
			txt_peisong_bill.setText(main.psmx.c_Djh);
			txt_peisong_date.setText(main.c_Pssj.substring(0, 2) + ":"
					+ main.c_Pssj.substring(2, 4) + ":"
					+ main.c_Pssj.substring(4));
			txt_peisong_name.setText(main.psmx.c_Psrmc2);
			txt_peisong_phone.setText(main.psmx.c_Dh);
		} else {
			ll_peisong.setVisibility(View.GONE);
		}
		txt_view_detail_peisongfei = (TextView) findViewById(R.id.txt_view_detail_peisongfei);
		txt_view_detail_peisongfei.setText(Double.toString(peisongfei));
	}
}
