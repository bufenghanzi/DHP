/**
 * 
 */
package com.mingseal.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mingseal.dhp.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author 商炎炳
 * @description 登录界面Spinner的自定义
 */
public class LoginSpinnerAdapter extends BaseAdapter {

	private String[] admins;
	private ViewHolder holder;
	private LayoutInflater mInflater;

	/**
	 * 初始化适配器
	 * 
	 * @param context
	 */
	public LoginSpinnerAdapter(Context context) {
		super();
		this.mInflater = LayoutInflater.from(context);
	}

	/**
	 * Activity设置初值
	 * 
	 * @param admins
	 */
	public void setAdmins(String[] admins) {
		this.admins = admins;
	}

	@Override
	public int getCount() {
		return admins.length;
	}

	@Override
	public String getItem(int position) {
		return admins[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
//			convertView = mInflater.inflate(R.layout.item_login_spinner, null);
			convertView = mInflater.inflate(R.layout.item_login_spinner,parent,false);
			holder.tv_num = (TextView) convertView.findViewById(R.id.item_admin);

			convertView.setTag(holder);
			//对于listview，注意添加这一行，即可在item上使用高度
			AutoUtils.autoSize(convertView);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (admins != null && admins.length != 0) {
			holder.tv_num.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize(55));
			holder.tv_num.setText(getItem(position));
		}

		return convertView;
	}

	private class ViewHolder {
		private TextView tv_num;// 方案号
	}

}
