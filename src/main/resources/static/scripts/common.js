const icon = document.querySelector(".menu-toggle")
const menu = document.querySelector(".menu")
function toggleMenu() {
    if (menu.classList.contains("expanded")) {
        menu.classList.remove("expanded")
        icon.querySelector('a').innerHTML = '<i id="toggle-icon" class="far fa-plus-square"></i>'
    } else {
        menu.classList.add("expanded")
        icon.querySelector('a').innerHTML = '<i id="toggle-icon" class="far fa-minus-square"></i>'
    }
}
icon.addEventListener("click", toggleMenu, false)

function updateUrl(select) {
    let selectedOption = select.options[select.selectedIndex].value; // array[np. 2].value czyli index 2 i jego value to /all-movies
    window.location.href = selectedOption; // dodaje do urla wybraną wartość
}

function showForm(formId) {
    var forms = document.querySelectorAll('form');
    forms.forEach(function(form) {
        form.classList.add('hidden');
    });
    var selectedForm = document.getElementById(formId);
    if (selectedForm) {
        selectedForm.classList.remove('hidden');
    }
}

function dateFormatter() {
    let commentDate = document.querySelector(".comment-time").innerHTML;
    let date = new Date(commentDate);
    console.log(date.getSeconds());
    commentDate = date.getSeconds();
}

// function searchUser() {
//     let searchInput = document.querySelector("#user").value;
//     fetch("/search")
//         .then((res) => {
//             if (!res.ok) {
//                 throw new Error("Unable to get feedback");
//             } else {
//                 return res.json();
//             }
//         })
//         .then((users) => {
//             console.log(use);
//         })
// }