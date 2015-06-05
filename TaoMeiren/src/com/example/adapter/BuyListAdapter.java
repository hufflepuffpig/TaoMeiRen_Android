package com.example.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import org.apache.http.Header;

import com.example.model.BuyListNode;
import com.example.taomeiren.MainActivity;
import com.example.taomeiren.R;
import com.example.utils.GlobalValues;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BuyListAdapter extends BaseAdapter
{
	private List<BuyListNode> lists;
	private Context mContext;
	private AsyncHttpClient client;
	private String currentDate;
	
	public BuyListAdapter(Context context)
	{
		this.mContext=context;
		this.lists=new ArrayList<>();
		this.client=new AsyncHttpClient();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		currentDate=format.format(new Date(System.currentTimeMillis()));
		Log.e("Time", currentDate);
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
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		Holder holder;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(R.layout.personalcenter_buylist_item, null);
			holder=new Holder();
			holder.dealType=(TextView) convertView.findViewById(R.id.buylist_item_dealType);
			holder.mailOfseller=(TextView) convertView.findViewById(R.id.buylist_item_mailOfseller);
			holder.name=(TextView) convertView.findViewById(R.id.buylist_item_name);
			holder.itemNum=(TextView) convertView.findViewById(R.id.buylist_item_itemNum);
			holder.deposit=(TextView) convertView.findViewById(R.id.buylist_item_deposit);
			holder.OKbtn=(Button) convertView.findViewById(R.id.buylist_item_OKbtn);
			holder.cancelbtn=(Button) convertView.findViewById(R.id.buylist_item_Cancelbtn);
			convertView.setTag(holder);
		}else {
			holder=(Holder) convertView.getTag();
		}
		if(lists.get(position).dealType==1)
			holder.dealType.setText("一口价");
		else if(lists.get(position).dealType==2)
			holder.dealType.setText("团购");
		else if(lists.get(position).dealType==3)
			holder.dealType.setText("竞拍");
		holder.mailOfseller.setText(lists.get(position).mailOfseller);
		holder.name.setText(lists.get(position).name);
		holder.itemNum.setText(lists.get(position).itemNum+"");
		holder.deposit.setText(lists.get(position).deposit+"");
		
		holder.OKbtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(lists.get(position).dealType==1)
				{
					dealWithOnePrice_Confirm(lists.get(position).orderID);
					lists.remove(position);
					notifyDataSetChanged();
				}else if(lists.get(position).dealType==2)
				{
					dealWithCollective_Confirm(lists.get(position).orderID);
					lists.remove(position);
					notifyDataSetChanged();
				}else if(lists.get(position).dealType==3)
				{
					dealWithAuction_Confirm(lists.get(position).orderID);
					lists.remove(position);
					notifyDataSetChanged();
				}
			}
		});
		holder.cancelbtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(lists.get(position).dealType==1)
				{
					dealWithOnePrice_Cancel(lists.get(position).orderID);
					lists.remove(position);
					notifyDataSetChanged();
				}else if(lists.get(position).dealType==2)
				{
					dealWithCollective_Cancel(lists.get(position).orderID);
					lists.remove(position);
					notifyDataSetChanged();
				}else if(lists.get(position).dealType==3)
				{
					Toast.makeText(mContext, "竞投之后不能放弃", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		return convertView;
	}
	private class Holder
	{
		TextView dealType,mailOfseller,name,itemNum,deposit;
		Button OKbtn,cancelbtn;
	}
	public void setData(List<BuyListNode> nodes)
	{
		lists=nodes;
		notifyDataSetChanged();
	}

	public void dealWithOnePrice_Confirm(int orderID)
	{
		client.get(MainActivity.IP+"confirmBuyOnePrice.php", new RequestParams("orderID", orderID), new AsyncHttpResponseHandler()
		{
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				Intent intent=new Intent(GlobalValues.UPDATE_PERSONAL);
				mContext.sendBroadcast(intent);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
				// TODO Auto-generated method stub
				Log.e("Lin", "failed");
			}
		});
	}
	public void dealWithOnePrice_Cancel(int orderID)
	{
		client.get(MainActivity.IP+"cancelBuyOnePrice.php", new RequestParams("orderID", orderID), new AsyncHttpResponseHandler()
		{
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				// TODO Auto-generated method stub
				Log.e("LinSucess", new String(arg2));
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void dealWithCollective_Confirm(int orderID)
	{
		client.get(MainActivity.IP+"confirmBuyCollective.php", new RequestParams("orderID", orderID), new AsyncHttpResponseHandler()
		{
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(GlobalValues.UPDATE_PERSONAL);
				mContext.sendBroadcast(intent);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void dealWithCollective_Cancel(int orderID)
	{
		client.get(MainActivity.IP+"cancelBuyCollective.php", new RequestParams("orderID", orderID), new AsyncHttpResponseHandler()
		{
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void dealWithAuction_Confirm(int orderID)
	{
		client.get(MainActivity.IP+"confirmBuyAuction.php", new RequestParams("orderID", orderID), new AsyncHttpResponseHandler()
		{
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				// TODO Auto-generated method stub
				Log.e("Lin", new String(arg2));
				if(new String(arg2).equals("2"))
				{
					Toast.makeText(mContext, "你不是当前竞拍的最高价得主", Toast.LENGTH_LONG).show();
				}else {
					Intent intent=new Intent(GlobalValues.UPDATE_PERSONAL);
					mContext.sendBroadcast(intent);
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}

}
