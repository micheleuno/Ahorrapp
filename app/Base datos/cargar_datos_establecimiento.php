<?php
	$response = array();
	require_once __DIR__ . '/db_config.php';
	if(!empty($_POST)){
		$Latitud =utf8_encode($_POST['Latitud']);
		$Longitud =utf8_encode($_POST['Longitud']);
		$conexion = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
		$query = " SELECT d.idEstablecimiento as idEstablecimiento , d.Nombre as Nombre, d.Direccion as Direccion, d.Descripcion as Descripcion, d.Contacto as Contacto, Usuario.Id_establecimiento as Id_usuario_estab
			FROM Establecimiento d
			LEFT JOIN Usuario ON d.idEstablecimiento = Usuario.Id_establecimiento
			WHERE Latitud =  '".$Latitud."'
			AND Longitud =  '".$Longitud."'"; 			
		$result = mysqli_query($conexion, $query); 
		if (mysqli_num_rows($result) > 0 ) {
			$response["Establecimiento"] = array();
			while ($row = mysqli_fetch_array($result)) {
				$product = array();
				$product["Nombre"] = $row["Nombre"];
				$product["Direccion"] = $row["Direccion"];
				$product["IdEstablecimiento"] = $row["idEstablecimiento"];
				$product["Id_usuario_estab"] = $row["Id_usuario_estab"];
				$product["Descripcion"] = $row["Descripcion"];
				$product["Contacto"] = $row["Contacto"];
				array_push($response["Establecimiento"], $product);
			}
			$response["success"] = 1;
			echo json_encode($response);
		}
	}
	else {
		?>
		 <h1>Buscar establecimiento</h1> 
		 <form action="cargar_datos_establecimiento.php" method="post"> 
		    latitud:<br /> 
		     <input type="text" name="Latitud" value="" /> 
		     <br /><br /> 
		    latitud:<br /> 
		     <input type="text" name="Longitud" value="" /> 
		     <br /><br /> 
		     <input type="submit" value="Buscar" /> 
		 </form>
		 <?php
	}
?>