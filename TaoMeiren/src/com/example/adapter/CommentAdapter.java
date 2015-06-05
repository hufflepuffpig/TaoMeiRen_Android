package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Comment;
import com.example.taomeiren.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter
{
	private List<Comment> comments;
	private Context mContext;
	public CommentAdapter(Context context)
	{
		this.comments=new ArrayList<>();
		this.mContext=context;
	}
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return comments.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return comments.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		Holder holder;
		if(convertView==null)
		{
			convertView=LayoutInflater.from(mContext).inflate(R.layout.comment_activity_listview_item, null);
			holder=new Holder();
			holder.email=(TextView) convertView.findViewById(R.id.comment_item_email);
			holder.content=(TextView) convertView.findViewById(R.id.comment_item_content);
			convertView.setTag(holder);
		}else {
			holder=(Holder) convertView.getTag();
		}
		holder.email.setText(comments.get(position).email);
		holder.content.setText(comments.get(position).content);
		return convertView;
	}

	private class Holder
	{
		public TextView email,content;
	}
	
	public void setData(List<Comment> list)
	{
		comments=list;
		notifyDataSetChanged();
	}
	public void addData(Comment comment)
	{
		comments.add(comment);
		notifyDataSetChanged();
	}
}
