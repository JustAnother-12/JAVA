import { showAlert,showAlert2 } from './cart.js';
document.addEventListener("DOMContentLoaded", function () {
    const editBtn = document.querySelector(".js-edit-btn");
    const submitBtn = document.querySelector(".js-submit-btn");
    const inputs = document.querySelectorAll("#full-name, #user-email, #user-phone, #user-address");

    editBtn.addEventListener("click", function () {
        inputs.forEach(input => input.disabled = false);
    });

    submitBtn.addEventListener("click", function () {
        const data = {
            fullName: document.getElementById("full-name").value,
            email: document.getElementById("user-email").value,
            phone: document.getElementById("user-phone").value,
            address: document.getElementById("user-address").value
        };

        fetch("/gui/account/update_user.php", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
        .then(res => res.json())
        .then(response => {
            if (response.success) {
                
                inputs.forEach(input => input.disabled = true);
            } else {
                showAlert2("Cập nhật thất bại: " + response.message);
            }
        })
        .catch(err => {
            console.error(err);
            showAlert2("Lỗi khi gửi yêu cầu cập nhật.");
        });
    });
});

