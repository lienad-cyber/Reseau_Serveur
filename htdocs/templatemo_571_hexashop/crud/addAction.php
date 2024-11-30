<?php
require_once '../createDB/connectDB.php';

if (isset($_POST['submit'])) {
    $conn = Database::getConnection();

    $categorie_id = htmlspecialchars($_POST['categorie_id']);
    $nom = htmlspecialchars($_POST['nom']);
    $description = htmlspecialchars($_POST['description']);
    $prix = htmlspecialchars($_POST['prix']);

    if (isset($_FILES['photo']) && $_FILES['photo']['error'] == 0 && isset($_FILES['second_photo']) && $_FILES['second_photo']['error'] == 0) {
        $targetDir = "../assets/images/";
        $fileName = basename($_FILES['photo']['name']);
        $targetFilePath = $targetDir . $fileName;
        $fileType = pathinfo($targetFilePath, PATHINFO_EXTENSION);

        $secondFileName = basename($_FILES['second_photo']['name']);
        $secondTargetFilePath = $targetDir . $secondFileName;
        $secondFileType = pathinfo($secondTargetFilePath, PATHINFO_EXTENSION);

        $allowedTypes = ['jpg', 'jpeg', 'png', 'gif'];
        if (in_array(strtolower($fileType), $allowedTypes)) {

            if (move_uploaded_file($_FILES['photo']['tmp_name'], $targetFilePath)) {
                $url_photo = $fileName;
            } else {
                echo "<p style='color:red;'>Erreur lors du téléchargement du fichier.</p>";
                exit;
            }
        } else {
            echo "<p style='color:red;'>Type de fichier non autorisé. Seuls JPG, JPEG, PNG, et GIF sont acceptés.</p>";
            exit;
        }

        if (in_array(strtolower($secondFileType), $allowedTypes)) {

            if (move_uploaded_file($_FILES['second_photo']['tmp_name'], $secondTargetFilePath)) {
                $url_second_photo = $secondFileName;
            } else {
                echo "<p style='color:red;'>Erreur lors du téléchargement du second fichier.</p>";
                exit;
            }
        } else {
            echo "<p style='color:red;'>Type de fichier non autorisé. Seuls JPG, JPEG, PNG, et GIF sont acceptés.</p>";
            exit;
        }
    } else {
        echo "<p style='color:red;'>Veuillez sélectionner une photo.</p>";
        exit;
    }

    try {
        $stmt = $conn->prepare("INSERT INTO produits (categorie_id, nom, description, prix, url_photo, url_second_photo) 
                                VALUES (:categorie_id, :nom, :description, :prix, :url_photo, :url_second_photo)");
        $stmt->bindParam(':categorie_id', $categorie_id);
        $stmt->bindParam(':nom', $nom);
        $stmt->bindParam(':description', $description);
        $stmt->bindParam(':prix', $prix);
        $stmt->bindParam(':url_photo', $url_photo);
        $stmt->bindParam(':url_second_photo', $url_second_photo);

        if ($stmt->execute()) {
            echo "<p style='color:green;'>Produit ajouté avec succès !</p>";
            echo "<a href='listProducts.php'>Voir la liste des produits</a>";
        }
    } catch (PDOException $e) {
        echo "Erreur : " . $e->getMessage();
    }

    Database::closeConnection();
}
?>