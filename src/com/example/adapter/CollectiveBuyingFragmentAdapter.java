package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.model.CollectiveBuying;
import com.example.taomeiren.MainActivity;
import com.example.taomeiren.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CollectiveBuyingFragmentAdapter extends BaseAdapter
{
	private List<CollectiveBuying> lists;
	private Context mContext;
	private ImageLoaderConfiguration configuration;
	
	public CollectiveBuyingFragmentAdapter(Context context)
	{
		this.mContext=context;
		this.lists=new ArrayList<>();
		this.configuration=new ImageLoaderConfiguration.Builder(mContext).build();
		ImageLoader.getInstance().init(configuration);
	}
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return lists.get(position);
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
		Holder holder;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(R.layout.collective_buying_fragment_listview_item, null);
			holder=new Holder();
			holder.img=(ImageView) convertView.findViewById(R.id.collective_item_img);
			holder.name=(TextView) convertView.findViewById(R.id.collective_item_name);
			holder.price=(TextView) convertView.findViewById(R.id.collective_item_price);
			holder.collectiveprice=(TextView) convertView.findViewById(R.id.collective_item_collectiveprice);
			holder.description=(TextView) convertView.findViewById(R.id.collective_item_description);
			convertView.setTag(holder);
		}else {
			holder=(Holder) convertView.getTag();
		}
		holder.name.setText(lists.get(position).commodity.name);
		holder.price.setText("原价:"+lists.get(position).commodity.price+"￥");
		holder.collectiveprice.setText("团购价:"+lists.get(position).collectivePrice+"￥");
		holder.description.setText(lists.get(position).commodity.description);
		ImageLoader.getInstance().displayImage(MainActivity.IP+"images/"+lists.get(position).commodity.imageURL, holder.img);
		return convertView;
	}
	
	private class Holder
	{
		public ImageView img;
		public TextView name,price,collectiveprice,description;
	}

	public void addData(List<CollectiveBuying> list)
	{
		lists.addAll(list);
		notifyDataSetChanged();
	}
	
	public void updateData(int position, CollectiveBuying collectiveBuying)
	{
		CollectiveBuying item=lists.get(position);
		item.commodity.stock=collectiveBuying.commodity.stock;
		item.currentNum=collectiveBuying.currentNum;
		item.mailOfparticipants=collectiveBuying.mailOfparticipants;
	}

}
