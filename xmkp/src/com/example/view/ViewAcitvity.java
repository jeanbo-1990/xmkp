package com.example.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bespeak.LoginActivity;
import com.example.bespeak.OperateActivity;
import com.example.bespeak.R;
import com.example.bespeak.common.BadgeView;
import com.example.bespeak.common.FunctionConst;
import com.example.bespeak.common.ImageLoader;
import com.example.bespeak.common.JsonUtil;
import com.example.bespeak.common.ProjectApplication;
import com.example.bespeak.model.t_SpdaInfo;
import com.example.bespeak.model.t_spflInfo;
import com.google.gson.reflect.TypeToken;

public class ViewAcitvity extends Activity {

	/**
	 * 是否其他界面调用刷新
	 */
	boolean isRefresh = false;
	public static ViewAcitvity thisView;
	static final int SCROLL_ACTION = 0;
	ExpandableListView mExpandableListView;
	int[] tags = new int[] { 0, 0, 0, 0, 0 };
	Gallery mGallery;
	GalleryAdapter mGalleryAdapter;
	FlowIndicator mMyView;

	/**
	 * 计时器，菜单页面顶端用
	 */
	Timer mTimer;

	/**
	 * 等待框
	 */
	Dialog dialogWait;

	/**
	 * 提示框
	 */
	Toast toast;

	/**
	 * 图片加载
	 */
	ImageLoader imageLoader;

	/**
	 * 全局类
	 */
	ProjectApplication application;

	MyListAdapter adapter;

	private long exitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		application = (ProjectApplication) getApplication();
		thisView = this;
		InitDialogWait();
		imageLoader = new ImageLoader(ViewAcitvity.this);
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);// 实例化提示框
		dialogWait.show();
		// 加载菜单
		new Thread() {
			public void run() {
				GetViewData();
			};
		}.start();
	}

	public void Refresh() {
		dialogWait.show();
		isRefresh = true;
		// 加载菜单
		new Thread() {
			public void run() {
				GetViewData();
			};
		}.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		adapter.notifyDataSetChanged();
	}

	/**
	 * 初始化等待框
	 */
	private void InitDialogWait() {
		// 等待框
		dialogWait = new Dialog(ViewAcitvity.this, R.style.dialog);
		dialogWait.setCancelable(false);
		LayoutInflater inflaterWait = getLayoutInflater();
		View alertDialogViewWait = inflaterWait.inflate(
				R.layout.activity_dialog, null);
		dialogWait.setContentView(alertDialogViewWait);
	}

	/**
	 * 加载菜单
	 */
	private void GetViewData() {
		Message message = new Message();
		message.what = 0;

		// 获取商品分类json
		Object result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					FunctionConst.GetAllGoodsCategory);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					application.URL);
			androidHttpTransport.call("http://tempuri.org/GetGoodsCategory",
					envelope);
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

		String strGoodsCategory = result.toString();
		// 将获取到的json转成对象
		application.goodsCategoryList = JsonUtil.fromJson(strGoodsCategory,
				new TypeToken<List<t_spflInfo>>() {
				}.getType());

		tags = new int[application.goodsCategoryList.size()];
		for (int i = 0; i < application.goodsCategoryList.size(); i++) {
			t_spflInfo goodsCategory = application.goodsCategoryList.get(i);
			tags[i] = 0;
		}
		message.obj = "获取成功";
		handlerGetData.sendMessage(message);
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

	Handler handlerGetData = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String strResult = msg.obj.toString();
				if (strResult.contains("获取成功")) {
					prepareView();
					if (!isRefresh) {
						mTimer = new Timer();
						mTimer.scheduleAtFixedRate(new MyTask(), 0, 5000);
						isRefresh = false;
					}

				} else {
					toast.setText("获取菜单失败！");
					toast.show();
				}

				dialogWait.dismiss();
			}
		};
	};

	private class MyTask extends TimerTask {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(SCROLL_ACTION);
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SCROLL_ACTION:

				if (mGalleryAdapter.postion == mGalleryAdapter.getCount() - 1) {
					mGallery.setSelection((mGalleryAdapter.postion + 1)
							% mGalleryAdapter.getCount());//
				} else {
					mGallery.onFling(null, null, -1300, 0);
				}
				break;

			default:
				break;
			}
		}
	};

	private void prepareView() {
		mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView1);
		TextView emptyView = new TextView(ViewAcitvity.this);
		emptyView.setTextColor(getResources().getColor(R.color.red));
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		emptyView.setText("加载菜单失败！点我重试");
		emptyView.setClickable(true);
		emptyView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread() {
					public void run() {
						GetViewData();
					};
				}.start();
			}
		});
		emptyView.setVisibility(View.GONE);
		((ViewGroup) mExpandableListView.getParent()).addView(emptyView);
		mExpandableListView.setEmptyView(emptyView);

		adapter = new MyListAdapter();
		View header = LayoutInflater.from(this).inflate(R.layout.header_view,
				null);

		if (!isRefresh) {
			mGalleryAdapter = new GalleryAdapter(this);
			mMyView = (FlowIndicator) header.findViewById(R.id.myView);
			mMyView.setCount(mGalleryAdapter.getCount());
			mGallery = (Gallery) header.findViewById(R.id.home_gallery);
			mGallery.setAdapter(mGalleryAdapter);
			mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					mMyView.setSeletion(arg2 % mGalleryAdapter.getCount());
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});
			mExpandableListView.addHeaderView(header);

		}
		mExpandableListView.setAdapter(adapter);

		mExpandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int arg0) {
						// TODO Auto-generated method stub
						tags[arg0] = 1;
					}
				});
		mExpandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int arg0) {
						// TODO Auto-generated method stub
						tags[arg0] = 0;
					}
				});
	}

	class MyListAdapter extends BaseExpandableListAdapter {
		ChildView childView;
		// 用来记录按钮状态的Map
		public Map<String, Boolean> isChecked;

		public MyListAdapter() {
			init();
		}

		@Override
		public String getChild(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return application.goodsCategoryList.get(arg0).goodsList.get(arg1).c_Spbm;
		}

		// 初使化操作，默认都是false
		private void init() {
			isChecked = new HashMap<String, Boolean>();
			for (int i = 0; i < application.goodsCategoryList.size(); i++) {
				for (int j = 0; j < application.goodsCategoryList.get(i).goodsList
						.size(); j++) {
					isChecked.put(
							Integer.toString(i) + ";" + Integer.toString(j),
							false);
				}

			}
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
				ViewGroup arg4) {
			// TODO Auto-generated method stub

			final t_SpdaInfo goods = application.goodsCategoryList.get(arg0).goodsList
					.get(arg1);
			if (arg3 == null) {
				childView = new ChildView();
				arg3 = LayoutInflater.from(ViewAcitvity.this).inflate(
						R.layout.list_child_item, null);
				childView.linl_goodsInfo = (LinearLayout) arg3
						.findViewById(R.id.linl_goodsInfo);
				childView.imgV_goodsimage = (ImageView) arg3
						.findViewById(R.id.imgV_goodsimage);
				childView.txt_goosName = (TextView) arg3
						.findViewById(R.id.txt_goosName);
				childView.txt_goodsprice = (TextView) arg3
						.findViewById(R.id.txt_goodsprice);
				childView.btn_rmb = (ImageView) arg3.findViewById(R.id.btn_rmb);
				childView.linl_shopGoodsCount = (LinearLayout) arg3
						.findViewById(R.id.linl_shopGoodsCount);
				childView.linl_shopGoodsCount.setTag(Integer.toString(arg0));
				childView.edtxt_shopGoodsCount = (EditText) arg3
						.findViewById(R.id.edtxt_shopGoodsCount);
				childView.btn_reduction = (ImageView) arg3
						.findViewById(R.id.btn_reduction);
				childView.btn_add = (ImageView) arg3.findViewById(R.id.btn_add);
				childView.btn_shopGoodsCount_ok = (ImageView) arg3
						.findViewById(R.id.btn_shopGoodsCount_ok);
				childView.groupId = arg0;

				arg3.setTag(childView);
			} else {
				childView = (ChildView) arg3.getTag();
			}

			/**
			 * 商品信息区域点击事件
			 */
			childView.linl_goodsInfo.setOnClickListener(new listView_btn_click(
					arg0, arg1));

			/**
			 * 订购数量减点击事件
			 */
			childView.btn_reduction.setOnClickListener(new listView_btn_click(
					arg0, arg1));

			/**
			 * 订购数量加点击事件
			 */
			childView.btn_add.setOnClickListener(new listView_btn_click(arg0,
					arg1));
			/**
			 * 订购按钮
			 */
			childView.btn_rmb.setOnClickListener(new listView_btn_click(arg0,
					arg1));

			/**
			 * 订购数确定
			 */
			childView.btn_shopGoodsCount_ok
					.setOnClickListener(new listView_btn_click(arg0, arg1));

			if (isChecked.get(Integer.toString(arg0) + ";"
					+ Integer.toString(arg1))) {
				childView.linl_shopGoodsCount.setVisibility(View.VISIBLE);
			} else {
				childView.linl_shopGoodsCount.setVisibility(View.GONE);
			}

			// 赋值
			childView.txt_goosName.setText(goods.c_Spmc);
			childView.txt_goodsprice.setText(Double.toString(goods.c_Spsj));
			childView.edtxt_shopGoodsCount.setText(Integer
					.toString(goods.OrderAmcount));

			imageLoader.DisplayImage(goods.c_Xtp, childView.imgV_goodsimage);

			return arg3;
		}

		@Override
		public int getChildrenCount(int arg0) {
			// TODO Auto-generated method stub
			return application.goodsCategoryList.get(arg0).goodsList.size();
		}

		@Override
		public Object getGroup(int arg0) {
			// TODO Auto-generated method stub
			return application.goodsCategoryList.get(arg0);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return application.goodsCategoryList.size();
		}

		@Override
		public long getGroupId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		class GroupHolder {
			ImageView img;
			TextView title;
		}

		@Override
		public View getGroupView(int arg0, boolean arg1, View arg2,
				ViewGroup arg3) {
			// TODO Auto-generated method stub
			GroupHolder groupHolder;
			if (arg2 == null) {
				arg2 = LayoutInflater.from(ViewAcitvity.this).inflate(
						R.layout.list_group_item, null);
				groupHolder = new GroupHolder();
				groupHolder.img = (ImageView) arg2.findViewById(R.id.tag_img);
				groupHolder.title = (TextView) arg2
						.findViewById(R.id.title_view);
				arg2.setTag(groupHolder);
			} else {
				groupHolder = (GroupHolder) arg2.getTag();
			}
			if (tags[arg0] == 0) {
				groupHolder.img.setImageResource(R.drawable.group_item_nomal);
			} else {
				groupHolder.img.setImageResource(R.drawable.group_item_down);
			}
			groupHolder.title
					.setText(((t_spflInfo) application.goodsCategoryList
							.get(arg0)).c_flmc);

			return arg2;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return true;
		}

		class listView_btn_click implements OnClickListener {

			private String groupPostion;
			private String childPostion;

			public listView_btn_click(int groupPostion, int childPostion) {
				this.groupPostion = Integer.toString(groupPostion);
				this.childPostion = Integer.toString(childPostion);
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int vId = v.getId();
				if (vId == childView.btn_rmb.getId()) {

					if (application.hyda == null
							|| application.hyda.isLogin == 0) {
						Intent intentLogin = new Intent(ViewAcitvity.this,
								LoginActivity.class);
						startActivity(intentLogin);
						return;
					}

					if (!isChecked.get(groupPostion + ";" + childPostion)) {
						isChecked.put(groupPostion + ";" + childPostion, true);
						childView.linl_shopGoodsCount
								.setVisibility(View.VISIBLE);

					} else {
						isChecked.put(groupPostion + ";" + childPostion, false);
						childView.linl_shopGoodsCount.setVisibility(View.GONE);
					}
					notifyDataSetChanged();
				}
				if (vId == childView.btn_add.getId()) {
					application.goodsCategoryList.get(Integer
							.parseInt(groupPostion)).goodsList.get(Integer
							.parseInt(childPostion)).OrderAmcount += 1;
					notifyDataSetChanged();
				}
				if (vId == childView.btn_reduction.getId()
						&& application.goodsCategoryList.get(Integer
								.parseInt(groupPostion)).goodsList.get(Integer
								.parseInt(childPostion)).OrderAmcount >= 1) {
					application.goodsCategoryList.get(Integer
							.parseInt(groupPostion)).goodsList.get(Integer
							.parseInt(childPostion)).OrderAmcount -= 1;
					notifyDataSetChanged();
				}
				if (vId == childView.btn_shopGoodsCount_ok.getId()) {

					// isChecked.put(groupPostion + ";" + childPostion, false);
					// childView.linl_shopGoodsCount.setVisibility(View.GONE);
					// notifyDataSetChanged();

					if (application.badge == null) {
						application.badge = new BadgeView(
								ViewAcitvity.this.getParent(),
								OperateActivity.operateActivity.operate_menu_car);
					}
					if (application.GetOrderCount() > 0) {
						application.badge.setText(Integer.toString(application
								.GetOrderCount()));
						application.badge.show();
					}

				}

				if (vId == childView.linl_goodsInfo.getId()) {
					final AlertDialog dialog_foods = new AlertDialog.Builder(
							ViewAcitvity.this).create();
					dialog_foods.show();
					Window window = dialog_foods.getWindow();
					window.setContentView(R.layout.dialog_foods_view);
					ImageView imag_close = (ImageView) window
							.findViewById(R.id.foodDialog_btn_close);
					ImageView img_dialogView_goods = (ImageView) window
							.findViewById(R.id.img_dialogView_goods);
					//
					// img_dialogView_goods
					// .setImageBitmap(application.goodsCategoryList.get(Integer
					// .parseInt(groupPostion)).goodsList
					// .get(Integer.parseInt(childPostion)).bitmap);

					imageLoader.DisplayImage(application.goodsCategoryList
							.get(Integer.parseInt(groupPostion)).goodsList
							.get(Integer.parseInt(childPostion)).c_Dtp,
							img_dialogView_goods);

					TextView txt_dialogView_goodsName = (TextView) window
							.findViewById(R.id.txt_dialogView_goodsName);
					txt_dialogView_goodsName
							.setText(application.goodsCategoryList.get(Integer
									.parseInt(groupPostion)).goodsList
									.get(Integer.parseInt(childPostion)).c_Spmc);

					TextView txt_dialogView_goodsPrice = (TextView) window
							.findViewById(R.id.txt_dialogView_goodsPrice);
					txt_dialogView_goodsPrice
							.setText(application.goodsCategoryList.get(Integer
									.parseInt(groupPostion)).goodsList
									.get(Integer.parseInt(childPostion)).c_Spsj
									+ "元/份");

					imag_close.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method
							// stub
							dialog_foods.cancel();
						}
					});
				}
			}
		}

	}

	/**
	 * 商品信息控件类
	 * 
	 * @author wjb
	 * 
	 */
	class ChildView {

		/**
		 * 商品信息布局
		 */
		LinearLayout linl_goodsInfo;

		/**
		 * 商品图片
		 */
		ImageView imgV_goodsimage;

		/**
		 * 商品名称
		 */
		TextView txt_goosName;
		/**
		 * 商品单价
		 */
		TextView txt_goodsprice;

		/**
		 * 订购按钮
		 */
		ImageView btn_rmb;

		/**
		 * 商品订购数量修改布局
		 */
		LinearLayout linl_shopGoodsCount;

		/**
		 * 订购数量
		 */
		EditText edtxt_shopGoodsCount;

		/**
		 * 数量减
		 */
		ImageView btn_reduction;

		/**
		 * 数量加
		 */
		ImageView btn_add;

		/**
		 * 订购数量确定
		 */
		ImageView btn_shopGoodsCount_ok;

		/**
		 * 父控件id
		 */
		int groupId;

		public LinearLayout getLinl_shopGoodsCount() {
			return linl_shopGoodsCount;
		}

		public void setLinl_shopGoodsCount(LinearLayout linl_shopGoodsCount) {
			this.linl_shopGoodsCount = linl_shopGoodsCount;
		}

	}
}
