package com.example.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class GetFilePath
{
	private Context mContext;
	public GetFilePath(Context context)
	{
		mContext=context;
	}

	public String getRealPath(Uri fileUrl)
	{
		String fileName = null;
		Uri filePathUri = fileUrl;
		if (fileUrl != null)
		{
			if (fileUrl.getScheme().toString().compareTo("content") == 0) // content://开头的uri
			{
				Cursor cursor = mContext.getContentResolver().query(fileUrl,
						null, null, null, null);
				if (cursor != null && cursor.moveToFirst())
				{
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					fileName = cursor.getString(column_index); // 取出文件路径
					if(fileName.startsWith("/storage"))
					{
						fileName=fileName.substring(8);
						Log.e("fileName", fileName);
					}
					if (!fileName.startsWith("/mnt"))
					{
						// 检查是否有”/mnt“前缀

						fileName = "/mnt" + fileName;
					}
					cursor.close();
				}
			} else if (fileUrl.getScheme().compareTo("file") == 0) // file:///开头的uri
			{
				fileName = filePathUri.toString();
				fileName = filePathUri.toString().replace("file://", "");
				// 替换file://
				if (!fileName.startsWith("/mnt"))
				{
					// 加上"/mnt"头
					fileName += "/mnt";
				}
			}
		}
		return fileName;
	}
}
