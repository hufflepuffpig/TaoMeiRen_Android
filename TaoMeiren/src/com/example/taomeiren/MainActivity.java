package com.example.taomeiren;

import org.apache.http.Header;

import com.example.fragments.AuctionFragment;
import com.example.fragments.CollectivtBuyingFragment;
import com.example.fragments.OnePriceFragment;
import com.example.fragments.PersonalCenterFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends Activity
{
	
	public static final String IP="http://10.0.2.2/taomeiren/";
	
	public OnePriceFragment onePriceFragment;
	public CollectivtBuyingFragment collectivtBuyingFragment;
	public AuctionFragment auctionFragment;
	public PersonalCenterFragment personalCenterFragment;
	
	private int currentFragment=1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//init all the Fragment
		onePriceFragment=new OnePriceFragment();
		collectivtBuyingFragment=new CollectivtBuyingFragment();
		auctionFragment=new AuctionFragment();
		personalCenterFragment=new PersonalCenterFragment();
		
		getFragmentManager().beginTransaction()
		.add(R.id.fragment_container, onePriceFragment)
		.add(R.id.fragment_container, collectivtBuyingFragment)
		.hide(collectivtBuyingFragment)
		.add(R.id.fragment_container, auctionFragment)
		.hide(auctionFragment)
		.add(R.id.fragment_container, personalCenterFragment)
		.hide(personalCenterFragment)
		.commit();
		
	}
	
	public void Ontitlebar_item_click(View view)
	{
		switch (view.getId())
		{
		case R.id.titlebar_item_oneprice:
			getFragmentManager().beginTransaction()
			.show(onePriceFragment)
			.hide(collectivtBuyingFragment)
			.hide(auctionFragment)
			.hide(personalCenterFragment)
			.commit();
			currentFragment=1;
			break;
		case R.id.titlebar_item_collective_buying:
			getFragmentManager().beginTransaction()
			.hide(onePriceFragment)
			.show(collectivtBuyingFragment)
			.hide(auctionFragment)
			.hide(personalCenterFragment)
			.commit();
			currentFragment=2;
			break;
		case R.id.titlebar_item_auction:
			getFragmentManager().beginTransaction()
			.hide(onePriceFragment)
			.hide(collectivtBuyingFragment)
			.show(auctionFragment)
			.hide(personalCenterFragment)
			.commit();
			currentFragment=3;
			break;
		case R.id.titlebar_item_personal_center:
			getFragmentManager().beginTransaction()
			.hide(onePriceFragment)
			.hide(collectivtBuyingFragment)
			.hide(auctionFragment)
			.show(personalCenterFragment)
			.commit();
			currentFragment=4;
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		switch (currentFragment)
		{
		case 1:
			getFragmentManager().beginTransaction()
			.show(onePriceFragment)
			.hide(collectivtBuyingFragment)
			.hide(auctionFragment)
			.hide(personalCenterFragment)
			.commit();
			break;
		case 2:
			getFragmentManager().beginTransaction()
			.hide(onePriceFragment)
			.show(collectivtBuyingFragment)
			.hide(auctionFragment)
			.hide(personalCenterFragment)
			.commit();
			break;
		case 3:
			getFragmentManager().beginTransaction()
			.hide(onePriceFragment)
			.hide(collectivtBuyingFragment)
			.show(auctionFragment)
			.hide(personalCenterFragment)
			.commit();
			break;
		case 4:
			getFragmentManager().beginTransaction()
			.hide(onePriceFragment)
			.hide(collectivtBuyingFragment)
			.hide(auctionFragment)
			.show(personalCenterFragment)
			.commit();
			break;

		default:
			break;
		}
	}

}
