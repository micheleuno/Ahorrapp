<?php
	$response = array();
	require_once __DIR__ . '/db_config.php';
	if(!empty($_POST)){
			$idEstablecimiento =$_POST['idEstablecimiento'];
			$conexion = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
			$query = "SELECT Ubicacion.Nombre_producto,Ubicacion.Precio,Ubicacion.idUbicacion,Unidad.unidad
						FROM Ubicacion
						INNER JOIN Unidad ON Unidad.id = Ubicacion.Unidad
						WHERE  Ubicacion.Establecimiento_idEstablecimiento ='".$idEstablecimiento."'	;
					"; 			
			$result = mysqli_query($conexion, $query); 
			if (mysqli_num_rows($result) > 0 ) {
				$response["Producto"] = array();
				while ($row = mysqli_fetch_array($result)) {
					$producto = array();
					$producto["Nombre"] = $row["Nombre_producto"];
					$producto["Precio"] = $row["Precio"];
					$producto["Unidad"] = $row["unidad"];
					$producto["idUbicacion"] = $row["idUbicacion"];
					
					array_push($response["Producto"], $producto);
				}
				$response["id"] = $idEstablecimiento;
				$response["success"] = 1;
				echo json_encode($response);
			}
		} 
		else {
			?>
			 <h1>Cargar Productos</h1> 
			 <form action="cargar_productos.php" method="post"> 
			     Id establecimiento:<br /> 
			     <input type="text" name="idEstablecimiento" value="" /> 
			     <br /><br /> 		
			 </form>
			 <?php
	}
?>

