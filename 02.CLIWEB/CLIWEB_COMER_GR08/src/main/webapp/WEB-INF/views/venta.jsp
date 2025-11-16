<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ec.edu.monster.model.Electrodomestico"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrar venta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/venta.css">
</head>
<body class="venta-body">
<%
    List<Electrodomestico> lista = (List<Electrodomestico>) request.getAttribute("electrodomesticos");
    String error = (String) request.getAttribute("error");
    int disponibles = lista != null ? lista.size() : 0;
%>
<div class="flow-shell">
    <header class="hero">
        <div class="hero__content">
            <p class="hero__eyebrow">Flujo comercial</p>
            <h1>Registrar nueva venta</h1>
            <p>Captura datos del cliente, define la forma de pago y agrega productos en una sola carrera.</p>
            <div class="hero__stats">
                <article>
                    <h2><%= disponibles %></h2>
                    <p>Referencias disponibles</p>
                </article>
                <article>
                    <h2>33%</h2>
                    <p>Descuento en pago en efectivo</p>
                </article>
            </div>
        </div>
        <div class="hero__actions">
            <a href="${pageContext.request.contextPath}/user/menu" class="ghost-btn">Volver al menú</a>
        </div>
    </header>

    <% if (error != null) { %>
        <div class="alert"><%= error %></div>
    <% } %>

    <section class="panel">
        <form method="post" action="${pageContext.request.contextPath}/user/venta" class="venta-form">
            <div class="form-grid">
                <article class="card">
                    <header>
                        <p class="card__eyebrow">Datos del cliente</p>
                        <h2>Identificación</h2>
                    </header>
                    <label class="field">
                        <span>Cédula</span>
                        <input id="cedula" name="cedula" type="text" required>
                    </label>
                    <label class="field">
                        <span>Nombre completo</span>
                        <input id="nombre" name="nombre" type="text" required>
                    </label>
                </article>
                <article class="card">
                    <header>
                        <p class="card__eyebrow">Condiciones</p>
                        <h2>Forma de pago</h2>
                    </header>
                    <div class="radio-list">
                        <label class="radio">
                            <input type="radio" name="formaPago" value="EFECTIVO" checked>
                            <div>
                                <strong>Efectivo</strong>
                                <p>Aplica descuento automático del 33%.</p>
                            </div>
                        </label>
                        <label class="radio">
                            <input type="radio" name="formaPago" value="CREDITO_DIRECTO">
                            <div>
                                <strong>Crédito directo</strong>
                                <p>Valida aprobación con Banquito y define plazo.</p>
                            </div>
                        </label>
                    </div>
                    <label class="field">
                        <span>Plazo (meses, solo crédito)</span>
                        <input id="plazoMeses" name="plazoMeses" type="number" min="3" max="24" placeholder="Ej: 12">
                    </label>
                </article>
            </div>

            <article class="card card--full">
                <header>
                    <p class="card__eyebrow">Productos</p>
                    <div>
                        <h2>Buscar y agregar referencias</h2>
                        <p>Usa el buscador y el selector para añadir productos con su cantidad.</p>
                    </div>
                </header>

                <% if (lista != null && !lista.isEmpty()) { %>
                <div class="product-selector">
                    <div class="product-selector__control">
                        <label for="productSearch">Buscar producto</label>
                        <input type="text" id="productSearch" placeholder="Escribe nombre o ID">
                    </div>
                    <div class="product-selector__control">
                        <label for="productoSelect">Listado</label>
                        <select id="productoSelect">
                            <% for (Electrodomestico e : lista) { %>
                                <option value="<%= e.getIdElectrodomestico() %>"
                                        data-nombre="<%= e.getNombre() %>"
                                        data-precio="<%= e.getPrecioVenta() %>">
                                    <%= e.getIdElectrodomestico() %> - <%= e.getNombre() %> ($<%= e.getPrecioVenta() %>)
                                </option>
                            <% } %>
                        </select>
                    </div>
                    <div class="product-selector__control">
                        <label for="cantidadInput">Cantidad</label>
                        <input type="number" id="cantidadInput" min="1" value="1">
                    </div>
                    <button type="button" class="primary-btn product-selector__add" id="agregarProducto">Agregar</button>
                </div>

                <div class="selected-products">
                    <div class="selected-products__header">
                        <div>
                            <h3>Productos seleccionados</h3>
                            <p>Ajusta cantidades o elimina referencias antes de procesar.</p>
                        </div>
                    </div>
                    <div class="table-wrapper">
                        <table class="table" id="productosSeleccionados">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Producto</th>
                                <th>Cantidad</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr class="table__empty-row">
                                <td colspan="4" class="table__empty">Aún no hay productos agregados.</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <% } else { %>
                    <div class="alert">No hay productos disponibles para seleccionar.</div>
                <% } %>
            </article>

            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/user/menu" class="ghost-btn">Cancelar</a>
                <button type="submit" class="primary-btn">Procesar venta</button>
            </div>
        </form>
    </section>
</div>

<% if (lista != null && !lista.isEmpty()) { %>
<script>
    (function () {
        const searchInput = document.getElementById('productSearch');
        const select = document.getElementById('productoSelect');
        const qtyInput = document.getElementById('cantidadInput');
        const addButton = document.getElementById('agregarProducto');
        const tableBody = document.querySelector('#productosSeleccionados tbody');

        function filterOptions() {
            const term = searchInput.value.toLowerCase();
            Array.from(select.options).forEach(option => {
                const text = option.textContent.toLowerCase();
                option.hidden = term && !text.includes(term);
            });
            const firstVisible = Array.from(select.options).find(opt => !opt.hidden);
            if (firstVisible) {
                select.value = firstVisible.value;
            }
        }

        function renderEmptyRow() {
            if (!tableBody.querySelector('tr')) {
                const emptyRow = document.createElement('tr');
                emptyRow.classList.add('table__empty-row');
                emptyRow.innerHTML = '<td colspan="4" class="table__empty">Aún no hay productos agregados.</td>';
                tableBody.appendChild(emptyRow);
            }
        }

        function addProduct() {
            const option = select.options[select.selectedIndex];
            if (!option) {
                alert('Selecciona un producto.');
                return;
            }
            const cantidad = parseInt(qtyInput.value, 10);
            if (!cantidad || cantidad <= 0) {
                alert('Ingresa una cantidad válida.');
                return;
            }

            const productoId = option.value;
            const nombre = option.dataset.nombre;
            const existingRow = tableBody.querySelector(`tr[data-producto-id="${productoId}"]`);

            if (existingRow) {
                const cantidadInput = existingRow.querySelector('input[name="cantidad"]');
                const nuevaCantidad = parseInt(cantidadInput.value, 10) + cantidad;
                cantidadInput.value = nuevaCantidad;
                existingRow.querySelector('.cantidad-text').textContent = nuevaCantidad;
                return;
            }

            if (tableBody.querySelector('.table__empty-row')) {
                tableBody.querySelector('.table__empty-row').remove();
            }

            const row = document.createElement('tr');
            row.dataset.productoId = productoId;
            row.innerHTML = `
                <td>${productoId}<input type="hidden" name="productoId" value="${productoId}"></td>
                <td>${nombre}</td>
                <td>
                    <span class="cantidad-text">${cantidad}</span>
                    <input type="hidden" name="cantidad" value="${cantidad}">
                </td>
                <td class="table__actions">
                    <button type="button" class="ghost-btn ghost-btn--inline" aria-label="Eliminar" data-remove="${productoId}">Eliminar</button>
                </td>
            `;
            tableBody.appendChild(row);
        }

        function removeProduct(productoId) {
            const row = tableBody.querySelector(`tr[data-producto-id="${productoId}"]`);
            if (row) {
                row.remove();
                if (!tableBody.querySelector('tr')) {
                    renderEmptyRow();
                }
            }
        }

        searchInput.addEventListener('input', filterOptions);
        addButton.addEventListener('click', addProduct);
        tableBody.addEventListener('click', (event) => {
            const button = event.target.closest('button[data-remove]');
            if (button) {
                removeProduct(button.dataset.remove);
            }
        });

        renderEmptyRow();
    })();
</script>
<% } %>
</body>
</html>
