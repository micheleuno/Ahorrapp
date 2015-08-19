	<?php
		$response = array();
	require("config.inc.php");
		if(!empty($_POST)){																	

			$query = "DELETE from Ubicacion

					 where  idUbicacion =  :id;

			";
		 	$query_params = array(
		    ':id' => $_POST['id'],		    
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
		
			$response["resultado"] = "se ha eliminado un producto";
			$response["success"] = 1;
			echo json_encode($response);
		}
			 
			else {
				?>
				 <h1>Eliminar producto</h1> 
				 <form action="eliminar_producto.php" method="post"> 
				     Nombre producto:<br /> 
				     <input type="text" name="nombre" value="" /> 
				     <br /><br /> 

				     Id Establecimiento:<br /> 
				     <input type="text" name="id" value="" /> 
				     <br /><br /> 
				     
				     <input type="submit" value="Eliminar producto" /> 
				 </form>
				 <?php
		}
	?>