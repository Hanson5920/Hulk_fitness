package com.lxh.userlibrary.base;

import android.util.SparseArray;

import com.lxh.userlibrary.user.BindPhoneFragment;
import com.lxh.userlibrary.user.MineFragment;
import com.lxh.userlibrary.user.SetPasswordFragment;
import com.lxh.userlibrary.user.UpdateUserDataFragment;
import com.lxh.userlibrary.user.UserLoginFragment;
import com.lxh.userlibrary.user.UserRegisterFragment;


/**
 * Created with Android Studio.
 * <p/>
 * Author:xiaxf
 * <p/>
 * Date:2015/8/13.
 */
public class FragmentFactory {

	//用户中心
	public static final int FRAGMENT_TYPE_MINE = 0x01;//用户中心
	public static final int FRAGMENT_TYPE_USER_LOGIN = 0x05;//登录界面
	public static final int FRAGMENT_TYPE_USER_REGISTER = 0x06;//注册界面
	public static final int FRAGMENT_TYPE_USER_SET_PASSWORD = 0x07;//设置密码界面
	public static final int FRAGMENT_TYPE_USER_DATA_UPDATE = 0x08;//用户资料修改界面

	public static final int FRAGMENT_TYPE_BIRTHDAY_REMINDER = 0x09;//提醒设置界面
	public static final int FRAGMENT_TYPE_FEEDBACK = 0x10;//意见反馈界面
	public static final int FRAGMENT_TYPE_FOUND_APP = 0x11;//发现APP界面
	public static final int FRAGMENT_TYPE_BIND_PHONE = 0x12;//绑定手机

	private SparseArray<BaseFragment> mFragmentCache = new SparseArray<BaseFragment>();

	public BaseFragment getFragment(int type, boolean useCache) {

		BaseFragment fragment = null;

		if (useCache && (fragment = mFragmentCache.get(type)) != null) {
			return fragment;
		}
		switch (type) {
			case FragmentFactory.FRAGMENT_TYPE_USER_LOGIN:
				fragment = UserLoginFragment.newInstance();
				break;
			case FragmentFactory.FRAGMENT_TYPE_USER_REGISTER:
				fragment = UserRegisterFragment.newInstance();
				break;
			case FragmentFactory.FRAGMENT_TYPE_USER_SET_PASSWORD:
				fragment = SetPasswordFragment.newInstance();
				break;
			case FragmentFactory.FRAGMENT_TYPE_USER_DATA_UPDATE:
				fragment = UpdateUserDataFragment.newInstance();
				break ;
			case FragmentFactory.FRAGMENT_TYPE_MINE:
				fragment = MineFragment.newInstance();
				break;
//			case FragmentFactory.FRAGMENT_TYPE_FEEDBACK:
//				fragment = FeedbackFragment.newInstance();
//				break;
			case FragmentFactory.FRAGMENT_TYPE_BIND_PHONE:
				fragment = BindPhoneFragment.newInstance();
				break;
		}


		if (useCache) {
			mFragmentCache.put(type, fragment);
		}
		return fragment;
	}

	public void removeFragment(int type) {
		mFragmentCache.remove(type);
	}

	public BaseFragment getFragmentFromCache(int type) {
		return mFragmentCache.get(type);
	}

	public void clearCache() {
		mFragmentCache.clear();
	}

}
