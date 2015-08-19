	<?php
		$response = array();
	require("config.inc.php");
		if(!empty($_POST)){
		
			$query = "INSERT INTO Ubicacion (Precio, Establecimiento_idEstablecimiento,	Nombre_producto,Unidad ) VALUES ( :precio, :id_establecimiento, :producto,:Unidad ) ";
		 	$query_params = array(
		    ':precio' => $_POST['Precio'],
		    ':id_establecimiento' => $_POST['Id_establecimiento'],
		    ':producto' => utf8_encode($_POST['Producto']),
		    ':Unidad' => $_POST['Unidad']
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
		
			$response["resultado"] = "se ha agregado un producto";
			$response["success"] = 1;
			echo json_encode($response);
		}
			 
			else {
				?>
				 <h1>Agregar producto</h1> 
				 <form action="agregar_producto.php" method="post"> 
				     Precio:<br /> 
				     <input type="text" name="Precio" value="" /> 
				     <br /><br /> 

				     id establecimiento:<br /> 
				     <input type="text" name="Id_establecimiento" value="" /> 
				     <br /><br /> 
				       nombre producto:<br /> 
				     <input type="text" name="Producto" value="" /> 
				     <br /><br /> 

				     Unidad:<br /> 
				     <input type="text" name="Unidad" value="" /> 
				     <br /><br /> 

				     <input type="submit" value="Agregar Producto" /> 
				 </form>
				 <?php
		}
	?>