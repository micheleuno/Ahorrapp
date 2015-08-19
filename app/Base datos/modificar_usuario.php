	<?php
		$response = array();
	require("config.inc.php");
		if(!empty($_POST)){																	

			$query = "UPDATE Usuario
						set 
							 Rut_usuario = :Rut_usuario,
							 Direccion_usuario = :Direccion_usuario,
							 Telefono_usuario = :Telefono_usuario,
							 Id_establecimiento = :Id_establecimiento,
							 Rut_establecimiento = :Rut_establecimiento,
							 Giro_estableciemiento = :Giro_estableciemiento
					 where  id =  :id;

			";
		 	$query_params = array(
		    ':Rut_usuario' => $_POST['Rut_usuario'],
		    ':Direccion_usuario' => $_POST['Direccion_usuario'],
		    ':Telefono_usuario' => $_POST['Telefono_usuario'],
		    ':Id_establecimiento' => $_POST['Id_establecimiento'],
		    ':Rut_establecimiento' => $_POST['Rut_establecimiento'],
		    ':Giro_estableciemiento' => $_POST['Giro_estableciemiento'],
		    ':id' => $_POST['id']
		    );	    
	    //ejecutamos la query y creamos el usuario
		    try {
		        $stmt   = $db->prepare($query);
		        $result = $stmt->execute($query_params);
		    }
		     catch (PDOException $ex) {
		        // solo para testing
		        //die("Failed to run query: " . $ex->getMessage());	        
		        $response["success"] = 0;
		        $response["message"] = "Error base de datos2. Porfavor vuelve a intentarlo";
		       // die(json_encode($response));
		    }
		
			$response["resultado"] = "se ha modificado un usuario";
			$response["success"] = 1;
			echo json_encode($response);
		}
			 
			else {
				?>
				 <h1>Actualizar usaurio</h1> 
				 <form action="modificar_usuario.php" method="post"> 
				     Rut:<br /> 
				     <input type="text" name="Rut_usuario" value="" /> 
				     <br /><br /> 

				     Direccion:<br /> 
				     <input type="text" name="Direccion_usuario" value="" /> 
				     <br /><br /> 
				      Telefono:<br /> 
				     <input type="text" name="Telefono_usuario" value="" /> 
				     <br /><br /> 

				     ID establecimento:<br /> 
				     <input type="text" name="Id_establecimiento" value="" /> 
				     <br /><br /> 
				      Rut establecimiento:<br /> 
				     <input type="text" name="Rut_establecimiento" value="" /> 
				     <br /><br /> 

				     Giro establecimiento:<br /> 
				     <input type="text" name="Giro_estableciemiento" value="" /> 
				     <br /><br />
				     ID usuario:<br /> 
				     <input type="text" name="id" value="" /> 
				     <br /><br />
				     <input type="submit" value="Actualizar Usuario" /> 
				 </form>
				 <?php
		}
	?>