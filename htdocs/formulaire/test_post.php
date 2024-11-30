<?php
if (isset($_SERVER['REQUEST_METHOD'])) {
    echo "Méthode de requête : " . $_SERVER['REQUEST_METHOD'] . "\n";
} else {
    echo "REQUEST_METHOD n'est pas défini.\n";
}

$postData = file_get_contents('php://input');
echo "Données POST : " . $_POST["username"] . "\n";
?>
