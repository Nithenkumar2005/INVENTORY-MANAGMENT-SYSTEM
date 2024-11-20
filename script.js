const apiBaseUrl = "http://127.0.0.1:5000"; // Update this if your Flask server runs on a different URL

document.addEventListener("DOMContentLoaded", () => {
    // Handle Add Item Form Submission
    const addItemForm = document.getElementById("add-item-form");
    addItemForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const name = document.getElementById("name").value;
        const quantity = document.getElementById("quantity").value;
        const price = document.getElementById("price").value;

        const response = await fetch(`${apiBaseUrl}/add_item`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ name, quantity, price }),
        });

        const result = await response.json();
        alert(result.message);
        addItemForm.reset();
    });

    // Handle View Items Button Click
    const viewItemsButton = document.getElementById("view-items-button");
    const itemsTableBody = document.querySelector("#items-table tbody");

    viewItemsButton.addEventListener("click", async () => {
        const response = await fetch(`${apiBaseUrl}/view_items`);
        const result = await response.json();

        if (result.items) {
            itemsTableBody.innerHTML = ""; // Clear existing rows
            result.items.forEach((item) => {
                const row = document.createElement("tr");

                // Parse item details
                const [id, name, quantity, price] = item.split("\t");

                row.innerHTML = `
                    <td>${id}</td>
                    <td>${name}</td>
                    <td>${quantity}</td>
                    <td>${price}</td>
                    <td>
                        <button class="update" data-id="${id}">Update</button>
                        <button class="delete" data-id="${id}">Delete</button>
                    </td>
                `;
                itemsTableBody.appendChild(row);
            });
        } else {
            alert(result.message);
        }
    });

    // Delegate Actions (Update & Delete)
    itemsTableBody.addEventListener("click", async (e) => {
        if (e.target.classList.contains("update")) {
            const id = e.target.getAttribute("data-id");
            const newQuantity = prompt("Enter new quantity:");
            const newPrice = prompt("Enter new price:");

            if (newQuantity && newPrice) {
                const response = await fetch(`${apiBaseUrl}/update_item`, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ id, quantity: newQuantity, price: newPrice }),
                });

                const result = await response.json();
                alert(result.message);
            }
        } else if (e.target.classList.contains("delete")) {
            const id = e.target.getAttribute("data-id");
            const confirmation = confirm("Are you sure you want to delete this item?");
            if (confirmation) {
                const response = await fetch(`${apiBaseUrl}/delete_item`, {
                    method: "DELETE",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ id }),
                });

                const result = await response.json();
                alert(result.message);
            }
        }
    });
});
