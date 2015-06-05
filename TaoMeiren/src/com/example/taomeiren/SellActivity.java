package com.example.taomeiren;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.Header;

import com.example.utils.GetFilePath;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SellActivity extends Activity
{

	private LinearLayout collective_layou,auction_layout;
	private ImageView fileImageView;
	private boolean isRecycleBmp=false;
	private File imgFile=null;
	private AsyncHttpClient client=new AsyncHttpClient();
	private ProgressDialog progressDialog;
	private int mCurTableType=1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sellactivity_main);
		collective_layou=(LinearLayout) findViewById(R.id.sellactivity_collective_layout);
		auction_layout=(LinearLayout) findViewById(R.id.sellactivity_auction_layout);
		initRadioGroup();//使之动态改变填写表格
		initFileBtn();//使之响应选择文件
		initSubmitBtn();//使之提交参数
	}
	
	public void initRadioGroup()
	{
		((RadioGroup)findViewById(R.id.radiogroup)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				switch (checkedId)
				{
				case R.id.radiobtn_oneprice:
					collective_layou.setVisibility(View.GONE);
					auction_layout.setVisibility(View.GONE);
					mCurTableType=1;
					break;
				case R.id.radiobtn_collective:
					collective_layou.setVisibility(View.VISIBLE);
					auction_layout.setVisibility(View.GONE);
					mCurTableType=2;
					break;
				case R.id.radiobtn_auction:
					collective_layou.setVisibility(View.GONE);
					auction_layout.setVisibility(View.VISIBLE);
					mCurTableType=3;
					break;

				default:
					break;
				}
			}
		});
	}

	public void initFileBtn()
	{
		fileImageView=(ImageView) findViewById(R.id.sellactivity_file_iv);
		((Button)findViewById(R.id.sellactivity_filebtn)).setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent file_intent=new Intent();
				file_intent.setAction(Intent.ACTION_GET_CONTENT);
				file_intent.setType("image/*");
				startActivityForResult(file_intent, 0);
			}
		});
	}
	
	public void initSubmitBtn()
	{
		((Button)findViewById(R.id.sellactivity_submitbtn)).setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				uploadFile();
				client.post(MainActivity.IP+"forSellActivity.php", getTableParams(), new AsyncHttpResponseHandler()
				{
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
					{
						Log.e("Lin", new String(arg2));
						if(new String(arg2).equals("1"))
						{
							SellActivity.this.finish();
						}
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
					{
						Toast.makeText(SellActivity.this, "有点问题= =", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	public RequestParams getTableParams()
	{
		RequestParams params=new RequestParams();
		String imgURL=imgFile.getAbsolutePath().substring(imgFile.getAbsolutePath().lastIndexOf("/")+1);
		String mailOfseller=getSharedPreferences("data", MODE_PRIVATE).getString("email", null);
		String price=((EditText)findViewById(R.id.sellactivity_price_editor)).getText().toString();
		String description=((EditText)findViewById(R.id.sellactivity_description_editor)).getText().toString();
		String name=((EditText)findViewById(R.id.sellactivity_name_editor)).getText().toString();
		String stock=((EditText)findViewById(R.id.sellactivity_stock_editor)).getText().toString();
		
		params.put("imgURL", imgURL);
		params.put("mailOfseller", mailOfseller);
		params.put("price", price);
		params.put("description", description);
		params.put("name", name);
		params.put("stock", stock);
		if(mCurTableType==1)
		{
			String dealType="1";
			params.put("dealType", dealType);
			return params;
			
		}else if(mCurTableType==2)
		{
			String dealType="2";
			String deadLine=((EditText)findViewById(R.id.sellactivity_deadline_editor1)).getText().toString();
			String activeNum=((EditText)findViewById(R.id.sellactivity_activeNum_editor)).getText().toString();
			String collectivePrice=((EditText)findViewById(R.id.sellactivity_collectiveprice_editor)).getText().toString();
			params.put("dealType", dealType);
			params.put("deadLine", deadLine);
			params.put("activeNum", activeNum);
			params.put("collectivePrice", collectivePrice);
			return params;
			
		}else if(mCurTableType==3)
		{
			String dealType="3";
			String deadLine=((EditText)findViewById(R.id.sellactivity_deadline_editor2)).getText().toString();
			String activePrice=((EditText)findViewById(R.id.sellactivity_activeprice_editor)).getText().toString();
			params.put("dealType", dealType);
			params.put("deadLine", deadLine);
			params.put("activePrice", activePrice);
			return params;
		}
		return params;
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		if(requestCode==0)
		{
			Uri uri=data.getData();
			if(((BitmapDrawable)fileImageView.getDrawable()).getBitmap()!=null && isRecycleBmp)
				((BitmapDrawable)fileImageView.getDrawable()).getBitmap().recycle();
			fileImageView.setImageURI(uri);
			isRecycleBmp=true;
			
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);
			int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			actualimagecursor.moveToFirst();
			String img_path = actualimagecursor.getString(actual_image_column_index);
			imgFile = new File(img_path);
			if(imgFile.exists())
				Log.e("lin", img_path);
			
			/*String img_path=new GetFilePath(this).getRealPath(uri);
			Log.e("lin", img_path);
			imgFile = new File(img_path);
			if(imgFile.exists())
				Log.e("lin", img_path);*/
		}
		
	}

	public void uploadFile()
	{
		if (imgFile!=null && imgFile.exists() && imgFile.length() > 0) 
		{
			RequestParams params = new RequestParams();
			try
			{
				params.put("uploadfile", imgFile);
			} catch (FileNotFoundException e)
			{
				Toast.makeText(SellActivity.this, "文件不存在", Toast.LENGTH_LONG).show();
			}
			// 上传文件
			client.post(MainActivity.IP+"uploadimg.php", params, new AsyncHttpResponseHandler() 
			{

				@Override
				public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						Throwable arg3)
				{
					Toast.makeText(SellActivity.this, "上传失败", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
				{
					Toast.makeText(SellActivity.this, "上传成功", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onFinish()
				{
					// TODO Auto-generated method stub
					super.onFinish();
					if(progressDialog!=null)
					{
						progressDialog.dismiss();
						progressDialog=null;
					}
				}

				@Override
				public void onStart()
				{
					// TODO Auto-generated method stub
					super.onStart();
					progressDialog=new ProgressDialog(SellActivity.this);
					progressDialog.setTitle("上传图片中");
					progressDialog.setCanceledOnTouchOutside(true);
					progressDialog.show();
				}

				
				

			});
		} else {
			Toast.makeText(SellActivity.this, "文件不存在", Toast.LENGTH_LONG).show();
		}
	}

	
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
	}

}
