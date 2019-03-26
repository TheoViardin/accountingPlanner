package miar.com.miar_project_client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import miar.com.miar_project_client.R;

public class MemberAdapter extends ArrayAdapter {

	private int myItemLayout;
	private LayoutInflater li;
	private List<String> users = new ArrayList<String>();

	public MemberAdapter(Context context, int resourceId, List list) {
		//public MemberAdapter(Context context, int resourceId, List list) {
		super(context, resourceId);
		this.myItemLayout = resourceId;
		this.li = LayoutInflater.from(context);
		users = list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		LayoutInflater i = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = i.inflate(R.layout.user_item, null);
		TextView lenom = (TextView) view.findViewById(R.id.userName);
		//lenom.setText(users.get(position).pseudo);
		lenom.setText(users.get(position));
		return view;
	}


}
