<?php
	$response = array();
	require_once __DIR__ . '/db_config.php';
	$conexion = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);
	$sql = " SELECT id,unidad FROM Unidad"; 
	$result = mysqli_query($conexion, $sql); 
	if (mysqli_num_rows($result) > 0) {
		$response["Unidades"] = array();

		while ($row = mysqli_fetch_array($result)) {
			$product = array();
			$product["Id"] = $row["id"];
			$product["Unidad"] = $row["unidad"];
			array_push($response["Unidades"], $product);
		}
		$response["success"] = 1;
		echo json_encode($response);
		} 
		else {
			$response["success"] = 0;
			$response["message"] = "No products found";
			echo json_encode($response);
	}
?>