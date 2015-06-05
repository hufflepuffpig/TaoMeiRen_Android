package com.example.taomeiren;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.adapter.CommentAdapter;
import com.example.model.Comment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class CommentActivity extends Activity
{
	private SharedPreferences sharedPreferences;
	private String email;
	private EditText mycommentEditText;
	private Button btn;
	private ListView listView;
	private CommentAdapter myAdapter;
	private AsyncHttpClient client;
	private int commodityID;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_activity_main);
		sharedPreferences=getSharedPreferences("data", MODE_PRIVATE);
		client=new AsyncHttpClient();
		
		email=sharedPreferences.getString("email", null);
		commodityID=getIntent().getIntExtra("commodityID", 0);
		
		mycommentEditText=(EditText) findViewById(R.id.comment_mycomment_editor);
		btn=(Button) findViewById(R.id.comment_postbtn);
		listView=(ListView) findViewById(R.id.comment_list);
		initListView();
		initBtn();
	}
	
	private void initListView()
	{
		myAdapter=new CommentAdapter(this);
		listView.setAdapter(myAdapter);
		client.get(MainActivity.IP+"getPersonalComment.php", new RequestParams("commodityID", commodityID), new JsonHttpResponseHandler()
		{

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray response)
			{
				// TODO Auto-generated method stub
				List<Comment> ret=new ArrayList<>();
				for(int i=0;i<response.length();i++)
				{
					try
					{
						JSONObject object=response.getJSONObject(i);
						Comment node=new Comment();
						node.email=object.getString("email");
						node.content=object.getString("personalComment");
						ret.add(node);
						
					} catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
				myAdapter.setData(ret);
				super.onSuccess(statusCode, headers, response);
				
			}
			
		});
	}

	private void initBtn()
	{
		btn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(email==null)
				{
					Toast.makeText(CommentActivity.this, "登陆后才能评论", Toast.LENGTH_SHORT).show();
					return;
				}else {
					final String content=mycommentEditText.getText().toString();
					RequestParams params=new RequestParams();
					params.put("email", email);
					params.put("commodityID", commodityID);
					params.put("personalComment", content);
					client.get(MainActivity.IP+"writePersonalComment.php", params, new AsyncHttpResponseHandler()
					{
						
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2)
						{
							// TODO Auto-generated method stub
							Comment node=new Comment();
							node.email=email;
							node.content=content;
							myAdapter.addData(node);
						}
						
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3)
						{
							// TODO Auto-generated method stub
							
						}
					});
				}
			}
		});
	}
}
