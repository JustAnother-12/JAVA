<?php
session_start();
header('Content-Type: application/json');

if (isset($_SESSION['fullname'])) {
    echo json_encode([
        'loggedIn' => true,
        'username' => $_SESSION['fullname']
    ]);
} else {
    echo json_encode(['loggedIn' => false]);
}
?>
