<?php
	$response = array();
	require_once __DIR__ . '/db_config.php';
	if(!empty($_POST)){
		$idEstablecimiento =$_POST['idEstablecimiento'];
		$conexion = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
		$query = " SELECT Comentario,nombre_usuario FROM Comentario

					Where Establecimiento_idEstablecimiento='".$idEstablecimiento."'"; 			
		$result = mysqli_query($conexion, $query); 
		if (mysqli_num_rows($result) > 0 ) {
			$response["Comentario"] = array();
			while ($row = mysqli_fetch_array($result)) {
				$comentario = array();
				$comentario["Comentario"] = $row["Comentario"];
				$comentario["Nombre_usuario"] = $row["nombre_usuario"];
				array_push($response["Comentario"], $comentario);
			}
			$response["id"] = $idEstablecimiento;
			$response["success"] = 1;
			echo json_encode($response);
		}else{
			$response["Comentario"] = array();
			$comentario = array();
				$comentario["Comentario"] = "Aquí no hay comentarios";
				$comentario["Nombre_usuario"] = " ";
				array_push($response["Comentario"], $comentario);				
				$response["success"] = 1;
				echo json_encode($response);
		}		
		} 
		else {
			?>
			 <h1>Cargar comentarios</h1> 
			 <form action="cargar_comentarios.php" method="post"> 
			     Id Establecimiento:<br /> 
			     <input type="text" name="idEstablecimiento" value="" /> 
			     <br /><br /> 
			     <input type="submit" value="Buscar" /> 
			 </form>
			 <?php
	}
?>