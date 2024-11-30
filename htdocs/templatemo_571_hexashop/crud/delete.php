<?php

require_once("../createDB/connectDB.php");

$conn = Database::getConnection();

if (isset($_GET['id']) && is_numeric($_GET['id'])) {
    $id = intval($_GET['id']);

    try {
        $stmt = $conn->prepare("DELETE FROM produits WHERE id = :id");
        $stmt->bindParam(':id', $id, PDO::PARAM_INT);

        if ($stmt->execute()) {
            header("Location: listProducts.php?message=success");
            exit;
        } else {
            echo "Erreur lors de la suppression.";
        }
    } catch (PDOException $e) {
        echo "Erreur : " . $e->getMessage();
    }
} else {
    echo "ID invalide.";
    exit;
}

Database::closeConnection();
?>
