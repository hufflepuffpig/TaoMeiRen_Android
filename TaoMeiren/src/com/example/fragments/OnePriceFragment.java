package com.example.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.adapter.OnePriceFragmentAdapter;
import com.example.model.Commodity;
import com.example.taomeiren.MainActivity;
import com.example.taomeiren.R;
import com.example.taomeiren.ShowCommodityActivity;
import com.example.utils.GlobalValues;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OnePriceFragment extends Fragment
{
	private PullToRefreshListView listView;
	private OnePriceFragmentAdapter myAdapter;
	private AsyncHttpClient client;
	private int mCurLoadCommodityNum=0;
	
	private OnePriceReceiver receiver;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.oneprice_fragment_main, null);
		listView=(PullToRefreshListView) view.findViewById(R.id.oneprice_fragment_list);
		myAdapter=new OnePriceFragmentAdapter(getActivity());
		client=new AsyncHttpClient();
		
		receiver=new OnePriceReceiver();
		IntentFilter filter=new IntentFilter(GlobalValues.UPDATE_ONEPRICE);
		getActivity().registerReceiver(receiver, filter);
		
		
		listView.setAdapter(myAdapter);
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
		{

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView)
			{
				// TODO Auto-generated method stub
				getMoreCommodity();
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent=new Intent(getActivity(),ShowCommodityActivity.class);
				intent.putExtra("from", GlobalValues.FROM_COMMODITY);
				intent.putExtra("commodity", (Commodity)myAdapter.getItem(position-1));
				intent.putExtra("position", position-1);
				startActivity(intent);
			}
		});
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getMoreCommodity();
	}
	
	public void getMoreCommodity()
	{
		
			client.get(MainActivity.IP+"forOnePriceFragment.php", new RequestParams("from", ""+mCurLoadCommodityNum), new JsonHttpResponseHandler()
			{
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONArray response)
				{
					List<Commodity> ret=new ArrayList<>();
					for(int i=0;i<response.length();i++)
					{
						try{
						JSONObject object=response.getJSONObject(i);
						Commodity commodity=new Commodity();
						commodity.commodityID=object.getInt("commodityID");
						commodity.imageURL=object.getString("imageURL");
						commodity.mailOfseller=object.getString("mailOfseller");
						commodity.price=object.getDouble("price");
						commodity.description=object.getString("description");
						commodity.name=object.getString("name");
						commodity.stock=object.getInt("stock");
						commodity.dealType=object.getInt("dealType");
						commodity.commodityCredit=object.getDouble("commodityCredit");
						ret.add(commodity);
						}catch(Exception e){}	
					}
					mCurLoadCommodityNum+=response.length();
					Log.e("Lin", mCurLoadCommodityNum+"");
					myAdapter.addData(ret);
					
					listView.onRefreshComplete();
				}
				
			});
		
	}
		

	private class OnePriceReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			// TODO Auto-generated method stub
			int position=intent.getIntExtra("position", 0);
			Commodity updateCommodity=(Commodity) intent.getSerializableExtra("commodity");
			myAdapter.updateData(updateCommodity, position);
		}
		
	}


	
	
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(receiver);
	}
}
