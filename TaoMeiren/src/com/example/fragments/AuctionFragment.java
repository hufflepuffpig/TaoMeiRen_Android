package com.example.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.adapter.AuctionFragmentAdapter;
import com.example.model.Auction;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AuctionFragment extends Fragment
{
	private PullToRefreshListView listView;
	private AuctionFragmentAdapter myAdapter;
	private AsyncHttpClient client;
	private AuctionReceiver receiver;
	private int mCurLoadAuctionNum=0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.auction_fragment_main, null);
		client=new AsyncHttpClient();
		receiver=new AuctionReceiver();
		IntentFilter filter=new IntentFilter(GlobalValues.UPDATE_AUCTION);
		getActivity().registerReceiver(receiver, filter);
		
		
		listView=(PullToRefreshListView) view.findViewById(R.id.auction_fragment_list);
		myAdapter=new AuctionFragmentAdapter(getActivity());
		listView.setAdapter(myAdapter);
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
		{

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView)
			{
				getMoreAuction();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),ShowCommodityActivity.class);
				intent.putExtra("from", GlobalValues.FROM_AUCTION);
				intent.putExtra("auction", (Auction)myAdapter.getItem(position-1));
				intent.putExtra("position", position-1);
				startActivity(intent);
			}
		});
		getMoreAuction();
		return view;
	}
	
	public void getMoreAuction()
	{
		client.get(MainActivity.IP+"forAuctionFragment.php", new RequestParams("from", ""+mCurLoadAuctionNum), new JsonHttpResponseHandler()
		{

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response)
			{
				Log.e("Lin", "here");
				List<Auction> ret=new ArrayList<>();
				for(int i=0;i<response.length();i++)
				{
					try
					{
						JSONObject object=response.getJSONObject(i);
						Auction node=new Auction();
						node.commodity.commodityID=object.getInt("commodityID");
						node.commodity.imageURL=object.getString("imageURL");
						node.commodity.mailOfseller=object.getString("mailOfseller");
						node.commodity.price=object.getDouble("price");
						node.commodity.description=object.getString("description");
						node.commodity.name=object.getString("name");
						node.commodity.stock=object.getInt("stock");
						node.commodity.dealType=object.getInt("dealType");
						node.commodity.commodityCredit=object.getDouble("commodityCredit");
						
						node.ID=object.getInt("ID");
						node.deadLine=object.getString("deadLine");
						node.activePrice=object.getDouble("activePrice");
						node.currentPrice=object.getDouble("currentPrice");
						node.currentMail=object.getString("currentMail");
						node.mailOfparticipants=object.getString("mailOfparticipants");
						ret.add(node);
						
					} catch (JSONException e){e.printStackTrace();}
	
				}
				
				myAdapter.addData(ret);
				mCurLoadAuctionNum+=response.length();
				listView.onRefreshComplete();
			}
			
		});
	}

	private class AuctionReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			int position=intent.getIntExtra("position", 0);
			Auction auction=(Auction) intent.getSerializableExtra("auction");
			myAdapter.updateData(position, auction);
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
