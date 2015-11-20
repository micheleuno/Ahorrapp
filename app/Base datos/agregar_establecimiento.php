	<?php
		$response = array();
	require("config.inc.php");
		if(!empty($_POST)){
		
			$query = "INSERT INTO Establecimiento (Descripcion, Nombre,	Direccion,Latitud,Longitud,Contacto ) VALUES ( :descripcion,:nombre,:direccion,:latitud,:longitud,:contacto ) ";
		 	$query_params = array(
		    ':descripcion' => ($_POST['Descripcion']),
		    ':nombre' => ($_POST['Nombre']),
		    ':direccion' => ($_POST['Direccion']),
		    ':latitud' => $_POST['Latitud'],
		    ':longitud' => $_POST['Longitud'],
		    ':contacto' => $_POST['Contacto']
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
		
			$response["resultado"] = "se ha agregado un establecimiento";
			$response["success"] = 1;
			echo json_encode($response);
		}
			 
			else {
				?>
				 <h1>Agregar producto</h1> 
				 <form action="agregar_establecimiento.php" method="post"> 
				     Descripcion:<br /> 
				     <input type="text" name="Descripcion" value="" /> 
				     <br /><br /> 

				     Nombre:<br /> 
				     <input type="text" name="Nombre" value="" /> 
				     <br /><br /> 
				       Direccion:<br /> 
				     <input type="text" name="Direccion" value="" /> 
				     <br /><br /> 

				     Latitud:<br /> 
				     <input type="text" name="Latitud" value="" /> 
				     <br /><br /> 
				     Longitud:<br /> 
				     <input type="text" name="Longitud" value="" /> 
				     <br /><br /> 
				     Contacto:<br /> 
				     <input type="text" name="Contacto" value="" /> 
				     <br /><br /> 

				     <input type="submit" value="Agregar Producto" /> 
				 </form>
				 <?php
		}
	?>