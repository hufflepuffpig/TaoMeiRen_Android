package com.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class AsyncBitmapLoader
{
	private Map<String, SoftReference<Bitmap>> imgCacheMap;
	private boolean isAllowDownloadbmp=true;
	public AsyncBitmapLoader()
	{
		imgCacheMap=new HashMap<>();
	}
	
	public Bitmap loadBitmap(final ImageView imageView,final String imageURL, final ImageCallBack imageCallBack) 
	{
		if(imgCacheMap.containsKey(imageURL))
		{
			Bitmap bmp=imgCacheMap.get(imageURL).get();
			if(bmp!=null)
			{
				return bmp;
			}else {
				imgCacheMap.remove(imageURL);
			}
		}
		if (isAllowDownloadbmp)
		{
			final Handler handler = new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					imageCallBack.imageLoad(imageView, (Bitmap) msg.obj);
					// imageView.setImageBitmap((Bitmap) msg.obj);
				}
			};

			new Thread()
			{

				@Override
				public void run()
				{
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(imageURL);
					try
					{
						HttpResponse response = client.execute(get);
						InputStream is = response.getEntity().getContent();
						Bitmap bmp = BitmapFactory.decodeStream(is);
						Log.e("Lin", "download " + imageURL);
						imgCacheMap.put(imageURL,
								new SoftReference<Bitmap>(bmp));
						Message msg = handler.obtainMessage(0, bmp);
						handler.sendMessage(msg);
					} catch (ClientProtocolException e)
					{
						e.printStackTrace();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}

			}.start();
		}
		return null;
	}
	
	public interface ImageCallBack
	{
		public void imageLoad(ImageView imageView,Bitmap bmp);
	}
	
	public void setDownloadFlag(boolean flag)
	{
		isAllowDownloadbmp=flag;
	}
}
