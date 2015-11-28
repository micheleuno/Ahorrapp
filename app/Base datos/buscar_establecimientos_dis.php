<?php
	$response = array();
	require_once __DIR__ . '/db_config.php';
	if(!empty($_POST)){
		$nombre =($_POST['Nombre']);
		$latitud =($_POST['Latitud']);
		$longittud =($_POST['Longitud']);
		$conexion = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
		$query = "  
				
					SELECT distinct 
				d.Latitud as Latitud, 
				d.Longitud as Longitud,
				d.Nombre as Nombre,
				d.Direccion as Direccion,
				d.idEstablecimiento,
				 111.1111 *
	   			 DEGREES(ACOS(COS(RADIANS(d.Latitud))
	         * COS(RADIANS('".$latitud."'))
	         * COS(RADIANS(d.Longitud - '".$longittud."'))
	         + SIN(RADIANS(d.Latitud))
	         * SIN(RADIANS('".$latitud."')))) AS distance_in_km			
				FROM Establecimiento d 				 
				LEFT JOIN Ubicacion  ON upper(Ubicacion.Nombre_producto) like ('%".$nombre."%')  
				where  (Ubicacion.Establecimiento_idEstablecimiento = d.idEstablecimiento  || ('".$nombre."') = '' ) 
				having distance_in_km<2
				order by Ubicacion.Precio ASC






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
						$product["distancia"] = $row["distance_in_km"];
				array_push($response["Establecimiento"], $product);
			}
			$response["success"] = 1;
			echo json_encode($response);
		}else{
			$query = "  
				SELECT  
				Latitud , 
				Longitud,
				Nombre,
				Direccion,
				idEstablecimiento
				FROM Establecimiento 				
				where  Nombre like ('%".$nombre."%')
		"; 

		
		$result2 = mysqli_query($conexion, $query); 
		if (mysqli_num_rows($result2) > 0 ) {
			$response["Establecimiento"] = array();
			while ($row = mysqli_fetch_array($result2)) {
				$product = array();
				$product["Nombre"] = ($row["Nombre"]);
				$product["Direccion"] = ($row["Direccion"]);
				$product["idEstablecimiento"] = $row["idEstablecimiento"];
					$product["Latitud"] = $row["Latitud"];
						$product["Longitud"] = $row["Longitud"];
						$product["distancia"] = 'asd';
				array_push($response["Establecimiento"], $product);
			}
			$response["success"] = 1;
			echo json_encode($response);
		}
		else{
$response["success"] = 0;
			echo json_encode($response);
		}	}	
			

		
	
	}
	else {
		?>
		 <h1>Buscar establecimiento</h1> 
		 <form action="buscar_establecimientos_dis.php" method="post"> 
		     Nombre producto:<br /> 
		     <input type="text" name="Nombre" value="" /> 
		     <br /><br /> 
		      Latitud:<br /> 
		     <input type="text" name="Latitud" value="" /> 
		     <br /><br /> 
		      Longitud:<br /> 
		     <input type="text" name="Longitud" value="" /> 
		     <br /><br /> 
		    
		     <input type="submit" value="Buscar" /> 
		 </form>
		 <?php
	}
?>