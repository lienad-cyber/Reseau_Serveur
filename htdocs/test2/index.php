<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Exemple de boucle for</title>
</head>
<body>
    <h1>Exemple de boucle for en PHP</h1>
    <ul>
        <?php
        for ($i = 1; $i <= 10; $i++) {
            echo "<li>Élément numéro $i</li>";
        }
        ?>
    </ul>
</body>
</html>
