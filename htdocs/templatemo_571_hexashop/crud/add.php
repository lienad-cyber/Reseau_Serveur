<style>
    form {
        max-width: 600px;
        margin: 0 auto;
        padding: 20px;
        background-color: #f9f9f9;
        border-radius: 10px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    table {
        width: 100%;
        border-collapse: collapse;
    }

    td {
        padding: 10px;
        text-align: left;
    }

    input[type="text"],
    input[type="number"],
    textarea,
    input[type="file"] {
        width: 100%;
        padding: 8px;
        margin-top: 5px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-size: 16px;
    }

    textarea {
        resize: vertical;
        min-height: 100px;
    }

    input[type="submit"] {
        background-color: #2a2a2a;
        color: white;
        border: none;
        padding: 12px 20px;
        font-size: 16px;
        border-radius: 5px;
        cursor: pointer;
        width: 100%;
        margin-top: 20px;
    }

    input[type="submit"]:hover {
        background-color:  #2a2a2a;
    }

    td:first-child {
        font-weight: bold;
        width: 30%;
    }

    input:invalid,
    textarea:invalid {
        border: 1px solid #2a2a2a;
    }

    input:valid,
    textarea:valid {
        border: 1px solid #2a2a2a;
    }
</style>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="https://fonts.googleapis.com/css?family=Poppins:100,200,300,400,500,600,700,800,900&display=swap"
        rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <title>Hexashop Ecommerce HTML CSS Template</title>

    <link rel="stylesheet" type="text/css" href="../assets/css/bootstrap.min.css">

    <link rel="stylesheet" type="text/css" href="../assets/css/font-awesome.css">

    <link rel="stylesheet" href="../assets/css/templatemo-hexashop.css">

    <link rel="stylesheet" href="../assets/css/owl-carousel.css">

    <link rel="stylesheet" href="../assets/css/lightbox.css">

    <link rel="stylesheet" href="../assets/css/style.css">

</head>

<?php
    require_once './components/header.php';
?>

<form action="addAction.php" method="post" enctype="multipart/form-data">
    <table width="25%" border="0">
        <tr>
            <td>Catégorie ID</td>
            <td><input type="text" name="categorie_id"></td>
        </tr>
        <tr>
            <td>Nom</td>
            <td><input type="text" name="nom" required></td>
        </tr>
        <tr>
            <td>Description</td>
            <td><textarea name="description"></textarea></td>
        </tr>
        <tr>
            <td>Prix</td>
            <td><input type="number" step="0.01" name="prix" required></td>
        </tr>
        <tr>
            <td>Photo de presentation</td>
            <td><input type="file" name="photo" accept="image/*" required></td>
        </tr>
        <tr>
            <td>Second Photo</td>
            <td><input type="file" name="second_photo" accept="image/*" required></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" name="submit" value="Ajouter"></td>
        </tr>
    </table>
</form>