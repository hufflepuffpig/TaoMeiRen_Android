<?php
	$email=$_GET['email'];
	$password=$_GET['password'];
	
	$dbc=mysqli_connect('localhost','root','root','taomeiren')
	or die('connect taomeiren failed');
	
	$query="SELECT * FROM user WHERE email='$email'";
	$result=mysqli_query($dbc,$query);
	if(mysqli_num_rows($result)!=0)
	{
		mysqli_close($dbc);
		echo "0";//This username has been registered
	}
	else
	{
		$query="INSERT INTO user(email,passwd,account) value('$email','$password',10000)";
		mysqli_query($dbc,$query);
		mysqli_close($dbc);
		echo "1";//register success
	}
?>