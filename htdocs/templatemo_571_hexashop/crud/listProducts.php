<?php

session_start();
if (!isset($_SESSION['user_id'])) {
    header("Location: ./login.php");
    exit();
}

require_once '../CreateDB/connectDB.php';

$conn = Database::getConnection();
$query = "SELECT * FROM produits ORDER BY id DESC";
$stmt = $conn->query($query);
$produits = $stmt->fetchAll(PDO::FETCH_ASSOC);

Database::closeConnection();
?>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des produits</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">

    <title>Hexashop - Product Listing Page</title>

    <!-- Additional CSS Files -->
    <link rel="stylesheet" type="./text/css" href="assets/css/bootstrap.min.css">

    <link rel="stylesheet" type="./text/css" href="assets/css/font-awesome.css">

    <link rel="stylesheet" href="./assets/css/templatemo-hexashop.css">

    <link rel="stylesheet" href="./assets/css/owl-carousel.css">

    <link rel="stylesheet" href="./assets/css/lightbox.css">


    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f4f7fc;
            margin: 0;
            padding: 20px;
            color: #333;
        }

        h2 {
            text-align: center;
            color: #333;
            font-size: 2.5rem;
            margin-bottom: 30px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .btn {
            display: inline-block;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 5px;
            font-size: 1rem;
            font-weight: 600;
        }

        .add-button {
            background-color: #28a745;
            color: white;
        }

        .logout-button {
            background-color: #c91e23;
            color: white;
            margin-left: 10px;
        }

        .btn:hover {
            opacity: 0.8;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 30px;
            background-color: white;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            overflow: hidden;
        }

        table th,
        table td {
            padding: 15px;
            text-align: left;
            vertical-align: middle;
        }

        table th {
            background-color: #f7f7f7;
            color: #555;
            font-weight: 600;
        }

        table td {
            background-color: #fff;
        }

        table img {
            max-width: 100px;
            height: auto;
            border-radius: 5px;
        }

        table .actions a {
            margin-right: 10px;
            color: #007bff;
            text-decoration: none;
            font-weight: 500;
        }

        table .actions a:hover {
            text-decoration: underline;
        }

        .actions a.delete {
            color: #c91e23;
        }

        .actions a.delete:hover {
            text-decoration: none;
        }

        @media (max-width: 768px) {

            table th,
            table td {
                padding: 8px;
                font-size: 0.9rem;
            }

            .btn {
                font-size: 0.9rem;
                padding: 8px 15px;
            }

            table img {
                max-width: 80px;
            }
        }
    </style>
    
</head>



<body>

    <div class="container">
        <h2>Liste des produits</h2>

        <div class="actions-buttons">
            <a href="add.php" class="btn add-button">Ajouter un nouveau produit</a>
            <a href="./login/logout.php" class="btn logout-button">Se déconnecter</a>
        </div>

        <table>
            <thead>
                <tr>
                    <th>Catégorie ID</th>
                    <th>Nom</th>
                    <th>Description</th>
                    <th>Prix</th>
                    <th>Photo</th>
                    <th>Second Photo</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($produits as $produit): ?>
                    <tr>
                        <td><?= htmlspecialchars($produit['categorie_id']); ?></td>
                        <td><?= htmlspecialchars($produit['nom']); ?></td>
                        <td><?= htmlspecialchars($produit['description']); ?></td>
                        <td><?= htmlspecialchars($produit['prix']); ?> €</td>
                        <td>
                            <?php if (!empty($produit['url_photo'])): ?>
                                <img src="../assets/images/<?= htmlspecialchars($produit['url_photo']); ?>" alt="Photo">
                            <?php else: ?>
                                Pas d'image
                            <?php endif; ?>
                        </td>
                        <td>
                            <?php if (!empty($produit['url_second_photo'])): ?>
                                <img src="../assets/images/<?= htmlspecialchars($produit['url_second_photo']); ?>"
                                    alt="Second Photo">
                            <?php else: ?>
                                Pas d'image
                            <?php endif; ?>
                        </td>
                        <td class="actions">
                            <a href="single-product.php?id=<?= $produit['id']; ?>">Voir</a> |
                            <a href="edit.php?id=<?= $produit['id']; ?>">Modifier</a> |
                            <a href="delete.php?id=<?= $produit['id']; ?>" class="delete"
                                onclick="return confirm('Voulez-vous vraiment supprimer ce produit ?');">Supprimer</a>
                        </td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    </div>

</body>

</html>