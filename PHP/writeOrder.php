<?php
	$dbc=mysqli_connect('localhost','root','root','taomeiren')
	or die("connect failed");
	//�������ֹ���ʽ��Ҫ�ṩ����4����Ϣ
	$dealType=$_GET['dealType'];
	$commodityID=$_GET['commodityID'];
	$mailOfbuyer=$_GET['mailOfbuyer'];
	$mailOfseller=$_GET['mailOfseller'];
	$stock=$_GET['stock'];

	if($dealType==1)//����һ�ڼ���Ʒ
	{
		//�����ṩ������������Ʒ����
		$itemNum=$_GET['itemNum'];
		$stock=$stock-$itemNum;
		$price=$_GET['price'];
		$deposit=$price*$itemNum;
		
		$sql="INSERT INTO order_table(commodityID, itemNum, mailOfbuyer, mailOfseller, deposit, dealType) VALUES ($commodityID,$itemNum,'$mailOfbuyer','$mailOfseller',$deposit,$dealType)";
		mysqli_query($dbc,$sql) or die("query1 failed");
		
		if($stock>0)
		{
			$sql2="UPDATE commodity SET stock=$stock WHERE commodityID=$commodityID";
			mysqli_query($dbc,$sql2) or die("query2 failed");
		}else if($stock==0)
		{
			$sql3="DELETE FROM commodity WHERE commodityID=$commodityID";
			mysqli_query($dbc,$sql3) or die("query3 failed");
		}
	
	}else if($dealType==2)//�����Ź���Ʒ
	{
		//�����ṩ�����������Ź�ID
		$itemNum=$_GET['itemNum'];
		$stock-=$itemNum;
		$ID=$_GET['ID'];
		$sql1="SELECT * FROM collective_buying WHERE ID=$ID";
		$result=mysqli_query($dbc,$sql1) or die("query failed");
		$row=mysqli_fetch_array($result);
		$mailOfparticipants=$row['mailOfparticipants'];
		$mailOfparticipants=$mailOfparticipants."$mailOfbuyer,";
		$currentNum=$row['currentNum'];
		$currentNum+=$itemNum;
		$collectivePrice=$row['collectivePrice'];
		$deposit=$collectivePrice*$itemNum;
		
		$sql2="INSERT INTO order_table(commodityID, itemNum, mailOfbuyer, mailOfseller, deposit, dealType) VALUES ($commodityID,$itemNum,'$mailOfbuyer','$mailOfseller',$deposit,$dealType)";
		mysqli_query($dbc,$sql2) or die("query failed");
		
		$sql3="UPDATE collective_buying SET mailOfparticipants='$mailOfparticipants',currentNum=$currentNum WHERE ID=$ID";
		mysqli_query($dbc,$sql3) or die("query failed");
		
		if($stock>0)
		{
			$sql4="UPDATE commodity SET stock=$stock WHERE commodityID=$commodityID";
			mysqli_query($dbc,$sql4) or die("query failed");
		}else if($stock==0)
		{
			$sql5="DELETE FROM commodity WHERE commodityID=$commodityID";
			mysqli_query($dbc,$sql5) or die("query failed");
		}
		
	}else if($dealType==3)
	{
		//�����ṩ����ۺ;���ID
		$currentPrice=$_GET['currentPrice'];
		$ID=$_GET['ID'];
		$sql1="SELECT * FROM auction WHERE ID=$ID";
		$result=mysqli_query($dbc,$sql1) or die("query failed");
		$row=mysqli_fetch_array($result);
		$mailOfparticipants=$row['mailOfparticipants'];
		$mailOfparticipants=$mailOfparticipants."(".$mailOfbuyer.",".$currentPrice.")";
		
		$sql2="INSERT INTO order_table(commodityID, itemNum, mailOfbuyer, mailOfseller, deposit, dealType) VALUES ($commodityID,1,'$mailOfbuyer','$mailOfseller',$currentPrice,$dealType)";
		mysqli_query($dbc,$sql2) or die("query failed");
		
		$sql3="UPDATE auction SET currentPrice=$currentPrice,currentMail=$mailOfbuyer,mailOfparticipants=$mailOfparticipants WHERE 1";
		mysqli_query($dbc,$sql3) or die("query failed");
	}
	mysqli_close($dbc);
	echo "1";
?>