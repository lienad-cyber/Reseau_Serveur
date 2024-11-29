
<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulaire et Boucle for</title>
</head>

<body>
    <h1>Formulaire avec Méthode POST</h1>
    <form method="POST" action="">
        <label for="nombre">Entrez un nombre :</label>
        <input type="number" id="nombre" name="nombre" min="1" max="100" required>
        <button type="submit">Envoyer</button>
    </form>

    <h2>Résultat de la boucle for</h2>
    <ul>
        <?php
        if ($_SERVER["REQUEST_METHOD"] === "POST" && isset($_POST["nombre"])) {
            $nombre = intval($_POST["nombre"]);
            for ($i = 1; $i <= $nombre; $i++) {
                echo "<li>Élément numéro $i</li>";
            }
        } else {
            echo "<li>Aucun nombre soumis</li>";
        }
        ?>
    </ul>
</body>
</html>
