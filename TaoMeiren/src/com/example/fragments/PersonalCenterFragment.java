package com.example.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.adapter.BuyListAdapter;
import com.example.adapter.SellListAdapter;
import com.example.model.BuyListNode;
import com.example.model.SellListNode;
import com.example.taomeiren.MainActivity;
import com.example.taomeiren.R;
import com.example.taomeiren.SellActivity;
import com.example.utils.GlobalValues;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalCenterFragment extends Fragment
{
	private SharedPreferences sharedPreferences;
	private View view;
	private ListView sell_listview,buy_listview;
	private SellListAdapter sellListAdapter;
	private BuyListAdapter buyListAdapter;
	private AsyncHttpClient client=new AsyncHttpClient();
	private PersonalCenterReceiver receiver;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		sharedPreferences=getActivity().getSharedPreferences("data", getActivity().MODE_PRIVATE);
		view=inflater.inflate(R.layout.personal_center_fragment_main, null);
		receiver=new PersonalCenterReceiver();
		IntentFilter filter=new IntentFilter(GlobalValues.UPDATE_PERSONAL);
		getActivity().registerReceiver(receiver, filter);
		sell_listview=(ListView) view.findViewById(R.id.loginlayer_sell_list);
		buy_listview=(ListView) view.findViewById(R.id.loginlayer_buy_list);
		updateView();
		initBtn();
		return view;
	}
	
	public void updateView()
	{
		if(sharedPreferences.contains("email"))
		{
			view.findViewById(R.id.layer_without_login).setVisibility(View.INVISIBLE);
			view.findViewById(R.id.layer_with_login).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.loginlayer_email_tv)).setText(sharedPreferences.getString("email", null));
			setAccount((TextView)view.findViewById(R.id.loginlayer_account_tv));
			initListViews();
		}else {
			view.findViewById(R.id.layer_without_login).setVisibility(View.VISIBLE);
			view.findViewById(R.id.layer_with_login).setVisibility(View.INVISIBLE);
		}
	}

	public void initBtn()
	{
		view.findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Log.e("Lin", "click");
				final String email=((EditText)view.findViewById(R.id.email_editer)).getText().toString();
				final String password=((EditText)view.findViewById(R.id.password_editer)).getText().toString();
				final RequestParams params=new RequestParams();
				params.add("email", email);
				params.add("password", password);
				Log.e("Lin", email+" "+password+" "+MainActivity.IP+"register.php");
				client.get(MainActivity.IP+"register.php", params, new AsyncHttpResponseHandler()
				{
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
					{
						// TODO Auto-generated method stub
						if(new String(arg2).equals("1"))
						{
							Toast.makeText(getActivity(), "註冊成功", Toast.LENGTH_SHORT).show();
						}else if (new String(arg2).equals("0"))
						{
							Toast.makeText(getActivity(), "該郵箱已被註冊", Toast.LENGTH_SHORT).show();
						}
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
					{
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "網絡連接失敗", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});

		view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Log.e("Lin", "click");
				final String email=((EditText)view.findViewById(R.id.email_editer)).getText().toString();
				final String password=((EditText)view.findViewById(R.id.password_editer)).getText().toString();
				final RequestParams params=new RequestParams();
				params.add("email", email);
				params.add("password", password);
				Log.e("Lin", email+" "+password+" "+MainActivity.IP+"login.php");
				client.get(MainActivity.IP+"login.php", params, new AsyncHttpResponseHandler()
				{
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
					{
						if(new String(arg2).equals("1"))
						{
							Toast.makeText(getActivity(), "登陸成功", Toast.LENGTH_SHORT).show();
							view.findViewById(R.id.layer_without_login).setVisibility(View.INVISIBLE);
							view.findViewById(R.id.layer_with_login).setVisibility(View.VISIBLE);
							SharedPreferences.Editor editor=sharedPreferences.edit();
							editor.putString("email", email);
							editor.commit();
							updateView();
							((TextView)view.findViewById(R.id.loginlayer_email_tv)).setText(sharedPreferences.getString("email", null));
							setAccount((TextView)view.findViewById(R.id.loginlayer_account_tv));
						}else if (new String(arg2).equals("2"))
						{
							Toast.makeText(getActivity(), "密碼錯誤", Toast.LENGTH_SHORT).show();
						}else if (new String(arg2).equals("3"))
						{
							Toast.makeText(getActivity(), "該郵箱沒被註冊過", Toast.LENGTH_SHORT).show();
						}
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
					{
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "網絡連接失敗", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});

		view.findViewById(R.id.loginlayer_btn_logout).setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				sharedPreferences.edit().remove("email").commit();
				updateView();
			}
		});
	
		view.findViewById(R.id.loginlayer_btn_sell).setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent(getActivity(), SellActivity.class);
				startActivity(intent);
			}
		});
	}
	
	public void setAccount(final TextView tv)
	{
		RequestParams params=new RequestParams("email", sharedPreferences.getString("email", null));
		client.get(MainActivity.IP+"getUserByEmail.php", params, new JsonHttpResponseHandler()
		{

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response)
			{
				try
				{
					tv.setText(response.getString("account"));
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
			
		});
	}

	

	public void initListViews()
	{
		sellListAdapter=new SellListAdapter(getActivity());
		sell_listview.setAdapter(sellListAdapter);
		client.get(MainActivity.IP+"getSellItem.php", new RequestParams("email", sharedPreferences.getString("email", null)), 
				new JsonHttpResponseHandler()
		{

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response)
			{
				List<SellListNode> nodes=new ArrayList<>();
				for(int i=0;i<response.length();i++)
				{
					try
					{
						JSONObject object=response.getJSONObject(i);
						SellListNode node=new SellListNode();
						node.orderID=object.getInt("orderID");
						node.mailOfbuyer=object.getString("mailOfbuyer");
						node.itemNum=object.getInt("itemNum");
						node.deposit=object.getDouble("deposit");
						node.dealType=object.getInt("dealType");
						node.name=object.getString("name");
						nodes.add(node);
					} catch (JSONException e){e.printStackTrace();}
				}
				sellListAdapter.setData(nodes);
			}
			
		});
		
		buyListAdapter=new BuyListAdapter(getActivity());
		buy_listview.setAdapter(buyListAdapter);
		client.get(MainActivity.IP+"getBuyItem.php", new RequestParams("email", sharedPreferences.getString("email", null)), 
				new JsonHttpResponseHandler()
		{

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response)
			{
				List<BuyListNode> nodes=new ArrayList<>();
				Log.e("Lin", "respon num:"+response.length());
				for(int i=0;i<response.length();i++)
				{
					try
					{
						JSONObject object=response.getJSONObject(i);
						BuyListNode node=new BuyListNode();
						node.dealType=object.getInt("dealType");
						node.orderID=object.getInt("orderID");
						node.mailOfseller=object.getString("mailOfseller");
						node.itemNum=object.getInt("itemNum");
						node.deposit=object.getDouble("deposit");
						node.name=object.getString("name");
						nodes.add(node);
//						Log.e("Lin", "num********************"+i);
//						Log.e("Lin", node.dealType+"");
//						Log.e("Lin", node.orderID+"");
//						Log.e("Lin", node.mailOfseller+"");
//						Log.e("Lin", node.itemNum+"");
//						Log.e("Lin", node.deposit+"");
//						Log.e("Lin", node.name+"");
					} catch (JSONException e){e.printStackTrace();}
				}
				buyListAdapter.setData(nodes);
			}
			
		});
	}

	
	private class PersonalCenterReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			setAccount((TextView)view.findViewById(R.id.loginlayer_account_tv));
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
