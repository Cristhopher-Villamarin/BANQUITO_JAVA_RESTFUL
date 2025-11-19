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
                    <label class="field field--plazo field--hidden" id="plazoWrapper">
                        <span>Plazo (meses, solo crédito)</span>
                        <input id="plazoMeses" name="plazoMeses" type="number" min="3" max="24" placeholder="Ej: 12">
                    </label>
                </article>
            </div>

            <article class="card card--full">
                <header>
                    <p class="card__eyebrow">Productos</p>
                    <h2>Selecciona productos del catálogo</h2>
                </header>

                <% if (lista != null && !lista.isEmpty()) { %>
                <div class="products-browser">
                    <div class="products-browser__scroll">
                        <div class="cards-grid">
                            <% for (Electrodomestico e : lista) {
                                   String nombreAttr = e.getNombre() != null ? e.getNombre().replace("\"", "&quot;") : "";
                                   String fotoAttr = e.getFotoUrl() != null ? e.getFotoUrl().replace("\"", "&quot;") : "";
                            %>
                                <article class="product-card"
                                         data-producto-id="<%= e.getIdElectrodomestico() %>"
                                         data-nombre="<%= nombreAttr %>"
                                         data-precio="<%= e.getPrecioVenta() %>"
                                         data-foto="<%= fotoAttr %>">
                                    <div class="product-card__media">
                                        <% if (e.getFotoUrl() != null && !e.getFotoUrl().isBlank()) { %>
                                            <img src="<%= e.getFotoUrl() %>" alt="Foto de <%= e.getNombre() %>" class="product-card__image">
                                        <% } else { %>
                                            <div class="product-card__placeholder">Sin imagen</div>
                                        <% } %>
                                    </div>
                                    <div class="product-card__body">
                                        <header class="product-card__header">
                                            <span class="product-card__id">ID #<%= e.getIdElectrodomestico() %></span>
                                            <h3 class="product-card__title"><%= e.getNombre() %></h3>
                                        </header>
                                        <p class="product-card__price">$<%= e.getPrecioVenta() %></p>
                                        <div class="product-card__actions">
                                            <div class="product-card__qty">
                                                <label>Cant.</label>
                                                <input type="number" class="product-card__qty-input" min="1" value="1">
                                            </div>
                                            <button type="button" class="primary-btn product-card__add">Añadir</button>
                                        </div>
                                    </div>
                                </article>
                            <% } %>
                        </div>
                    </div>
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
                                <th>Precio unitario</th>
                                <th>Cantidad</th>
                                <th>Total</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr class="table__empty-row">
                                <td colspan="6" class="table__empty">Aún no hay productos agregados.</td>
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
        const tableBody = document.querySelector('#productosSeleccionados tbody');

        function renderEmptyRow() {
            if (!tableBody.querySelector('tr')) {
                const emptyRow = document.createElement('tr');
                emptyRow.classList.add('table__empty-row');
                emptyRow.innerHTML = '<td colspan="6" class="table__empty">Aún no hay productos agregados.</td>';
                tableBody.appendChild(emptyRow);
            }
        }

        function formatCurrency(value) {
            return new Intl.NumberFormat('es-EC', { style: 'currency', currency: 'USD' }).format(value);
        }

        function addProduct(productoId, nombre, precioUnitario, cantidad, fotoUrl) {
            const existingRow = tableBody.querySelector(`tr[data-producto-id="${productoId}"]`);

            if (existingRow) {
                const cantidadInput = existingRow.querySelector('input[name="cantidad"]');
                const nuevaCantidad = parseInt(cantidadInput.value, 10) + cantidad;
                cantidadInput.value = nuevaCantidad;
                existingRow.querySelector('.cantidad-text').textContent = nuevaCantidad;
                const totalCell = existingRow.querySelector('.total-text');
                totalCell.textContent = formatCurrency(precioUnitario * nuevaCantidad);
                return;
            }

            if (tableBody.querySelector('.table__empty-row')) {
                tableBody.querySelector('.table__empty-row').remove();
            }

            const row = document.createElement('tr');
            row.dataset.productoId = productoId;
            row.dataset.precio = String(precioUnitario);

            const safeFoto = fotoUrl && fotoUrl.trim() !== '' ? fotoUrl : '';
            const thumbHtml = safeFoto
                ? '<img src="' + safeFoto + '" alt="Foto ' + nombre + '" class="mini-thumb">'
                : '<div class="mini-thumb mini-thumb--empty">Sin imagen</div>';

            row.innerHTML =
                '<td>' +
                    '<div class="mini-product-cell">' +
                        '<span class="mini-product-id">ID #' + productoId + '</span>' +
                        thumbHtml +
                    '</div>' +
                    '<input type="hidden" name="productoId" value="' + productoId + '">' +
                '</td>' +
                '<td>' + nombre + '</td>' +
                '<td class="price-text">' + formatCurrency(precioUnitario) + '</td>' +
                '<td>' +
                    '<div class="qty-controls">' +
                        '<button type="button" class="qty-btn" data-action="dec" aria-label="Disminuir cantidad">-</button>' +
                        '<span class="cantidad-text">' + cantidad + '</span>' +
                        '<button type="button" class="qty-btn" data-action="inc" aria-label="Aumentar cantidad">+</button>' +
                    '</div>' +
                    '<input type="hidden" name="cantidad" value="' + cantidad + '">' +
                '</td>' +
                '<td class="total-text">' + formatCurrency(precioUnitario * cantidad) + '</td>' +
                '<td class="table__actions">' +
                    '<button type="button" class="ghost-btn ghost-btn--inline" aria-label="Eliminar" data-remove="' + productoId + '">Eliminar</button>' +
                '</td>';
            tableBody.appendChild(row);
        }

        const productCards = document.querySelectorAll('.product-card');

        productCards.forEach(card => {
            const addButton = card.querySelector('.product-card__add');
            const qtyInput = card.querySelector('.product-card__qty-input');
            const productoId = card.dataset.productoId;
            const nombre = card.dataset.nombre || '';
            const precioUnitario = parseFloat(card.dataset.precio || '0');
            const fotoUrl = card.dataset.foto || '';

            if (!addButton || !qtyInput) {
                return;
            }

            addButton.addEventListener('click', () => {
                const cantidad = parseInt(qtyInput.value, 10);
                if (!cantidad || cantidad <= 0) {
                    alert('Ingresa una cantidad válida.');
                    return;
                }
                addProduct(productoId, nombre, precioUnitario, cantidad, fotoUrl);
            });
        });

        tableBody.addEventListener('click', (event) => {
            const qtyButton = event.target.closest('.qty-btn');
            if (qtyButton) {
                const row = qtyButton.closest('tr');
                if (!row) return;

                const action = qtyButton.dataset.action;
                const cantidadInput = row.querySelector('input[name="cantidad"]');
                const cantidadText = row.querySelector('.cantidad-text');
                const totalCell = row.querySelector('.total-text');
                const precioUnitario = parseFloat(row.dataset.precio || '0');

                if (!cantidadInput || !cantidadText || !totalCell || !precioUnitario) return;

                let cantidadActual = parseInt(cantidadInput.value, 10) || 0;
                if (action === 'inc') {
                    cantidadActual += 1;
                } else if (action === 'dec') {
                    cantidadActual -= 1;
                }

                if (cantidadActual <= 0) {
                    row.remove();
                    if (!tableBody.querySelector('tr')) {
                        renderEmptyRow();
                    }
                    return;
                }

                cantidadInput.value = cantidadActual;
                cantidadText.textContent = cantidadActual;
                totalCell.textContent = formatCurrency(precioUnitario * cantidadActual);
                return;
            }

            const button = event.target.closest('button[data-remove]');
            if (button) {
                const row = button.closest('tr');
                if (row) {
                    row.remove();
                    if (!tableBody.querySelector('tr')) {
                        renderEmptyRow();
                    }
                }
            }
        });

        renderEmptyRow();
    })();
</script>
<% } %>

<script>
    (function () {
        const radios = document.querySelectorAll('input[name="formaPago"]');
        const plazoWrapper = document.getElementById('plazoWrapper');
        const plazoInput = document.getElementById('plazoMeses');

        function togglePlazo() {
            const creditoSeleccionado = document.querySelector('input[name="formaPago"]:checked')?.value === 'CREDITO_DIRECTO';
            plazoWrapper.classList.toggle('field--hidden', !creditoSeleccionado);
            if (!creditoSeleccionado) {
                plazoInput.value = '';
            }
        }

        radios.forEach(radio => radio.addEventListener('change', togglePlazo));
        togglePlazo();
    })();
</script>
</body>
</html>
