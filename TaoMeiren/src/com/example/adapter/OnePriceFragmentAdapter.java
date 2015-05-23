package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.Commodity;
import com.example.taomeiren.MainActivity;
import com.example.taomeiren.R;
import com.example.utils.AsyncBitmapLoader;
import com.example.utils.AsyncBitmapLoader.ImageCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class OnePriceFragmentAdapter extends BaseAdapter
{
	private List<Commodity> commodities;
	private Context mContext;
	private ImageLoaderConfiguration configuration;
	
	public OnePriceFragmentAdapter(Context context)
	{
		this.mContext=context;
		this.commodities=new ArrayList<>();
		this.configuration=new ImageLoaderConfiguration.Builder(mContext).build();
		ImageLoader.getInstance().init(configuration);
	}
		
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
		final Holder holder;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(R.layout.oneprice_fragment_listview_item,null);
			holder=new Holder();
			holder.img=(ImageView) convertView.findViewById(R.id.oneprice_item_img);
			holder.name=(TextView) convertView.findViewById(R.id.oneprice_item_name);
			holder.price=(TextView) convertView.findViewById(R.id.oneprice_item_price);
			holder.description=(TextView) convertView.findViewById(R.id.oneprice_item_description);
			convertView.setTag(holder);
		}else {
			holder=(Holder) convertView.getTag();
		}
		holder.name.setText(commodities.get(position).name);
		holder.price.setText(commodities.get(position).price+"￥");
		holder.description.setText(commodities.get(position).description);
		holder.img.setImageBitmap(null);
		ImageLoader.getInstance().displayImage(MainActivity.IP+"images/"+commodities.get(position).imageURL, holder.img);
		
		return convertView;
	}
	private class Holder
	{
		public ImageView img;
		public TextView name,price,description;
	}
	
	public void addData(List<Commodity> list)
	{
		commodities.addAll(list);
		notifyDataSetChanged();
	}

	public void updateData(Commodity updateCommodity,int position)
	{
		if(updateCommodity.stock>0)
			commodities.get(position).stock=updateCommodity.stock;
		else {
			commodities.remove(position);
		}
	}
}

