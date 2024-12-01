<?php
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    echo "Données reçues via POST :<br>";
    print_r($_POST);
} else {
    echo "Méthode non POST.";
}
?>