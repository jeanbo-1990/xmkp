package com.example.car;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bespeak.DeliveryInfoActivity;
import com.example.bespeak.LoginActivity;
import com.example.bespeak.OperateActivity;
import com.example.bespeak.R;
import com.example.bespeak.common.FunctionConst;
import com.example.bespeak.common.JsonUtil;
import com.example.bespeak.common.ProjectApplication;
import com.example.bespeak.model.t_HydaInfo;
import com.example.bespeak.model.t_SpdaInfo;
import com.example.bespeak.model.t_YYYYMM_DddjInfo;
import com.example.bespeak.model.t_YYYYMM_DdmxInfo;
import com.example.bespeak.model.t_spflInfo;
import com.example.view.ViewAcitvity;

public class CarAcitvity extends Activity {

	/**
	 * ������Ʒ��Ϣ
	 */
	ListView lv_orderdetail;

	/**
	 * �����ܶ�
	 */
	TextView txt_totalmoney;

	/**
	 * �ͻ����ƺ��ֻ�����
	 */
	TextView txt_car_order_phone;

	/**
	 * �Ͳ͵�ַ
	 */
	TextView txt_car_order_address;

	/**
	 * �ύ����
	 */
	ImageView btn_submitorder;

	/**
	 * ȫ����
	 */
	ProjectApplication application;

	private long exitTime = 0;

	/**
	 * �ȴ���
	 */
	Dialog dialogWait;

	/**
	 * ��ʾ��
	 */
	Toast toast;

	DetailAdapter detailAdapter;

	/**
	 * ���ͷ�
	 */
	TextView txt_car_peisongfei;

	/**
	 * ȷ����ť
	 */
	AlertDialog dlg;

	/**
	 * ֧����ʽ
	 */
	TextView txt_paystyle;

	/**
	 * ֧����ʽѡ�񴰿�
	 */
	AlertDialog payStyleDialog;

	/**
	 * ��������
	 */
	t_YYYYMM_DddjInfo newBill;

	// ���ʽ��ʾ����
	AlertDialog.Builder buildPayStyle;
	View view;

	private SharedPreferences sp;

	/**
	 * �Ƿ����ύ����
	 */
	boolean isSubmit = false;

	/**
	 * �ύ������ʾ��
	 */
	Dialog alertSubmitTipDialog;
	
	/**
	 * 
	 */
	RadioGroup currGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car);

		InitPayStryleDialog();
		application = (ProjectApplication) getApplication();
		sp = this.getSharedPreferences(application.hyda.c_Hybm + "paystyle",
				Context.MODE_WORLD_READABLE);
		// ������Ʒ��ϸ
		application.GetOrderDetail();
		newBill = new t_YYYYMM_DddjInfo();
		newBill.c_Zfgs=sp.getInt("paystyleInt", 0);
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);// ʵ������ʾ��
		InitDialogWait();
		InitSaveTipDialog();
		dialogWait.show();
		new Thread() {
			public void run() {
				LoadPeiSongFei();
			};
		}.start();
	}

	/**
	 * �������ͷ�
	 */
	private void LoadPeiSongFei() {
		Message message = new Message();
		message.what = 0;
		// ��ȡ��Աjson
		Object result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					FunctionConst.LoadPeiSongFei);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					application.URL);
			androidHttpTransport.call("http://tempuri.org/LoadPeiSongFei",
					envelope);
			result = (Object) envelope.getResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.obj = "0";
			handlerLoadPeiSongFei.sendMessage(message);
			return;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.obj = "0";
			handlerLoadPeiSongFei.sendMessage(message);
			return;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		message.obj = result.toString();
		handlerLoadPeiSongFei.sendMessage(message);
	}

	/**
	 * ���ͷ�
	 */
	Handler handlerLoadPeiSongFei = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String strResult = msg.obj.toString();
				if (strResult.contains("1")) {
					application.peisongfei = Double.parseDouble(strResult
							.split(",")[0]);
					application.peisongfeiFree = Double.parseDouble(strResult
							.split(",")[1]);
					InitControl();
					SetListEmptyView();// ����û�����ݵ�view
					InitListView();
				} else {
					toast.setText("��ȡ���ͷ�ʧ�ܣ�");
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
		dialogWait = new Dialog(CarAcitvity.this, R.style.dialog);
		dialogWait.setCancelable(false);
		LayoutInflater inflaterWait = getLayoutInflater();
		View alertDialogViewWait = inflaterWait.inflate(
				R.layout.activity_dialog, null);
		dialogWait.setContentView(alertDialogViewWait);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		application = (ProjectApplication) getApplication();
		sp = this.getSharedPreferences(application.hyda.c_Hybm + "paystyle",
				Context.MODE_WORLD_READABLE);
		InitPayStryleDialog();
		// ������Ʒ��ϸ
		application.GetOrderDetail();
		newBill = new t_YYYYMM_DddjInfo();
		newBill.c_Zfgs=sp.getInt("paystyleInt", 0);
		InitControl();
		SetListEmptyView();// ����û�����ݵ�view
		InitListView();
	}

	private void InitSaveTipDialog() {
		alertSubmitTipDialog = new AlertDialog.Builder(this)
				.setTitle("�ύ")
				.setMessage("��ȷ���ύ������")
				.setIcon(R.drawable.ic_launcher)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (application.orderDetail.size() > 0) {
							if (!sp.getBoolean("check_noshow", false)) {
								view.findViewById(R.id.check_noshow)
										.setVisibility(View.VISIBLE);
								payStyleDialog.show();
								Window window = payStyleDialog.getWindow();
								window.setContentView(view);
								isSubmit = true;
							} else {
								SubmitOrder();
							}
						} else {
							toast.setText("����û�ж���Ŷ��");
							toast.show();
							return;
						}
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).create();
	}

	/**
	 * ��ʼ��listview��������
	 */
	private void InitListView() {
		detailAdapter = new DetailAdapter();
		lv_orderdetail.setAdapter(detailAdapter);
	}

	private void InitPayStryleDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.dialog_select_paystyle, null);
		// ��������
		ImageView btn_select_paystyle_ok = (ImageView) view
				.findViewById(R.id.btn_select_paystyle_ok);
		ImageView btn_select_paystyle_cancle = (ImageView) view
				.findViewById(R.id.btn_select_paystyle_cancle);
		final RadioButton rb_h = (RadioButton) view.findViewById(R.id.rb_h);// ��������
		final RadioButton rb_z = (RadioButton) view.findViewById(R.id.rb_z);// �˻����
		final RadioGroup radioGroup = (RadioGroup) view
				.findViewById(R.id.radioGroup);
		// �˻�������ˣ�Ҫ�ж��˻�����Ƿ����0
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						currGroup=group;
						if (checkedId == R.id.rb_z) {
							dialogWait.show();
							new Thread(){
								public void run() {
									GetData();
								};
							}.start();
						}
					}
				});

		btn_select_paystyle_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ѡ�����Ҫ�ı�֧����ʽ
				newBill.c_Zfgs = radioGroup.getCheckedRadioButtonId() == rb_h
						.getId() ? 0 : 1;
				payStyleDialog.dismiss();
				if (isSubmit) {
					CheckBox check_noshow = (CheckBox) view
							.findViewById(R.id.check_noshow);
					// ��סѡ��
					sp.edit()
							.putBoolean("check_noshow",
									check_noshow.isChecked()).commit();
					if (check_noshow.isChecked()) {
						sp.edit().putInt("paystyleInt", newBill.c_Zfgs).commit();
					} else {
						sp.edit().putInt("paystyleInt", 0).commit();
					}
					SubmitOrder();
				}
			}
		});
		btn_select_paystyle_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				radioGroup.check(newBill.c_Zfgs == 0 ? R.id.rb_h : R.id.rb_z);
				payStyleDialog.dismiss();
			}
		});
		buildPayStyle = new AlertDialog.Builder(CarAcitvity.this);
		payStyleDialog = buildPayStyle.create();
		payStyleDialog.setCanceledOnTouchOutside(false);
		payStyleDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				// TODO Auto-generated method stub
				radioGroup.check(newBill.c_Zfgs == 0 ? R.id.rb_h : R.id.rb_z);
			}
		});
	}
	
	private void GetData(){
		Message message = new Message();
		message.what = 0;
		// ��ȡ��Աjson
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
			message.obj = strXQ;
			handlerGetData.sendMessage(message);
		}
	}
	
	/**
	 * �ز��Աhandler
	 */
	Handler handlerGetData = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String strResult = msg.obj.toString();
				if (!strResult.contains("��ȡʧ��")) {
					t_HydaInfo hy=JsonUtil.fromJson(strResult,
							t_HydaInfo.class);
					application.hyda.c_Hymc=hy.c_Hymc;
					application.hyda.c_Yddh=hy.c_Yddh;
					application.hyda.c_Hyye=hy.c_Hyye;
					application.hyda.c_Jf=hy.c_Jf;
					
					if (application.hyda.c_Hyye <= 0) {
						currGroup.check(R.id.rb_h);
						toast.setText("���㣬���ֵ��");
						toast.show();
						dialogWait.dismiss();
						return;
					}
				} else {
					toast.setText("��ȡʧ�ܣ�");
					toast.show();
				}
				dialogWait.dismiss();
			}
		};
	};

	private void SubmitOrder() {
		if (application.orderDetail.size() > 0) {
			
			//���֧��Ҫ�ж��Ƿ񹻸�
//			if(newBill.c_Zfgs==1){
//				if(application.hyda.c_Hyye<newBill.c_Ddgz){
//					toast.setText("���㣡");
//					toast.show();
//					return;
//				}
//			}
			
			application.GetOrderDetail();
			newBill.oldPeisongfei = application.peisongfei;
			newBill.peisongfeiFree = application.peisongfeiFree;

			newBill.c_Hybm = application.hyda.c_Hybm;
			newBill.c_XQID = application.hyda.c_XQID;
			newBill.c_Xxdz = application.hyda.c_Xxdz;
			newBill.c_DzID = application.hyda.c_xxdzid;
			newBill.c_Ddgz = application.GetTotalMoney();

			for (t_SpdaInfo spda : application.orderDetail) {
				t_YYYYMM_DdmxInfo detail = new t_YYYYMM_DdmxInfo();
				detail.c_Spbm = spda.c_Spbm;
				detail.c_Ddsl = spda.OrderAmcount;
				detail.c_Spjj = spda.c_Spjj;
				detail.c_Jjje = application.m2(spda.c_Spjj * spda.OrderAmcount);
				detail.c_Spsj = spda.c_Spsj;
				detail.c_Ddje = application.m2(spda.c_Spsj * spda.OrderAmcount);
				newBill.detailList.add(detail);
			}
			dialogWait.show();
			// �¶���
			new Thread() {
				public void run() {
					SaveOrder(newBill);
				};
			}.start();
		}
	}

	/**
	 * �����ͷѴ���
	 */
	private void PeiSongFeiDo() {
		if (application.peisongfeiFree >= 0) {
			if (application.m2(application.GetTotalMoney()) > application.peisongfeiFree) {
				txt_car_peisongfei.setText("0Ԫ");
				txt_totalmoney.setText(Double.toString(application
						.m2(application.GetTotalMoney())));
				newBill.peisongfei = 0;
			} else {
				txt_car_peisongfei.setText(Double
						.toString(application.peisongfei) + "Ԫ");
				txt_totalmoney.setText(Double.toString(application
						.m2(application.GetTotalMoney()
								+ application.peisongfei)));
				newBill.peisongfei = application.peisongfei;
			}
		} else {
			txt_car_peisongfei.setText(Double.toString(application.peisongfei)
					+ "Ԫ");
			txt_totalmoney.setText(Double.toString(application.m2(application
					.GetTotalMoney() + application.peisongfei)));
			newBill.peisongfei = application.peisongfei;
		}
	}

	/**
	 * �ؼ���ʼ��
	 */
	private void InitControl() {
		txt_paystyle = (TextView) findViewById(R.id.txt_paystyle);
		txt_paystyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (newBill == null || application.orderDetail.size() <= 0) {
					toast.setText("����û�ж���Ŷ��");
					toast.show();
					return;
				} else {
					view.findViewById(R.id.check_noshow).setVisibility(
							View.GONE);
					payStyleDialog.show();
					Window window = payStyleDialog.getWindow();
					window.setContentView(view);
					isSubmit = false;
				}
			}
		});

		txt_car_peisongfei = (TextView) findViewById(R.id.txt_car_peisongfei);

		lv_orderdetail = (ListView) findViewById(R.id.lv_orderdetail);
		txt_totalmoney = (TextView) findViewById(R.id.txt_totalmoney);

		txt_car_order_phone = (TextView) findViewById(R.id.txt_car_order_phone);
		txt_car_order_phone.setText(application.hyda.c_Hymc.subSequence(0, 1)
				+ "**"
				+ application.hyda.c_Hybm.substring(0, 3)
				+ "**"
				+ application.hyda.c_Hybm.substring(
						application.hyda.c_Hybm.length() - 4,
						application.hyda.c_Hybm.length()));
		txt_car_order_address = (TextView) findViewById(R.id.txt_car_order_address);
		txt_car_order_address.setText(application.hyda.c_XQID_Name.substring(0,
				3)
				+ "**"
				+ application.hyda.c_Xxdz.trim().substring(
						application.hyda.c_Xxdz.length() - 4,
						application.hyda.c_Xxdz.length()));
		btn_submitorder = (ImageView) findViewById(R.id.btn_submitorder);
		btn_submitorder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertSubmitTipDialog.show();
			}
		});
		PeiSongFeiDo();
	}

	private void SaveOrder(t_YYYYMM_DddjInfo bill) {
		Message message = new Message();
		message.what = 0;
		// ��ȡ��Աjson
		Object result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					FunctionConst.SaveOrder);
			request.addProperty("strOrderJson", JsonUtil.toJson(bill));
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					application.URL);
			androidHttpTransport.call("http://tempuri.org/SaveOrder", envelope);
			result = (Object) envelope.getResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.obj = "0";
			handlerSaveOrder.sendMessage(message);
			return;
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message.obj = "0";
			handlerSaveOrder.sendMessage(message);
			return;
		}

		message.obj = result.toString();
		handlerSaveOrder.sendMessage(message);
	}

	/**
	 * �¶���handler
	 */
	Handler handlerSaveOrder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String strResult = msg.obj.toString();
				if (strResult.contains("1")) {
					// �������
					for (t_spflInfo spfl : application.goodsCategoryList) {
						for (t_SpdaInfo spda : spfl.goodsList) {
							spda.OrderAmcount = 0;
						}
					}
					application.GetOrderDetail();
					detailAdapter.Refresh();
					if (application.GetOrderCount() == 0) {
						application.badge.hide();
					}
					OperateActivity.operateActivity.operate_menu_view
							.performClick();
					ViewAcitvity.thisView.Refresh();
					if (dlg == null) {
						dlg = new AlertDialog.Builder(CarAcitvity.this)
								.create();
						dlg.show();
						Window window = dlg.getWindow();
						dlg.setCanceledOnTouchOutside(false);
						// *** ��Ҫ����������ʵ������Ч����.
						// ���ô��ڵ�����ҳ��,shrew_exit_dialog.xml�ļ��ж���view����
						window.setContentView(R.layout.dialog_success);
						// Ϊȷ�ϰ�ť����¼�,ִ���˳�Ӧ�ò���
						ImageView btn_dialog_success_ok = (ImageView) window
								.findViewById(R.id.btn_dialog_success_ok);
						btn_dialog_success_ok
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										dlg.dismiss();
									}
								});
					} else {
						dlg.show();
					}
				} else if (strResult.contains("0")) {
					toast.setText("����ʧ�ܣ�");
					toast.show();
				}
				else if (strResult.contains("2")) {
					toast.setText("���㣡");
					toast.show();
				}
				dialogWait.dismiss();
			}
		};
	};

	/**
	 * listView ����Ϊ�յ�ʱ��view
	 */
	private void SetListEmptyView() {
		TextView emptyView = new TextView(CarAcitvity.this);
		emptyView.setTextColor(getResources().getColor(R.color.red));
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		emptyView.setText("û������");
		emptyView.setVisibility(View.GONE);
		((ViewGroup) lv_orderdetail.getParent()).addView(emptyView);
		lv_orderdetail.setEmptyView(emptyView);
	}

	/**
	 * ������ϸ����������
	 * 
	 * @author wjb
	 * 
	 */
	class DetailAdapter extends BaseAdapter {

		DetailView assignView;

		public void Refresh() {
			notifyDataSetChanged();
			PeiSongFeiDo();
			if (application.GetOrderCount() == 0) {
				application.badge.hide();
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return application.orderDetail.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return application.orderDetail.get(arg0);
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
				assignView = new DetailView();
				view = LayoutInflater.from(CarAcitvity.this).inflate(
						R.layout.car_goods_list_item, null);
				assignView.btn_cardetail_clear = (ImageView) view
						.findViewById(R.id.btn_cardetail_clear);
				assignView.txt_car_item_goodsName = (TextView) view
						.findViewById(R.id.txt_car_item_goodsName);
				assignView.txt_car_item_goodsMoney = (TextView) view
						.findViewById(R.id.txt_car_item_goodsMoney);
				assignView.edtxt_cardetail_shopGoodsCount = (TextView) view
						.findViewById(R.id.edtxt_cardetail_shopGoodsCount);
				assignView.btn_cardetail_reduction = (ImageView) view
						.findViewById(R.id.btn_cardetail_reduction);
				assignView.btn_cardetail_add = (ImageView) view
						.findViewById(R.id.btn_cardetail_add);

				view.setTag(assignView);
			} else {
				assignView = (DetailView) view.getTag();
			}

			assignView.btn_cardetail_clear
					.setOnClickListener(new listView_btn_click(postion));
			assignView.btn_cardetail_reduction
					.setOnClickListener(new listView_btn_click(postion));
			assignView.btn_cardetail_add
					.setOnClickListener(new listView_btn_click(postion));
			// /��ֵ
			assignView.txt_car_item_goodsName.setText(application.orderDetail
					.get(postion).c_Spmc);
			assignView.txt_car_item_goodsMoney
					.setText("��"
							+ Double.toString(application
									.m2(application.orderDetail.get(postion).c_Spsj
											* application.orderDetail
													.get(postion).OrderAmcount)));
			assignView.edtxt_cardetail_shopGoodsCount
					.setText(Double.toString(application.orderDetail
							.get(postion).OrderAmcount));

			return view;
		}

		/**
		 * listview ��item�еĿؼ�����¼�
		 * 
		 * @author jeanbo
		 * 
		 */
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
				if (vId == assignView.btn_cardetail_clear.getId()) {

					// ����Ʒ����������0

					for (t_spflInfo spfl : application.goodsCategoryList) {
						boolean isExist = false;
						for (t_SpdaInfo spda : spfl.goodsList) {
							if (spda.c_Spbm.equals(application.orderDetail
									.get(postion).c_Spbm)) {
								spda.OrderAmcount = 0;
								isExist = true;
								break;
							}
						}
						if (isExist) {
							break;
						}
					}

					application.orderDetail.remove(postion);
					notifyDataSetChanged();
					PeiSongFeiDo();
					application.badge.setText(Integer.toString(application
							.GetOrderCount()));
					application.badge.show();
					if (application.GetOrderCount() == 0) {
						application.badge.hide();
					}
				}

				// ������
				if (vId == assignView.btn_cardetail_reduction.getId()) {
					if (application.orderDetail.get(postion).OrderAmcount > 1) {
						// ����Ʒ����������0

						for (t_spflInfo spfl : application.goodsCategoryList) {
							boolean isExist = false;
							for (t_SpdaInfo spda : spfl.goodsList) {
								if (spda.c_Spbm.equals(application.orderDetail
										.get(postion).c_Spbm)) {
									spda.OrderAmcount = spda.OrderAmcount - 1;
									isExist = true;
									break;
								}
							}
							if (isExist) {
								break;
							}
						}

						// application.orderDetail.get(postion).OrderAmcount -=
						// 1;
						notifyDataSetChanged();
						PeiSongFeiDo();
						application.badge.setText(Integer.toString(application
								.GetOrderCount()));
						application.badge.show();
					}
				}

				// ������
				if (vId == assignView.btn_cardetail_add.getId()) {
					// ����Ʒ����������0
					for (t_spflInfo spfl : application.goodsCategoryList) {
						boolean isExist = false;
						for (t_SpdaInfo spda : spfl.goodsList) {
							if (spda.c_Spbm.equals(application.orderDetail
									.get(postion).c_Spbm)) {
								spda.OrderAmcount += 1;
								isExist = true;
								break;
							}
						}
						if (isExist) {
							break;
						}
					}
					// application.orderDetail.get(postion).OrderAmcount += 1;
					notifyDataSetChanged();
					PeiSongFeiDo();
					application.badge.setText(Integer.toString(application
							.GetOrderCount()));
					application.badge.show();
				}
			}
		}
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

	class DetailView {
		ImageView btn_cardetail_clear;
		TextView txt_car_item_goodsName;
		TextView txt_car_item_goodsMoney;
		TextView edtxt_cardetail_shopGoodsCount;
		ImageView btn_cardetail_reduction;
		ImageView btn_cardetail_add;
	}
}
