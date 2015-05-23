package com.example.adapter;

import java.util.List;

import com.example.model.Commodity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PersonalCenterSellAdapter extends BaseAdapter
{
	private List<Commodity> commodities;
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return commodities.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return commodities.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
