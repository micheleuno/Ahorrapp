	<?php
		$response = array();
	require("config.inc.php");
		if(!empty($_POST)){
			$comentario =$_POST['comentario'];
			$id =$_POST['id'];		
			$nombre = $_POST['nombre'];
			$rut_usuario = $_POST['rut_usuario'];
			$query = "INSERT INTO Comentario ( Comentario, Establecimiento_idEstablecimiento,nombre_usuario,rut_usuario ) VALUES ( :Comentario, :id_establecimiento,:nombre,:rut_usuario ) ";
		 	$query_params = array(
		    ':Comentario' => ($_POST['comentario']),
		    ':id_establecimiento' => $_POST['id'],
		    ':nombre' => $_POST['nombre'],
		    ':rut_usuario' => $_POST['rut_usuario']
		    );	    
	    //ejecutamos la query y creamos el usuario
		    try {
		        $stmt   = $db->prepare($query);
		        $result = $stmt->execute($query_params);
		    }
		     catch (PDOException $ex) {
		        // solo para testing
		       // die("Failed to run query: " . $ex->getMessage());	        
		        $response["success"] = 0;
		        $response["message"] = "Error base de datos2. Porfavor vuelve a intentarlo";
		       // die(json_encode($response));
		    }
		
			$response["resultado"] = "se ha agregado un comentario";
			$response["success"] = 1;
			echo json_encode($response);
		}
			 
			else {
				?>
				 <h1>Agregar Comentario</h1> 
				 <form action="agregar_comentario.php" method="post"> 
				     comentario:<br /> 
				     <input type="text" name="comentario" value="" /> 
				     <br /><br /> 
				     nombre usuario:<br /> 
				     <input type="text" name="nombre" value="" /> 
				     <br /><br /> 
				     rut usuario:<br /> 
				     <input type="text" name="rut_usuario" value="" /> 
				     <br /><br /> 

				     id establecimiento:<br /> 
				     <input type="text" name="id" value="" /> 
				     <br /><br /> 

				     <input type="submit" value="Register New User" /> 
				 </form>
				 <?php
		}
	?>