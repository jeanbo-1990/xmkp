package com.example.bespeak;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.example.bespeak.common.ProjectApplication;
import com.example.car.CarAcitvity;
import com.example.my.MyAcitvity;
import com.example.preferential.PreferentialAcitvity;
import com.example.view.ViewAcitvity;

public class OperateActivity extends TabActivity implements OnClickListener {

	public static OperateActivity operateActivity;
	// Animation(tab切换动画)
	private Animation left_in, left_out;
	private Animation right_in, right_out;

	// 页面跳转intent
	Intent viewIntent, preferentialIntent, carIntent, myIntent;

	public static TabHost mTabHost;

	public static String TAB_TAG_VIEW = "view";
	public static String TAB_TAG_PREFERENTIAL = "preferential";
	public static String TAB_TAG_SEARCH = "Car";
	public static String TAB_TAG_HELP = "My";

	ImageView ivView, ivPreferential, ivCar, ivMy;
	TextView txtView, txtPreferential, txtCar, txtMy;
	public View operate_menu_view, operate_menu_preferential, operate_menu_car,
			operate_menu_my;

	int mCurTabId = R.id.operate_menu_view;

	// 子图颜色
	static final int COLOR_NONE = Color.parseColor("#bfbdbe");
	static final int COLOR_DOWN = Color.parseColor("#ff7201");

	private long exitTime = 0;

	/**
	 * 全局类
	 */
	ProjectApplication application;

	/**
	 * 主页
	 */
	ImageView top_btn_home;

	/**
	 * 分享
	 */
	ImageView top_btn_other;

	RelativeLayout rl_top;

	View mPopupWindowView;
	PopupWindow popupWindow;
	View v_line;

	private FrontiaSocialShare mSocialShare;

	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operate);
		rl_top = (RelativeLayout) findViewById(R.id.rl_top);
		application = (ProjectApplication) getApplication();
		top_btn_home = (ImageView) findViewById(R.id.top_btn_home);
		top_btn_home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				operate_menu_view.performClick();
			}
		});
		InitPupWindows();
		top_btn_other = (ImageView) findViewById(R.id.top_btn_other);
		top_btn_other.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!popupWindow.isShowing()) {
					popupWindow.showAsDropDown(rl_top,OperateActivity.this.getWindowManager().getDefaultDisplay().getWidth()*2/3
							, 0);
				} else {
					popupWindow.dismiss();
				}
			}
		});
		operateActivity = this;
		prepareAnim();
		prepareIntent();
		setupIntent();
		prepareView();
		mTabHost.setCurrentTabByTag(TAB_TAG_VIEW);
		
		// 调用前，必须初始化
		Frontia.init(this.getApplicationContext(), Conf.APIKEY);
		mSocialShare = Frontia.getSocialShare();
		mSocialShare.setContext(this);
		mSocialShare.setClientId(MediaType.SINAWEIBO.toString(),
				Conf.SINA_APP_KEY);
		mSocialShare.setClientId(MediaType.QZONE.toString(), "100358052");
		mSocialShare.setClientId(MediaType.QQFRIEND.toString(), "100358052");
		// 自己应用的名称
		mSocialShare.setClientName(MediaType.QQFRIEND.toString(), "分享");
		mSocialShare.setClientId(MediaType.WEIXIN.toString(),
				"wx329c742cb69b41b8");
		// 标题
		mImageContent.setTitle("《小马快跑》Android客户端");
		// 描述
		mImageContent.setContent("小马快跑订餐app分享");
		// 链接地址
		mImageContent.setLinkUrl("http://218.93.51.170:8001/downLoadAndroidApp.html");
		// 显示图片
		mImageContent
				.setImageUri(Uri
						.parse("http://img.my.csdn.net/uploads/201412/09/1418131584_3559.png"));
	}
	
	private void InitPupWindows(){
		mPopupWindowView = LayoutInflater.from(OperateActivity.this).inflate(
				R.layout.other_pup, null);
		LinearLayout top_menu_share=(LinearLayout)mPopupWindowView.findViewById(R.id.top_menu_share);
		top_menu_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSocialShare.show(OperateActivity.this.getWindow().getDecorView(), mImageContent, FrontiaTheme.DARK,  new ShareListener());
				popupWindow.dismiss();
			}
		});
		LinearLayout top_menu_myself=(LinearLayout)mPopupWindowView.findViewById(R.id.top_menu_myself);
		top_menu_myself.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentMySelf=new Intent(OperateActivity.this,AboutUsActivity.class);
				startActivity(intentMySelf);
				popupWindow.dismiss();
			}
		});
		
		popupWindow = new PopupWindow(mPopupWindowView,
				OperateActivity.this.getWindowManager().getDefaultDisplay().getWidth()*2/5, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		//popupWindow.setBackgroundDrawable(new PaintDrawable());
		popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.other_function_style));
    	v_line=mPopupWindowView.findViewById(R.id.v_line);
	}

	private class ShareListener implements FrontiaSocialShareListener {

		@Override
		public void onSuccess() {
			Log.d("Test", "share success");
		}

		@Override
		public void onFailure(int errCode, String errMsg) {
			Log.d("Test", "share errCode " + errCode);
		}

		@Override
		public void onCancel() {
			Log.d("Test", "cancel ");
		}
	}

	/**
	 * 初始化动画
	 */
	private void prepareAnim() {
		left_in = AnimationUtils.loadAnimation(this, R.anim.left_in);
		left_out = AnimationUtils.loadAnimation(this, R.anim.left_out);

		right_in = AnimationUtils.loadAnimation(this, R.anim.right_in);
		right_out = AnimationUtils.loadAnimation(this, R.anim.right_out);
	}

	/**
	 * 初始化页面跳转Intent
	 */
	private void prepareIntent() {

		viewIntent = new Intent(this, ViewAcitvity.class);
		preferentialIntent = new Intent(this, PreferentialAcitvity.class);
		carIntent = new Intent(this, CarAcitvity.class);
		myIntent = new Intent(this, MyAcitvity.class);
	}

	private void setupIntent() {
		mTabHost = getTabHost();
		mTabHost.addTab(buildTabSpec(TAB_TAG_VIEW, R.string.operate_txt_view,
				R.drawable.operate_img_view, viewIntent));
		mTabHost.addTab(buildTabSpec(TAB_TAG_PREFERENTIAL,
				R.string.operate_txt_preferential,
				R.drawable.operate_img_preferential, preferentialIntent));
		mTabHost.addTab(buildTabSpec(TAB_TAG_SEARCH, R.string.operate_txt_car,
				R.drawable.operate_img_car, carIntent));
		mTabHost.addTab(buildTabSpec(TAB_TAG_HELP,
				R.string.title_activity_help, R.drawable.operate_img_my,
				myIntent));
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

	private void prepareView() {
		ivView = (ImageView) findViewById(R.id.operate_img_view);
		ivPreferential = (ImageView) findViewById(R.id.operate_img_preferential);
		ivCar = (ImageView) findViewById(R.id.operate_img_car);
		ivMy = (ImageView) findViewById(R.id.operate_img_my);
		operate_menu_view = (View) findViewById(R.id.operate_menu_view);
		operate_menu_preferential = (View) findViewById(R.id.operate_menu_preferential);
		operate_menu_car = (View) findViewById(R.id.operate_menu_car);
		operate_menu_my = (View) findViewById(R.id.operate_menu_my);
		operate_menu_view.setOnClickListener(this);
		operate_menu_preferential.setOnClickListener(this);
		operate_menu_car.setOnClickListener(this);
		operate_menu_my.setOnClickListener(this);
		txtView = (TextView) findViewById(R.id.operate_txt_view);
		txtPreferential = (TextView) findViewById(R.id.operate_txt_preferential);
		txtCar = (TextView) findViewById(R.id.operate_txt_car);
		txtMy = (TextView) findViewById(R.id.operate_txt_help);
	}

	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,
			final Intent content) {
		return mTabHost
				.newTabSpec(tag)
				.setIndicator(getString(resLabel),
						getResources().getDrawable(resIcon))
				.setContent(content);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mCurTabId == v.getId()) {
			return;
		}
		if (v.getId() == R.id.operate_menu_car
				&& (application.hyda == null || application.hyda.isLogin == 0)) {
			Intent intentLogin = new Intent(OperateActivity.this,
					LoginActivity.class);
			startActivity(intentLogin);
			return;
		}
		if (v.getId() == R.id.operate_menu_my
				&& (application.hyda == null || application.hyda.isLogin == 0)) {
			Intent intentLogin = new Intent(OperateActivity.this,
					LoginActivity.class);
			startActivity(intentLogin);
			return;
		}
		ivView.setImageResource(R.drawable.operate_img_view);
		ivPreferential.setImageResource(R.drawable.operate_img_preferential);
		ivCar.setImageResource(R.drawable.operate_img_car);
		ivMy.setImageResource(R.drawable.operate_img_my);
		txtView.setTextColor(COLOR_NONE);
		txtPreferential.setTextColor(COLOR_NONE);
		txtCar.setTextColor(COLOR_NONE);
		txtMy.setTextColor(COLOR_NONE);
		int checkedId = v.getId();
		final boolean o;
		if (mCurTabId < checkedId)
			o = true;
		else
			o = false;
		if (o)
			mTabHost.getCurrentView().startAnimation(left_out);
		else
			mTabHost.getCurrentView().startAnimation(right_out);
		switch (checkedId) {
		case R.id.operate_menu_view:
			mTabHost.setCurrentTabByTag(TAB_TAG_VIEW);
			ivView.setImageResource(R.drawable.operate_img_view_down);
			txtView.setTextColor(COLOR_DOWN);
			break;
		case R.id.operate_menu_preferential:
			mTabHost.setCurrentTabByTag(TAB_TAG_PREFERENTIAL);
			ivPreferential
					.setImageResource(R.drawable.operate_img_preferential_down);
			txtPreferential.setTextColor(COLOR_DOWN);
			break;
		case R.id.operate_menu_car:

			mTabHost.setCurrentTabByTag(TAB_TAG_SEARCH);
			ivCar.setImageResource(R.drawable.operate_img_car_down);
			txtCar.setTextColor(COLOR_DOWN);
			break;
		case R.id.operate_menu_my:

			mTabHost.setCurrentTabByTag(TAB_TAG_HELP);
			ivMy.setImageResource(R.drawable.operate_img_my_down);
			txtMy.setTextColor(COLOR_DOWN);
			break;
		default:
			break;
		}

		if (o)
			mTabHost.getCurrentView().startAnimation(left_in);
		else
			mTabHost.getCurrentView().startAnimation(right_in);
		mCurTabId = checkedId;
	}

}
