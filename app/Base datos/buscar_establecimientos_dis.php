	<?php
		$response = array();
		require_once __DIR__ . '/db_config.php';
		if(!empty($_POST)){
			$nombre =($_POST['Nombre']);
			$latitud =($_POST['Latitud']);
			$longittud =($_POST['Longitud']);
			$nombre_producto_flag =($_POST['Nombre_producto_flag']);
			$nombre_local_flag =($_POST['Nombre_local_flag']);
			$distancia =($_POST['Distancia']);
			$rubro =($_POST['Rubro']);
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
					LEFT JOIN Ubicacion  ON upper(Ubicacion.Nombre_producto) like ('%".$nombre."%')  or ('".$nombre_producto_flag."')  = 0 
					where  (Ubicacion.Establecimiento_idEstablecimiento = d.idEstablecimiento  || ('".$nombre."') = '' ) 
					and(d.Nombre like ('%".$nombre."%') or ('".$nombre_local_flag."')=0  )
					and (d.Rubro like ('%".$nombre."%') or ('".$rubro."')=0 )
					having distance_in_km<2 or ('".$distancia."')  = 0
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
			}
			else{
	$response["success"] = 0;
				echo json_encode($response);
			}	
				

			
		
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