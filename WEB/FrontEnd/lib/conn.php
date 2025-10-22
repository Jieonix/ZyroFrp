<?php
$mysql_server_name = 'zyroo.cn'; //数据库服务器
$mysql_username = 'root'; //数据库用户名
$mysql_password = 'shajie001'; //数据库密码
$mysql_database = 'frp'; //数据库名
$conn = mysqli_connect($mysql_server_name,$mysql_username,$mysql_password,$mysql_database);
if (mysqli_connect_errno($conn)) 
{ 
	die("连接 MySQL 失败: " . mysqli_connect_error()); 
}
?>