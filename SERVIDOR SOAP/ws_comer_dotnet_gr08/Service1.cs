using System;
using System.Collections.Generic;
using System.Text;
using ec.edu.monster.service;

namespace ws_comer_dotnet_gr08
{
    public class Service1 : IService1
    {
        private readonly AuthService _authService = new AuthService();
        private readonly ElectrodomesticoService _electrodomesticoService = new ElectrodomesticoService();
        private readonly VentaService _ventaService = new VentaService();

        // ==========================
        //  Autenticación
        // ==========================

        public LoginResponse Login(LoginRequest request)
        {
            if (request == null || string.IsNullOrWhiteSpace(request.Username) || string.IsNullOrWhiteSpace(request.Password))
            {
                return LoginResponse.Fallido("Credenciales inválidas");
            }

            return _authService.Autenticar(request);
        }

        public HealthResponse GetAuthHealth()
        {
            return new HealthResponse
            {
                Status = "OK",
                Message = "Auth Service is running",
                Timestamp = DateTime.Now
            };
        }

        public UsuariosInfoResponse GetAuthUsuarios()
        {
            return new UsuariosInfoResponse
            {
                Usuarios = _authService.ListarUsuariosDescripcion(),
                Timestamp = DateTime.Now
            };
        }

        // ==========================
        //  Electrodomésticos (CRUD)
        // ==========================

        public HealthResponse GetElectrodomesticoHealth()
        {
            return new HealthResponse
            {
                Status = "OK",
                Message = "Electrodomestico Service is running",
                Timestamp = DateTime.Now
            };
        }

        public List<ElectrodomesticoResponse> ListarElectrodomesticos()
        {
            return _electrodomesticoService.ListarTodos();
        }

        public ElectrodomesticoResponse ObtenerElectrodomesticoPorId(int id)
        {
            if (id <= 0)
            {
                return null;
            }

            return _electrodomesticoService.BuscarPorId(id);
        }

        public ElectrodomesticoResponse CrearElectrodomestico(ElectrodomesticoRequest request)
        {
            if (request == null || string.IsNullOrWhiteSpace(request.Nombre) || request.PrecioVenta <= 0)
            {
                return null;
            }

            return _electrodomesticoService.Crear(request);
        }

        public ElectrodomesticoResponse ActualizarElectrodomestico(int id, ElectrodomesticoRequest request)
        {
            if (id <= 0 || request == null || string.IsNullOrWhiteSpace(request.Nombre) || request.PrecioVenta <= 0)
            {
                return null;
            }

            return _electrodomesticoService.Actualizar(id, request);
        }

        public bool EliminarElectrodomestico(int id)
        {
            if (id <= 0)
            {
                return false;
            }

            return _electrodomesticoService.Eliminar(id);
        }

        // ==========================
        //  Ventas
        // ==========================

        public HealthResponse GetVentasHealth()
        {
            return new HealthResponse
            {
                Status = "OK",
                Message = "Comercializadora Service is running",
                Timestamp = DateTime.Now
            };
        }

        public VentaResponse ProcesarVenta(VentaRequest request)
        {
            if (request == null || string.IsNullOrWhiteSpace(request.CedulaCliente))
            {
                return new VentaResponse
                {
                    VentaExitosa = false,
                    Mensaje = "Datos de venta inválidos"
                };
            }

            if (request.Detalles == null || request.Detalles.Count == 0)
            {
                return new VentaResponse
                {
                    VentaExitosa = false,
                    Mensaje = "Debe incluir al menos un electrodoméstico"
                };
            }

            if (string.IsNullOrWhiteSpace(request.FormaPago))
            {
                return new VentaResponse
                {
                    VentaExitosa = false,
                    Mensaje = "Debe especificar la forma de pago"
                };
            }

            return _ventaService.ProcesarVenta(request);
        }
    }
}
