<?php
	$response = array();
	require_once __DIR__ . '/db_config.php';
	if(!empty($_POST)){
		$Email_usuario =$_POST['Email_usuario'];
		$conexion = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
		$query = " SELECT password,username FROM Usuario

					Where Email_usuario='".$Email_usuario."'"; 			
		$result = mysqli_query($conexion, $query); 
		if (mysqli_num_rows($result) > 0 ) {
			$response["usuario"] = array();
			while ($row = mysqli_fetch_array($result)) {
				$usuario = array();
				$usuario["password"] = $row["password"];
				$usuario["username"] = $row["username"];
				array_push($response["usuario"], $usuario);
			}			
			$response["success"] = 1;
			echo json_encode($response);
		}		
		} 
		else {
			?>
			 <h1>Recuperar datos usuario</h1> 
			 <form action="cargar_contrasena.php" method="post"> 
			     Email usuario:<br /> 
			     <input type="text" name="Email_usuario" value="" /> 
			     <br /><br /> 
			     <input type="submit" value="Buscar" /> 
			 </form>
			 <?php
	}
?>