	<?php
		$response = array();
	require("config.inc.php");
		if(!empty($_POST)){																	

			$query = "UPDATE Ubicacion
						set 
							 Precio = :Precio,
							 Nombre_producto = :Nombre_producto,
							 Unidad = :Unidad
					 where  	idUbicacion =  :Establecimiento_idEstablecimiento;

			";
		 	$query_params = array(
		    ':Precio' => $_POST['Precio'],
		    ':Nombre_producto' => $_POST['Nombre_producto'],
		    ':Unidad' => $_POST['Unidad'],
		    ':Establecimiento_idEstablecimiento' => $_POST['Establecimiento_idEstablecimiento']		   
		    );	    
	    //ejecutamos la query y creamos el usuario
		    try {
		        $stmt   = $db->prepare($query);
		        $result = $stmt->execute($query_params);
		    }
		     catch (PDOException $ex) {
		        // solo para testing
		        die("Failed to run query: " . $ex->getMessage());	        
		        $response["success"] = 0;
		        $response["message"] = "Error base de datos2. Porfavor vuelve a intentarlo";
		       // die(json_encode($response));
		    }
		
			$response["resultado"] = "se ha modificado un producto";
			$response["success"] = 1;
			echo json_encode($response);
		}
			 
			else {
				?>
				 <h1>Modificar producto</h1> 
				 <form action="modificar_producto.php" method="post"> 
				     Precio:<br /> 
				     <input type="text" name="Precio" value="" /> 
				     <br /><br /> 

				     Nombre Producto:<br /> 
				     <input type="text" name="Nombre_producto" value="" /> 
				     <br /><br /> 
				      Unidad:<br /> 
				     <input type="text" name="Unidad" value="" /> 
				     <br /><br /> 

				     ID producto:<br /> 
				     <input type="text" name="Establecimiento_idEstablecimiento" value="" /> 
				     <br /><br /> 
				      
				     <input type="submit" value="Modificar producto" /> 
				 </form>
				 <?php
		}
	?>