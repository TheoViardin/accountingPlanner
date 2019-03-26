package miar.com.miar_project_client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import miar.com.miar_project_client.R;
import miar.com.miar_project_client.model.User;

public class AllUserAdapter extends ArrayAdapter {

	private int myItemLayout;
	private LayoutInflater li;
	private List<String> allUsers = new ArrayList<String>();
	private List<String> listChecked = new ArrayList<String>();

	public AllUserAdapter(Context context, int resourceId, List list, List list2) {
		//public MemberAdapter(Context context, int resourceId, List list) {
		super(context, resourceId);
		this.myItemLayout = resourceId;
		this.li = LayoutInflater.from(context);
		allUsers = list;
		listChecked = list2;
	}

	public void setList(List list, String s){
		list.add(s);
	}

	public void removeList(List list, String s){
		list.remove(s);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		LayoutInflater i = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = i.inflate(R.layout.participant_item, null);
		CheckBox c = (CheckBox) view.findViewById(R.id.participantCheck);
		TextView lenom = (TextView) view.findViewById(R.id.participantName);

		lenom.setText(allUsers.get(position));
		c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					setList(listChecked, allUsers.get(position));
					System.out.println("LIST CHECKED ADD : " + listChecked.get(0));
				} else {
					removeList(listChecked, allUsers.get(position));
					System.out.println("LIST CHECKED REMOVE : " + listChecked);
				}
			}
		});

		return view;
	}

}
