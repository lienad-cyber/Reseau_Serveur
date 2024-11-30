<?php

require_once '../CreateDB/connectDB.php';

if (isset($_GET['id'])) {

	$id = (int) $_GET['id'];

	try {
		$conn = Database::getConnection();

		$stmt = $conn->prepare("SELECT * FROM produits WHERE id = :id");
		$stmt->bindParam(':id', $id, PDO::PARAM_INT);
		$stmt->execute();

		$product = $stmt->fetch(PDO::FETCH_ASSOC);

		if (!$product) {
			$error = "Produits non disponible";
		}
	} catch (PDOException $e) {
		$error_message = "Erreur : " . $e->getMessage();
	} finally {
		Database::closeConnection();
	}
} else {
	$error_message = "ID du produit invalide.";
}
?>

<html>

<!-- <head>
	<title>Edit Data</title>
</head> -->

<?php
	require_once 'components/headerTest.php';
?>

<body>
	<h2>Edit Data</h2>
	<p>
		<a href="index.php">Home</a>
	</p>

	<form name="edit" method="post" action="editAction.php" enctype="multipart/form-data">

		<table border="0">
			<tr>
				<td>Name</td>
				<td><input type="text" name="nom" value="<?php echo $product['nom']; ?>"></td>
			</tr>
			<tr>
				<td>Description</td>
				<td><input type="text" name="description" value="<?php echo $product['description']; ?>">
				</td>
			</tr>
			<tr>
				<td>categorie_id</td>
				<td><input type="number" name="categorie_id"
						value="<?php echo $product['categorie_id']; ?>"></td>
			</tr>
			<tr>
				<td>note</td>
				<td><input type="text" name="note" value="<?php echo $product['note']; ?>"></td>
			</tr>
			<tr>
				<td>Prix</td>
				<td><input type="text" name="prix" value="<?php echo $product['prix']; ?>"></td>
			</tr>
			<tr>
				<td>Url de la photo</td>
				<td><input type="file" name="photo" accept="image/*" required></td>
			</tr>
			<tr>
				<td>Url de la second photo</td>
				<td><input type="file" name="second_photo" accept="image/*" required></td>
			</tr>
			<tr>
				<td><input type="hidden" name="id" value=<?php echo $id; ?>></td>
				<td><input type="submit" name="update" value="Update"></td>
			</tr>
		</table>
	</form>
</body>

</html>