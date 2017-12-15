<?php
session_start();
session_destroy(); // Destroying All Sessions
header("Location: index.php"); // Redirecting To Home Page

?>