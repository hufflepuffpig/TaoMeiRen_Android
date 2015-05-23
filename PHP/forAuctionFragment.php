<?php
	$from=$_GET['from'];
	$now=0;
	$arr=array();
	
	$dbc=mysqli_connect('localhost','root','root','taomeiren')
	or die("connect failed");
	
	$sql="SELECT * FROM commodity,auction WHERE commodity.commodityID=auction.commodityID";
	$result=mysqli_query($dbc,$sql) or die("query failed");
	
	while($row=mysqli_fetch_array($result))
	{
		if($now<$from)
		{
			$now++;
			continue;
		}
		if($now>$from+4)
			break;
		else
		{
			$obj=new stdClass();
			$obj->commodityID=$row['commodityID'];
			$obj->imageURL=$row['imageURL'];
			$obj->mailOfseller=$row['mailOfseller'];
			$obj->price=$row['price'];
			$obj->description=$row['description'];
			$obj->name=$row['name'];
			$obj->stock=$row['stock'];
			$obj->dealType=$row['dealType'];
			$obj->commodityCredit=$row['commodityCredit'];
			$obj->ID=$row['ID'];
			$obj->deadLine=$row['deadLine'];
			$obj->activePrice=$row['activePrice'];
			$obj->currentPrice=$row['currentPrice'];
			$obj->currentMail=$row['currentMail'];
			$obj->mailOfparticipants=$row['mailOfparticipants'];
			array_push($arr,json_encode($obj));
			$now++;
		}
	}
	mysqli_close($dbc);
	echo "[";
	for($i=0;$i<count($arr);$i++)
	{
		echo $arr[$i];
		if($i<count($arr)-1)
			echo ",";
	}
	echo "]";
?>