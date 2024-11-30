<?php

require_once 'createDB/connectDB.php';

try {
    $conn = Database::getConnection();

    $sql = "SELECT * FROM produits WHERE 1=1";

    $params = [];

    if (!empty($_GET['name'])) {
        $sql .= " AND nom LIKE :name";
        $params[':name'] = '%' . $_GET['name'] . '%';
    }

    if (!empty($_GET['category'])) {
        $sql .= " AND categorie_id = :category";
        $params[':category'] = $_GET['category'];
    }

    if (!empty($_GET['min_price'])) {
        $sql .= " AND prix >= :min_price";
        $params[':min_price'] = $_GET['min_price'];
    }

    if (!empty($_GET['max_price'])) {
        $sql .= " AND prix <= :max_price";
        $params[':max_price'] = $_GET['max_price'];
    }

    $stmt = $conn->prepare($sql);
    $stmt->execute($params);
    $products = $stmt->fetchAll(PDO::FETCH_ASSOC);

} catch (PDOException $e) {
    die("Erreur : " . $e->getMessage());
} finally {
    Database::closeConnection();
}
?>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link href="https://fonts.googleapis.com/css?family=Poppins:100,200,300,400,500,600,700,800,900&display=swap"
        rel="stylesheet">

    <title>Hexashop - Product Listing Page</title>

    <!-- Additional CSS Files -->
    <link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css">

    <link rel="stylesheet" type="text/css" href="assets/css/font-awesome.css">

    <link rel="stylesheet" href="assets/css/templatemo-hexashop.css">

    <link rel="stylesheet" href="assets/css/owl-carousel.css">

    <link rel="stylesheet" href="assets/css/lightbox.css">

    <!-- <link rel="stylesheet" href="assets/css/style.css"> -->
    <!--

TemplateMo 571 Hexashop

https://templatemo.com/tm-571-hexashop

-->
</head>

<body>

    <!-- ***** Preloader Start ***** -->
    <div id="preloader">
        <div class="jumper">
            <div></div>
            <div></div>
            <div></div>
        </div>
    </div>
    <!-- ***** Preloader End ***** -->


    <!-- ***** Header Area Start ***** -->
    <header class="header-area header-sticky">
        <div class="container">
            <div class="row">
                <div class="col-12">
                    <nav class="main-nav">
                        <!-- ***** Logo Start ***** -->
                        <a href="index.php" class="logo">
                            <img src="assets/images/logo.png">
                        </a>
                        <!-- ***** Logo End ***** -->
                        <!-- ***** Menu Start ***** -->
                        <ul class="nav">
                            <li class="scroll-to-section"><a href="index.php" class="active">Home</a></li>
                            <li class="scroll-to-section"><a href="index.php">Men's</a></li>
                            <li class="scroll-to-section"><a href="index.php">Women's</a></li>
                            <li class="scroll-to-section"><a href="index.php">Kid's</a></li>
                            <li class="submenu">
                                <a href="javascript:;">Pages</a>
                                <ul>
                                    <li><a href="about.html">About Us</a></li>
                                    <li><a href="products.html">Products</a></li>
                                    <li><a href="single-product.php">Single Product</a></li>
                                    <li><a href="contact.html">Contact Us</a></li>
                                </ul>
                            </li>
                            <li class="submenu">
                                <a href="javascript:;">Features</a>
                                <ul>
                                    <li><a href="#">Features Page 1</a></li>
                                    <li><a href="#">Features Page 2</a></li>
                                    <li><a href="#">Features Page 3</a></li>
                                    <li><a rel="nofollow" href="https://templatemo.com/page/4" target="_blank">Template
                                            Page 4</a></li>
                                </ul>
                            </li>
                            <li class="scroll-to-section"><a href="index.php">Explore</a></li>
                        </ul>
                        <a class='menu-trigger'>
                            <span>Menu</span>
                        </a>
                        <!-- ***** Menu End ***** -->
                    </nav>
                </div>
            </div>
        </div>
    </header>
    <!-- ***** Header Area End ***** -->

    <!-- ***** Main Banner Area Start ***** -->
    <div class="page-heading" id="top">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="inner-content">
                        <h2>Check Our Products</h2>
                        <span>Awesome &amp; Creative HTML CSS layout by TemplateMo</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- ***** Main Banner Area End ***** -->


    <!-- ***** Products Area Starts ***** -->
    <section class="section" id="products">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="section-heading">
                        <h2>Our Latest Products</h2>
                        <span>Check out all of our products.</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <div class="mt-3">
                <form method="GET" action="products.php" class="d-flex align-items-center">
                    <div class="form-group mx-2">
                        <label for="name">Nom :</label>
                        <input type="text" id="name" name="name" class="form-control"
                            value="<?= htmlspecialchars($_GET['name'] ?? '') ?>">
                    </div>
                    <div class="form-group mx-2">
                        <label for="category">Catégorie :</label>
                        <select id="category" name="category" class="form-control">
                            <option value="">Toutes</option>

                            <!-- la liste des categories -->
                            <?php
                            try {
                                $conn = Database::getConnection();
                                $categories = $conn->query("SELECT id, Nom_categorie FROM categorie")->fetchAll(PDO::FETCH_ASSOC);
                                foreach ($categories as $category) {
                                    $selected = ($_GET['category'] ?? '') == $category['id'] ? 'selected' : '';
                                    echo "<option value='{$category['id']}' $selected>{$category['Nom_categorie']}</option>";
                                }
                            } catch (PDOException $e) {
                                echo "Erreur de chargement des catégories : " . $e->getMessage();
                            } finally {
                                Database::closeConnection();
                            }
                            ?>

                        </select>
                    </div>
                    <div class="form-group mx-2">
                        <label for="min_price">Prix min :</label>
                        <input type="number" id="min_price" name="min_price" class="form-control" step="0.01"
                            value="<?= htmlspecialchars($_GET['min_price'] ?? '') ?>">
                    </div>
                    <div class="form-group mx-2">
                        <label for="max_price">Prix max :</label>
                        <input type="number" id="max_price" name="max_price" class="form-control" step="0.01"
                            value="<?= htmlspecialchars($_GET['max_price'] ?? '') ?>">
                    </div>
                    <div class="form-group mx-2 mt-4">
                    <button type="submit" class="btn search-button mx-2 mt-1">Rechercher</button>
                    </div>
                </form>
            </div>

            <!-- La boucle qui incrémentes l'affichage des produits -->
            <div class="row mt-5">
                <?php if ($products): ?>
                    <?php foreach ($products as $product): ?>
                        <div class="col-lg-4">
                            <div class="item">
                                <div class="thumb">
                                    <div class="hover-content">
                                        <ul>
                                            <li><a href="single-product.php?id=<?= $product['id']; ?>"><i
                                                        class="fa fa-eye"></i></a></li>
                                            <li><a href="single-product.php?id=<?= $product['id']; ?>"><i
                                                        class="fa fa-star"></i></a></li>
                                            <li><a href="single-product.php?id=<?= $product['id']; ?>"><i
                                                        class="fa fa-shopping-cart"></i></a></li>
                                        </ul>
                                    </div>
                                    <img src="assets/images/<?= $product['url_photo']; ?>"
                                        alt="<?= htmlspecialchars($product['nom']); ?>">
                                </div>
                                <div class="down-content">
                                    <h4><?= htmlspecialchars($product['nom']); ?></h4>
                                    <span>$<?= number_format($product['prix'], 2); ?></span>
                                    <ul class="stars">
                                        <?php

                                        // l'affichage des étoiles 
                                        $rating = $product['note'];
                                        $fullStars = floor($rating);
                                        $halfStar = ($rating - $fullStars) >= 0.5;
                                        $emptyStars = 5 - $fullStars - ($halfStar ? 1 : 0);
                                        ?>
                                        <?php for ($i = 0; $i < $fullStars; $i++): ?>
                                            <li><i class="fa fa-star"></i></li>
                                        <?php endfor; ?>
                                        <?php if ($halfStar): ?>
                                            <li><i class="fa fa-star-half-o"></i></li>
                                        <?php endif; ?>
                                        <?php for ($i = 0; $i < $emptyStars; $i++): ?>
                                            <li><i class="fa fa-star-o"></i></li>
                                        <?php endfor; ?>
                                        

                                    </ul>
                                </div>
                            </div>
                        </div>
                    <?php endforeach; ?>
                <?php else: ?>
                    <p>Aucun produit disponible.</p>
                <?php endif; ?>
            </div>
        </div>
    </section>
    <!-- ***** Products Area Ends ***** -->

    <!-- ***** Footer Start ***** -->
    <footer>
        <div class="container">
            <div class="row">
                <div class="col-lg-3">
                    <div class="first-item">
                        <div class="logo">
                            <img src="assets/images/white-logo.png" alt="hexashop ecommerce templatemo">
                        </div>
                        <ul>
                            <li><a href="#">16501 Collins Ave, Sunny Isles Beach, FL 33160, United States</a></li>
                            <li><a href="#">hexashop@company.com</a></li>
                            <li><a href="#">010-020-0340</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-lg-3">
                    <h4>Shopping &amp; Categories</h4>
                    <ul>
                        <li><a href="#">Men’s Shopping</a></li>
                        <li><a href="#">Women’s Shopping</a></li>
                        <li><a href="#">Kid's Shopping</a></li>
                    </ul>
                </div>
                <div class="col-lg-3">
                    <h4>Useful Links</h4>
                    <ul>
                        <li><a href="#">Homepage</a></li>
                        <li><a href="#">About Us</a></li>
                        <li><a href="#">Help</a></li>
                        <li><a href="#">Contact Us</a></li>
                    </ul>
                </div>
                <div class="col-lg-3">
                    <h4>Help &amp; Information</h4>
                    <ul>
                        <li><a href="#">Help</a></li>
                        <li><a href="#">FAQ's</a></li>
                        <li><a href="#">Shipping</a></li>
                        <li><a href="#">Tracking ID</a></li>
                    </ul>
                </div>
                <div class="col-lg-12">
                    <div class="under-footer">
                        <p>Copyright © 2022 HexaShop Co., Ltd. All Rights Reserved.

                            <br>Design: <a href="https://templatemo.com" target="_parent"
                                title="free css templates">TemplateMo</a>
                        </p>
                        <ul>
                            <li><a href="#"><i class="fa fa-facebook"></i></a></li>
                            <li><a href="#"><i class="fa fa-twitter"></i></a></li>
                            <li><a href="#"><i class="fa fa-linkedin"></i></a></li>
                            <li><a href="#"><i class="fa fa-behance"></i></a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </footer>


    <!-- jQuery -->
    <script src="assets/js/jquery-2.1.0.min.js"></script>

    <!-- Bootstrap -->
    <script src="assets/js/popper.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>

    <!-- Plugins -->
    <script src="assets/js/owl-carousel.js"></script>
    <script src="assets/js/accordions.js"></script>
    <script src="assets/js/datepicker.js"></script>
    <script src="assets/js/scrollreveal.min.js"></script>
    <script src="assets/js/waypoints.min.js"></script>
    <script src="assets/js/jquery.counterup.min.js"></script>
    <script src="assets/js/imgfix.min.js"></script>
    <script src="assets/js/slick.js"></script>
    <script src="assets/js/lightbox.js"></script>
    <script src="assets/js/isotope.js"></script>

    <!-- Global Init -->
    <script src="assets/js/custom.js"></script>

    <script>

        $(function () {
            var selectedClass = "";
            $("p").click(function () {
                selectedClass = $(this).attr("data-rel");
                $("#portfolio").fadeTo(50, 0.1);
                $("#portfolio div").not("." + selectedClass).fadeOut();
                setTimeout(function () {
                    $("." + selectedClass).fadeIn();
                    $("#portfolio").fadeTo(50, 1);
                }, 500);

            });
        });

    </script>

</body>

</html>