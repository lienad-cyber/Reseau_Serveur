<?php
require_once '../CreateDB/connectDB.php';

if (isset($_POST['update'])) {
    $id = (int) $_POST['id'];
    $nom = trim($_POST['nom']);
    $description = trim($_POST['description']);
    $categorie_id = (int) $_POST['categorie_id'];
    $note = (int) $_POST['note'];
    $prix = trim($_POST['prix']);
    
    // Variables pour les photos
    $photo = null;
    $second_photo = null;

    // Traitement de la première photo
    if (isset($_FILES['photo']) && $_FILES['photo']['error'] == UPLOAD_ERR_OK) { 
        $target_dir = "";
        $target_file = $target_dir . basename($_FILES["photo"]["name"]);
    
        $imageFileType = strtolower(pathinfo($target_file, PATHINFO_EXTENSION));
        $check = getimagesize($_FILES["photo"]["tmp_name"]);
        if ($check === false) {
            die("Sorry, the file is not a valid image.");
        }
    
        if (move_uploaded_file($_FILES["photo"]["tmp_name"], $target_file)) { 
            $photo = $target_file;
        } else {
            die("Sorry, there was an error uploading your file.");
        }
    } else {
        echo 'No photo uploaded.';
    }

    // Traitement de la seconde photo
    if (isset($_FILES['second_photo']) && $_FILES['second_photo']['error'] == UPLOAD_ERR_OK) { 

        $target_file = $target_dir .  basename($_FILES["second_photo"]["name"]);
    
        $imageFileType = strtolower(pathinfo($target_file, PATHINFO_EXTENSION));
        $check = getimagesize($_FILES["second_photo"]["tmp_name"]);
        if ($check === false) {
            die("Sorry, the file is not a valid image.");
        }
    
        if (move_uploaded_file($_FILES["second_photo"]["tmp_name"], $target_file)) { 
            $second_photo = $target_file;
        } else {
            die("Sorry, there was an error uploading your file.");
        }
    } else {
        echo 'No second photo uploaded.';
    }

    try {
        $conn = Database::getConnection();

        // Mise à jour des informations du produit
        $sql = "UPDATE produits SET
                    nom = :nom,
                    description = :description,
                    categorie_id = :categorie_id, 
                    note = :note, 
                    prix = :prix, 
                    url_photo = :url_photo,
                    url_second_photo = :url_second_photo
                WHERE id = :id";

        $stmt = $conn->prepare($sql);
        $stmt->bindParam(':nom', $nom, PDO::PARAM_STR);
        $stmt->bindParam(':description', $description, PDO::PARAM_STR);
        $stmt->bindParam(':categorie_id', $categorie_id, PDO::PARAM_INT);
        $stmt->bindParam(':note', $note, PDO::PARAM_INT);
        $stmt->bindParam(':prix', $prix, PDO::PARAM_STR);
        $stmt->bindParam(':url_photo', $photo, PDO::PARAM_STR);
        $stmt->bindParam(':url_second_photo', $second_photo, PDO::PARAM_STR);
        $stmt->bindParam(':id', $id, PDO::PARAM_INT);

        $stmt->execute();

        echo "<p><font color='green'>Data updated successfully!</font></p>";
        echo "<a href='./listProducts.php'>View Result</a>";
    } catch (PDOException $e) {
        echo "<p><font color='red'>Error: " . $e->getMessage() . "</font></p>";
    } finally {
        Database::closeConnection();
    }
}
?>
