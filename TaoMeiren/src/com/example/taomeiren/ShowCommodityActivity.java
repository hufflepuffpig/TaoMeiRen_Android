package com.example.taomeiren;

import org.apache.http.Header;

import com.example.model.Auction;
import com.example.model.CollectiveBuying;
import com.example.model.Commodity;
import com.example.utils.GlobalValues;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowCommodityActivity extends Activity
{
	private Intent intent;
	private int position;
	private int from;
	private Commodity commodity;
	private CollectiveBuying collectiveBuying;
	private Auction auction;
	private ImageLoaderConfiguration configuration;
	//xml中的组件
	private ImageView img;
	private TextView name,description,stock,layer1_price,layer2_deadline,layer2_activeNum,
	layer2_currentNum,layer2_collectivePrice,layer3_deadline,layer3_activePrice,layer3_currentPrice;
	private EditText layer1_editor,layer2_editor,layer3_editor;
	private Button layer1_btn,layer2_btn,layer3_btn;
	
	private AsyncHttpClient client;
	private SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_commodity_activity_main);
		
		configuration=new ImageLoaderConfiguration.Builder(this).build();
		ImageLoader.getInstance().init(configuration);
		client=new AsyncHttpClient();
		sharedPreferences=getSharedPreferences("data", MODE_PRIVATE);
		
		intent=getIntent();
		from=intent.getIntExtra("from", 0);
		position=intent.getIntExtra("position", 0);
		initLayer();
		initComponent();
		initInfo();
		initBtn();
	}
	
	
	private void initLayer()
	{
		switch (from)
		{
		case GlobalValues.FROM_COMMODITY:
			findViewById(R.id.show_commodity_layer1).setVisibility(View.VISIBLE);
			findViewById(R.id.show_commodity_layer2).setVisibility(View.GONE);
			findViewById(R.id.show_commodity_layer3).setVisibility(View.GONE);
			commodity=(Commodity) intent.getSerializableExtra("commodity");
			break;
		case GlobalValues.FROM_COLLECTIVE_BUYING:
			findViewById(R.id.show_commodity_layer1).setVisibility(View.GONE);
			findViewById(R.id.show_commodity_layer2).setVisibility(View.VISIBLE);
			findViewById(R.id.show_commodity_layer3).setVisibility(View.GONE);
			collectiveBuying=(CollectiveBuying) intent.getSerializableExtra("collectivebuying");
			break;
		case GlobalValues.FROM_AUCTION:
			findViewById(R.id.show_commodity_layer1).setVisibility(View.GONE);
			findViewById(R.id.show_commodity_layer2).setVisibility(View.GONE);
			findViewById(R.id.show_commodity_layer3).setVisibility(View.VISIBLE);
			auction=(Auction) intent.getSerializableExtra("auction");
			break;

		default:
			break;
		}
	}

	private void initComponent()
	{
		img=(ImageView) findViewById(R.id.show_commodity_img);
		name=(TextView) findViewById(R.id.show_commodity_name);
		description=(TextView) findViewById(R.id.show_commodity_description);
		stock=(TextView) findViewById(R.id.show_commodity_stock);
		layer1_price=(TextView) findViewById(R.id.show_commodity_layer1_price);
		layer1_editor=(EditText) findViewById(R.id.show_commodity_layer1_numEditor);
		layer1_btn=(Button) findViewById(R.id.show_commodity_layer1_buybtn);
		layer2_deadline=(TextView) findViewById(R.id.show_commodity_layer2_deadline);
		layer2_activeNum=(TextView) findViewById(R.id.show_commodity_layer2_activeNum);
		layer2_currentNum=(TextView) findViewById(R.id.show_commodity_layer2_currentNum);
		layer2_collectivePrice=(TextView) findViewById(R.id.show_commodity_layer2_collectivePrice);
		layer2_editor=(EditText) findViewById(R.id.show_commodity_layer2_numEditor);
		layer2_btn=(Button) findViewById(R.id.show_commodity_layer2_buybtn);
		layer3_deadline=(TextView) findViewById(R.id.show_commodity_layer3_deadline);
		layer3_activePrice=(TextView) findViewById(R.id.show_commodity_layer3_activePrice);
		layer3_currentPrice=(TextView) findViewById(R.id.show_commodity_layer3_currentPrice);
		layer3_editor=(EditText) findViewById(R.id.show_commodity_layer3_priceEditor);
		layer3_btn=(Button) findViewById(R.id.show_commodity_layer3_buybtn);
	}
	
	private void initInfo()
	{
		if(from==GlobalValues.FROM_COMMODITY)
		{
			ImageLoader.getInstance().displayImage(MainActivity.IP+"images/"+commodity.imageURL, img);
			name.setText(commodity.name);
			description.setText(commodity.description);
			stock.setText(""+commodity.stock);
			layer1_price.setText(""+commodity.price);
		}else if(from==GlobalValues.FROM_COLLECTIVE_BUYING)
		{
			ImageLoader.getInstance().displayImage(MainActivity.IP+"images/"+collectiveBuying.commodity.imageURL, img);
			name.setText(collectiveBuying.commodity.name);
			description.setText(collectiveBuying.commodity.description);
			stock.setText(""+collectiveBuying.commodity.stock);
			layer2_deadline.setText(collectiveBuying.deadLine);
			layer2_activeNum.setText(""+collectiveBuying.activeNum);
			layer2_currentNum.setText(""+collectiveBuying.currentNum);
			layer2_collectivePrice.setText(""+collectiveBuying.collectivePrice);
		}else if(from==GlobalValues.FROM_AUCTION)
		{
			ImageLoader.getInstance().displayImage(MainActivity.IP+"images/"+auction.commodity.imageURL, img);
			name.setText(auction.commodity.name);
			description.setText(auction.commodity.description);
			stock.setText(""+auction.commodity.stock);
			layer3_deadline.setText(auction.deadLine);
			layer3_activePrice.setText(""+auction.activePrice);
			layer3_currentPrice.setText(""+auction.currentPrice);
		}
	}

	private void initBtn()
	{
		layer1_btn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				String itemNum=layer1_editor.getText().toString();
				
				RequestParams params=new RequestParams();
				params.put("dealType",1);
				params.put("commodityID", commodity.commodityID);
				params.put("mailOfbuyer", sharedPreferences.getString("email", null));
				params.put("mailOfseller", commodity.mailOfseller);
				params.put("stock", commodity.stock);
				params.put("itemNum", Integer.parseInt(itemNum));
				params.put("price", commodity.price);
				commodity.stock-=Integer.parseInt(itemNum);
				client.get(MainActivity.IP+"writeOrder.php",  params, new AsyncHttpResponseHandler()
				{
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
					{
						// TODO Auto-generated method stub
						if(new String(arg2).equals("1"))
						{
							Toast.makeText(ShowCommodityActivity.this, "购买成功", Toast.LENGTH_SHORT).show();
							Intent intent=new Intent(GlobalValues.UPDATE_ONEPRICE);
							intent.putExtra("position", position);
							intent.putExtra("commodity", commodity);
							sendBroadcast(intent);
							ShowCommodityActivity.this.finish();
						}
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
					{
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		
		
		layer2_btn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				String itemNum=layer2_editor.getText().toString();
				if(collectiveBuying.commodity.stock<Integer.parseInt(itemNum))
				{
					Toast.makeText(ShowCommodityActivity.this, "超出库存量了", Toast.LENGTH_SHORT).show();
					return;
				}
				
				RequestParams params=new RequestParams();
				params.put("dealType",2);
				params.put("commodityID", collectiveBuying.commodity.commodityID);
				params.put("mailOfbuyer", sharedPreferences.getString("email", null));
				params.put("mailOfseller", collectiveBuying.commodity.mailOfseller);
				params.put("stock", collectiveBuying.commodity.stock);
				params.put("itemNum", Integer.parseInt(itemNum));
				params.put("ID", collectiveBuying.ID);
				collectiveBuying.commodity.stock-=Integer.parseInt(itemNum);
				collectiveBuying.currentNum+=Integer.parseInt(itemNum);
				collectiveBuying.mailOfparticipants+=sharedPreferences.getString("email", null)+",";
				client.get(MainActivity.IP+"writeOrder.php", params, new AsyncHttpResponseHandler()
				{
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
					{
						if(new String(arg2).equals("1"))
						{
							Toast.makeText(ShowCommodityActivity.this, "购买成功", Toast.LENGTH_SHORT).show();
							Intent intent=new Intent(GlobalValues.UPDATE_COLLECTIVE);
							intent.putExtra("position", position);
							intent.putExtra("collectivebuying", collectiveBuying);
							sendBroadcast(intent);
							ShowCommodityActivity.this.finish();
						}
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
					{
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
	}
}
