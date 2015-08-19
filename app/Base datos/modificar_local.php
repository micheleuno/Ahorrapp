	<?php
		$response = array();
	require("config.inc.php");
		if(!empty($_POST)){																	

			$query = "UPDATE Establecimiento
						set 
							 Descripcion = :Descripcion,
							 Direccion = :Direccion,
							 Contacto = :Contacto
					 where  idEstablecimiento =  :id;

			";
		 	$query_params = array(
		    ':Descripcion' => $_POST['Descripcion'],
		    ':Direccion' => $_POST['Direccion'],
		    ':Contacto' => $_POST['Contacto'],
		    ':id' => $_POST['id']
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
		
			$response["resultado"] = "se ha modificado un establecimiento";
			$response["success"] = 1;
			echo json_encode($response);
		}
			 
			else {
				?>
				 <h1>Actualizar Local</h1> 
				 <form action="modificar_local.php" method="post"> 
				     Direccion:<br /> 
				     <input type="text" name="Direccion" value="" /> 
				     <br /><br /> 

				     Descripcion:<br /> 
				     <input type="text" name="Descripcion" value="" /> 
				     <br /><br /> 
				      id:<br /> 
				     <input type="text" name="id" value="" /> 
				     <br /><br /> 
				      contacto:<br /> 
				     <input type="text" name="Contacto" value="" /> 
				     <br /><br /> 
				     <input type="submit" value="Actualizar local" /> 
				 </form>
				 <?php
		}
	?>