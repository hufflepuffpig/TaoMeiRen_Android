package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Auction;
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

public class AuctionFragmentAdapter extends BaseAdapter
{
	private List<Auction> lists;
	private Context mContext;
	private ImageLoaderConfiguration configuration;
	
	public AuctionFragmentAdapter(Context context)
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
		Holder holder;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(R.layout.auction_fragment_listview_item, null);
			holder=new Holder();
			holder.img=(ImageView) convertView.findViewById(R.id.auction_item_img);
			holder.name=(TextView) convertView.findViewById(R.id.auction_item_name);
			holder.activtprice=(TextView) convertView.findViewById(R.id.auction_item_activeprice);
			holder.currentprice=(TextView) convertView.findViewById(R.id.auction_item_currentprice);
			holder.description=(TextView) convertView.findViewById(R.id.auction_item_description);
			convertView.setTag(holder);
		}else {
			holder=(Holder) convertView.getTag();
		}
		holder.name.setText(lists.get(position).commodity.name);
		holder.activtprice.setText("底價:"+lists.get(position).activePrice);
		holder.description.setText(lists.get(position).commodity.description);
		if(lists.get(position).currentMail.equals("null"))
		{
			holder.currentprice.setText("暫無人競投");
		}else {
			holder.currentprice.setText("目前最高價:"+lists.get(position).currentPrice+"￥");
		}
		ImageLoader.getInstance().displayImage(MainActivity.IP+"images/"+lists.get(position).commodity.imageURL, holder.img);
		
		return convertView;
	}
	
	private class Holder
	{
		public ImageView img;
		public TextView name,activtprice,currentprice,description;
	}

	public void addData(List<Auction> list)
	{
		lists.addAll(list);
		notifyDataSetChanged();
	}

	public void updateData(int position,Auction auction)
	{
		Auction item=lists.get(position);
		item.currentPrice=auction.currentPrice;
		item.currentMail=auction.currentMail;
		item.mailOfparticipants=auction.mailOfparticipants;
		notifyDataSetChanged();
	}
}
