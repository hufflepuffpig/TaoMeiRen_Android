<?php
	$dbc=mysqli_connect('localhost','root','root','taomeiren')
	or die("connect failed");
	
	$dealType=$_POST['dealType'];
	
	$imgURL=$_POST['imgURL'];
	$mailOfseller=$_POST['mailOfseller'];
	$price=$_POST['price'];
	$description=$_POST['description'];
	$name=$_POST['name'];
	$stock=$_POST['stock'];
	if($dealType==1)//仅仅写进commodity
	{
		$sql="INSERT INTO commodity(imageURL, mailOfseller, price, description, name, stock, dealType) VALUES ('$imgURL','$mailOfseller',$price,'$description','$name',$stock,$dealType)";
		mysqli_query($dbc,$sql) or die("query failed");
		
	}else if($dealType==2)//写进commodity，collective_buying
	{
		$deadLine=$_POST['deadLine'];
		$activeNum=$_POST['activeNum'];
		$collectivePrice=$_POST['collectivePrice'];
		
		$sql="INSERT INTO commodity(imageURL, mailOfseller, price, description, name, stock, dealType) VALUES ('$imgURL','$mailOfseller',$price,'$description','$name',$stock,$dealType)";
		mysqli_query($dbc,$sql) or die("query failed");
		
		$sql2="SELECT commodityID FROM commodity WHERE imageURL='$imgURL' and mailOfseller='$mailOfseller' and name='$name'";
		$result=mysqli_query($dbc,$sql2) or die("query failed");
		$row=mysqli_fetch_array($result);
		$commodityID=$row['commodityID'];
	
		
		$sql3="INSERT INTO collective_buying(commodityID, deadLine, activeNum, collectivePrice, currentNum) VALUES ($commodityID,'$deadLine',$activeNum,$collectivePrice,0)";
		mysqli_query($dbc,$sql3) or die("query failed");
		
	}else if($dealType==3)//写进commodity，auction
	{
		$deadLine=$_POST['deadLine'];
		$activePrice=$_POST['activePrice'];
		
		$sql="INSERT INTO commodity(imageURL, mailOfseller, price, description, name, stock, dealType) VALUES ('$imgURL','$mailOfseller',$price,'$description','$name',$stock,$dealType)";
		mysqli_query($dbc,$sql) or die("query failed");
		
		$sql2="SELECT commodityID FROM commodity WHERE imageURL='$imgURL' and mailOfseller='$mailOfseller' and name='$name'";
		$result=mysqli_query($dbc,$sql2) or die("query failed");
		$row=mysqli_fetch_array($result);
		$commodityID=$row['commodityID'];
	
		
		$sql3="INSERT INTO auction(commodityID, deadLine, activePrice) VALUES ($commodityID,'$deadLine',$activePrice)";
		mysqli_query($dbc,$sql3) or die("query failed");
	}
	mysqli_close($dbc);
	echo "1";
?>