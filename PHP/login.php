<?php
	$email=$_GET['email'];
	$password=$_GET['password'];
	
	$dbc=mysqli_connect('localhost','root','root','taomeiren')
	or die("connect taomeiren failed");
	
	$query="SELECT * FROM user WHERE email='$email'";
	$result=mysqli_query($dbc,$query);
	if(mysqli_num_rows($result)==0)
	{
		mysqli_close($dbc);
		echo "3";//no this username
	}
	else
	{
		$row=mysqli_fetch_array($result);
		if($password==$row['passwd'])
		{
			echo "1";//login success
		}
		else
		{
			echo "2";//password is wrong
		}
	}
?>