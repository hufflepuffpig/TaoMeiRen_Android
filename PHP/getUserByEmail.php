<?php
	$email=$_GET['email'];

	$dbc=mysqli_connect('localhost','root','root','taomeiren')
	or die("connect failed");
	
	$query="SELECT * FROM user where email='$email'";
	
	$result=mysqli_query($dbc,$query)
	or die("query failed");
	
	$row=mysqli_fetch_array($result);
	$obj=new stdClass();
	$obj->email=$row['email'];
	$obj->passwd=$row['passwd'];
	$obj->account=$row['account'];
	$obj->buyerCredit=$row['buyerCredit'];
	$obj->sellerCredit=$row['sellerCredit'];
	mysqli_close($dbc);
	echo json_encode($obj);
?>