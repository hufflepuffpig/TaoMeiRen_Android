package com.example.db;

import org.apache.http.Header;

import android.R.integer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taomeiren.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserDao
{
	private AsyncHttpClient client;
	public UserDao()
	{
		client=new AsyncHttpClient();
	}
	
}
