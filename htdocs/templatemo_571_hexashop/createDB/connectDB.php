<?php

class Database
{
    // 172.80.237.54
    private static $servername = "localhost";
    private static $username = "root";
    private static $password = "";
    private static $dbname = "e-commerce";
    private static $conn = null;

    public static function getConnection()
    {
        if (self::$conn === null) {
            try {
                self::$conn = new PDO(
                    "mysql:host=" . self::$servername . ";dbname=" . self::$dbname,
                    self::$username,
                    self::$password
                );
                self::$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            } catch (PDOException $e) {
                die("Échec de la connexion : " . $e->getMessage());
            }
        }
        return self::$conn;
    }

    public static function closeConnection()
    {
        if (self::$conn !== null) {
            self::$conn = null;
        }
    }
}
?>


<!--
private static $servername = "localhost";
    private static $username = "root";
    private static $password = "steve";
    private static $dbname = "db_S2_ root"; -->