<?php

// static $servername = "localhost";
// static $username = "root";
// static $password = "steve";
// static $dbname = "db_S2_root";
// static $conn = null;

// require_once 'assets/php/connectDB.php';

// function listeCategorie() {
//     try {
//         $conn = new PDO("mysql:host=" .  $servername ,  $dbname,  $username,  $password);
        
//         $sql = "SELECT id, nom_categorie FROM categorie";
//         $stmt = $conn->prepare($sql);
//         $stmt->execute();

//         $categories = $stmt->fetchAll(PDO::FETCH_ASSOC);

//         return $categories;
//     } catch (PDOException $e) {
//         echo "Erreur lors de la récupération des catégories : " . $e->getMessage();
//         return [];
//     }
// }

// $categories = listeCategorie();

// if (!empty($categories)) {
//     echo "<ul>";
//     foreach ($categories as $categorie) {
//         echo "<li>" . htmlspecialchars($categorie['nom_categorie']) . "</li>";
//     }
//     echo "</ul>";
// } else {
//     echo "Aucune catégorie trouvée.";
// }

?>

<link rel="stylesheet" href="assets/css/style.css">

<ul class="stars">
    <li><i class="fa fa-star"></i></li>
    <li><i class="fa fa-star"></i></li>
    <li><i class="fa fa-star"></i></li>
    <li><i class="fa fa-star-half-o"></i></li>
    <li><i class="fa fa-star-o"></i></li>
</ul>
