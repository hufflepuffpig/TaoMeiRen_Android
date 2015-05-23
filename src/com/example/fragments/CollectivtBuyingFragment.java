package com.example.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.adapter.CollectiveBuyingFragmentAdapter;
import com.example.model.CollectiveBuying;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class CollectivtBuyingFragment extends Fragment
{
	private PullToRefreshListView listView;
	private CollectiveBuyingFragmentAdapter myAdapter;
	private int mCurLoadCollectiveCommodityNum=0;
	private AsyncHttpClient client;
	private CollectiveReceiver receiver;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.collective_buying_fragment_main, null);
		client=new AsyncHttpClient();
		receiver=new CollectiveReceiver();
		IntentFilter filter=new IntentFilter(GlobalValues.UPDATE_COLLECTIVE);
		getActivity().registerReceiver(receiver, filter);
		
		
		listView=(PullToRefreshListView) view.findViewById(R.id.collective_fragment_list);
		myAdapter=new CollectiveBuyingFragmentAdapter(getActivity());
		listView.setAdapter(myAdapter);
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()
		{

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView)
			{
				// TODO Auto-generated method stub
				getMoreCollectiveBuying();
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),ShowCommodityActivity.class);
				intent.putExtra("from", GlobalValues.FROM_COLLECTIVE_BUYING);
				intent.putExtra("collectivebuying", (CollectiveBuying)myAdapter.getItem(position-1));
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
		getMoreCollectiveBuying();
	}

	
	public void getMoreCollectiveBuying()
	{
		client.get(MainActivity.IP+"forCollectiveFragment.php", new RequestParams("from", ""+mCurLoadCollectiveCommodityNum), new JsonHttpResponseHandler()
		{

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response)
			{
				List<CollectiveBuying> ret=new ArrayList<>();
				for(int i=0;i<response.length();i++)
				{
					try
					{
						JSONObject object=response.getJSONObject(i);
						CollectiveBuying node=new CollectiveBuying();
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
						node.activeNum=object.getInt("activeNum");
						node.collectivePrice=object.getDouble("collectivePrice");
						node.mailOfparticipants=object.getString("mailOfparticipants");
						node.currentNum=object.getInt("currentNum");
						ret.add(node);
					} catch (JSONException e){e.printStackTrace();}
	
				}
				myAdapter.addData(ret);
				mCurLoadCollectiveCommodityNum+=response.length();
				listView.onRefreshComplete();
			}
			
		});
	}


	private class CollectiveReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			int position=intent.getIntExtra("position", 0);
			CollectiveBuying collectiveBuying=(CollectiveBuying) intent.getSerializableExtra("collectivebuying");
			myAdapter.updateData(position, collectiveBuying);
			
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
