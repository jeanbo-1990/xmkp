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
	 * �Ƿ������������ˢ��
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
	 * ��ʱ�����˵�ҳ�涥����
	 */
	Timer mTimer;

	/**
	 * �ȴ���
	 */
	Dialog dialogWait;

	/**
	 * ��ʾ��
	 */
	Toast toast;

	/**
	 * ͼƬ����
	 */
	ImageLoader imageLoader;

	/**
	 * ȫ����
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
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);// ʵ������ʾ��
		dialogWait.show();
		// ���ز˵�
		new Thread() {
			public void run() {
				GetViewData();
			};
		}.start();
	}

	public void Refresh() {
		dialogWait.show();
		isRefresh = true;
		// ���ز˵�
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
	 * ��ʼ���ȴ���
	 */
	private void InitDialogWait() {
		// �ȴ���
		dialogWait = new Dialog(ViewAcitvity.this, R.style.dialog);
		dialogWait.setCancelable(false);
		LayoutInflater inflaterWait = getLayoutInflater();
		View alertDialogViewWait = inflaterWait.inflate(
				R.layout.activity_dialog, null);
		dialogWait.setContentView(alertDialogViewWait);
	}

	/**
	 * ���ز˵�
	 */
	private void GetViewData() {
		Message message = new Message();
		message.what = 0;

		// ��ȡ��Ʒ����json
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

		String strGoodsCategory = result.toString();
		// ����ȡ����jsonת�ɶ���
		application.goodsCategoryList = JsonUtil.fromJson(strGoodsCategory,
				new TypeToken<List<t_spflInfo>>() {
				}.getType());

		tags = new int[application.goodsCategoryList.size()];
		for (int i = 0; i < application.goodsCategoryList.size(); i++) {
			t_spflInfo goodsCategory = application.goodsCategoryList.get(i);
			tags[i] = 0;
		}
		message.obj = "��ȡ�ɹ�";
		handlerGetData.sendMessage(message);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
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
				if (strResult.contains("��ȡ�ɹ�")) {
					prepareView();
					if (!isRefresh) {
						mTimer = new Timer();
						mTimer.scheduleAtFixedRate(new MyTask(), 0, 5000);
						isRefresh = false;
					}

				} else {
					toast.setText("��ȡ�˵�ʧ�ܣ�");
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
		emptyView.setText("���ز˵�ʧ�ܣ���������");
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
		// ������¼��ť״̬��Map
		public Map<String, Boolean> isChecked;

		public MyListAdapter() {
			init();
		}

		@Override
		public String getChild(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return application.goodsCategoryList.get(arg0).goodsList.get(arg1).c_Spbm;
		}

		// ��ʹ��������Ĭ�϶���false
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
			 * ��Ʒ��Ϣ�������¼�
			 */
			childView.linl_goodsInfo.setOnClickListener(new listView_btn_click(
					arg0, arg1));

			/**
			 * ��������������¼�
			 */
			childView.btn_reduction.setOnClickListener(new listView_btn_click(
					arg0, arg1));

			/**
			 * ���������ӵ���¼�
			 */
			childView.btn_add.setOnClickListener(new listView_btn_click(arg0,
					arg1));
			/**
			 * ������ť
			 */
			childView.btn_rmb.setOnClickListener(new listView_btn_click(arg0,
					arg1));

			/**
			 * ������ȷ��
			 */
			childView.btn_shopGoodsCount_ok
					.setOnClickListener(new listView_btn_click(arg0, arg1));

			if (isChecked.get(Integer.toString(arg0) + ";"
					+ Integer.toString(arg1))) {
				childView.linl_shopGoodsCount.setVisibility(View.VISIBLE);
			} else {
				childView.linl_shopGoodsCount.setVisibility(View.GONE);
			}

			// ��ֵ
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
									+ "Ԫ/��");

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
	 * ��Ʒ��Ϣ�ؼ���
	 * 
	 * @author wjb
	 * 
	 */
	class ChildView {

		/**
		 * ��Ʒ��Ϣ����
		 */
		LinearLayout linl_goodsInfo;

		/**
		 * ��ƷͼƬ
		 */
		ImageView imgV_goodsimage;

		/**
		 * ��Ʒ����
		 */
		TextView txt_goosName;
		/**
		 * ��Ʒ����
		 */
		TextView txt_goodsprice;

		/**
		 * ������ť
		 */
		ImageView btn_rmb;

		/**
		 * ��Ʒ���������޸Ĳ���
		 */
		LinearLayout linl_shopGoodsCount;

		/**
		 * ��������
		 */
		EditText edtxt_shopGoodsCount;

		/**
		 * ������
		 */
		ImageView btn_reduction;

		/**
		 * ������
		 */
		ImageView btn_add;

		/**
		 * ��������ȷ��
		 */
		ImageView btn_shopGoodsCount_ok;

		/**
		 * ���ؼ�id
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
