<?php
	$response = array();
	require_once __DIR__ . '/db_config.php';
	if(!empty($_POST)){
		$nombre =($_POST['Nombre']);
		$conexion = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
		$query = "  
				SELECT distinct 
				d.Latitud as Latitud, 
				d.Longitud as Longitud,
				d.Nombre as Nombre,
				d.Direccion as Direccion,
				d.idEstablecimiento
				FROM Establecimiento d 		 
				INNER JOIN Ubicacion  ON upper(Ubicacion.Nombre_producto) like CONCAT  ('%', '".$nombre."' ,'%')  
				where  Ubicacion.Establecimiento_idEstablecimiento = d.idEstablecimiento  
		"; 
		$result = mysqli_query($conexion, $query); 
		if (mysqli_num_rows($result) > 0 ) {
			$response["Establecimiento"] = array();
			while ($row = mysqli_fetch_array($result)) {
				$product = array();
				$product["Nombre"] = ($row["Nombre"]);
				$product["Direccion"] = ($row["Direccion"]);
				$product["idEstablecimiento"] = $row["idEstablecimiento"];
					$product["Latitud"] = $row["Latitud"];
						$product["Longitud"] = $row["Longitud"];
				array_push($response["Establecimiento"], $product);
			}
			$response["success"] = 1;
			echo json_encode($response);
		}
	}
	else {
		?>
		 <h1>Buscar establecimiento</h1> 
		 <form action="buscar_establecimientos.php" method="post"> 
		     Nombre producto:<br /> 
		     <input type="text" name="Nombre" value="" /> 
		     <br /><br /> 
		    
		     <input type="submit" value="Buscar" /> 
		 </form>
		 <?php
	}
?>