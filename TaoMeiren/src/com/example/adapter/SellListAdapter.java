package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.example.model.SellListNode;
import com.example.taomeiren.MainActivity;
import com.example.taomeiren.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SellListAdapter extends BaseAdapter
{
	private List<SellListNode> lists;
	private Context mContext;
	private AsyncHttpClient client;
	
	public SellListAdapter(Context context)
	{
		this.mContext=context;
		this.lists=new ArrayList<>();
		this.client=new AsyncHttpClient();
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.personalcenter_selllist_item, null);
			holder=new Holder();
			holder.mailOfBuyer=(TextView) convertView.findViewById(R.id.selllist_item_mailOfBuyer);
			holder.dealType=(TextView) convertView.findViewById(R.id.selllist_item_dealType);
			holder.name=(TextView) convertView.findViewById(R.id.selllist_item_name);
			holder.itemNum=(TextView) convertView.findViewById(R.id.selllist_item_itemNum);
			holder.deposit=(TextView) convertView.findViewById(R.id.selllist_item_deposit);
			holder.btn=(Button) convertView.findViewById(R.id.selllist_item_btn);
			convertView.setTag(holder);
		}else {
			holder=(Holder) convertView.getTag();
		}
		holder.mailOfBuyer.setText(lists.get(position).mailOfbuyer);
		if(lists.get(position).dealType==1)
			holder.dealType.setText("一口价");
		else if(lists.get(position).dealType==2)
			holder.dealType.setText("团购");
		else if(lists.get(position).dealType==3)
			holder.dealType.setText("竞拍");
		holder.name.setText(lists.get(position).name);
		holder.itemNum.setText(lists.get(position).itemNum+"");
		holder.deposit.setText(lists.get(position).deposit+"");
		
		holder.btn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(lists.get(position).dealType==2)
				{
					dealWithSellCollective(lists.get(position).orderID);
				}else if(lists.get(position).dealType==3)
				{
					dealWithSellAuction(lists.get(position).orderID);
				}
				
			}
		});
		
		return convertView;
	}
	
	private class Holder
	{
		TextView mailOfBuyer,dealType,name,itemNum,deposit;
		Button btn;
	}

	public void setData(List<SellListNode> nodes)
	{
		lists=nodes;
		notifyDataSetChanged();
	}

	public void dealWithSellCollective(final int orderID)
	{
		client.get(MainActivity.IP+"checkSellCollective.php", new RequestParams("orderID", orderID), new AsyncHttpResponseHandler()
		{
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				// TODO Auto-generated method stub
				if(new String(arg2).equals("1"))
				{
					Toast.makeText(mContext, "已发货,请耐心等待顾客确认收货", Toast.LENGTH_SHORT).show();
				}else if(new String(arg2).equals("2"))
				{
					Toast.makeText(mContext, "团购过期且没达到团购标准,现已删除相关登记信息", Toast.LENGTH_SHORT).show();
					for(int i=0;i<lists.size();i++)
					{
						if(lists.get(i).orderID==orderID)
						{
							lists.remove(i);
						}
					}
					notifyDataSetChanged();
				}else if(new String(arg2).equals("3"))
				{
					Toast.makeText(mContext, "团购还没到截止时间", Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}
	public void dealWithSellAuction(final int orderID)
	{
		client.get(MainActivity.IP+"checkSellAuction.php", new RequestParams("orderID", orderID), new AsyncHttpResponseHandler()
		{
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
			{
				// TODO Auto-generated method stub
				if(new String(arg2).equals("1"))
				{
					Toast.makeText(mContext, "已发货,请耐心等待买家确认收货", Toast.LENGTH_LONG).show();
				}else if(new String(arg2).equals("2"))
				{
					Toast.makeText(mContext, "竞拍还没到截止时间", Toast.LENGTH_LONG).show();
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
