using System;
using System.Collections.Generic;
using System.Runtime.Serialization;

namespace ws_comer_dotnet_gr08
{
    [DataContract]
    public class LoginRequest
    {
        [DataMember]
        public string Username { get; set; }

        [DataMember]
        public string Password { get; set; }
    }

    [DataContract]
    public class LoginResponse
    {
        public LoginResponse()
        {
            Timestamp = DateTime.Now;
        }

        public LoginResponse(bool autenticado, string mensaje, string username, string rol)
            : this()
        {
            Autenticado = autenticado;
            Mensaje = mensaje;
            Username = username;
            Rol = rol;
            if (autenticado)
            {
                Token = GenerarToken();
            }
        }

        public static LoginResponse Exitoso(string username, string rol)
        {
            return new LoginResponse(true, "Autenticación exitosa", username, rol);
        }

        public static LoginResponse Fallido(string mensaje)
        {
            return new LoginResponse(false, mensaje, null, null);
        }

        private string GenerarToken()
        {
            return "TOKEN_" + Username + "_" + DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
        }

        [DataMember]
        public bool Autenticado { get; set; }

        [DataMember]
        public string Mensaje { get; set; }

        [DataMember]
        public string Username { get; set; }

        [DataMember]
        public string Rol { get; set; }

        [DataMember]
        public string Token { get; set; }

        [DataMember]
        public DateTime Timestamp { get; set; }
    }

    [DataContract]
    public class ElectrodomesticoRequest
    {
        [DataMember]
        public string Nombre { get; set; }

        [DataMember]
        public decimal PrecioVenta { get; set; }
    }

    [DataContract]
    public class ElectrodomesticoResponse
    {
        [DataMember]
        public int IdElectrodomestico { get; set; }

        [DataMember]
        public string Nombre { get; set; }

        [DataMember]
        public decimal PrecioVenta { get; set; }
    }

    [DataContract]
    public class DetalleVentaRequest
    {
        [DataMember]
        public string IdElectrodomestico { get; set; }

        [DataMember]
        public string Cantidad { get; set; }
    }

    [DataContract]
    public class DetalleVentaResponse
    {
        [DataMember]
        public string IdElectrodomestico { get; set; }

        [DataMember]
        public string NombreElectrodomestico { get; set; }

        [DataMember]
        public string Cantidad { get; set; }

        [DataMember]
        public decimal PrecioUnitario { get; set; }

        [DataMember]
        public decimal SubtotalLinea { get; set; }
    }

    [DataContract]
    public class VentaRequest
    {
        [DataMember]
        public string CedulaCliente { get; set; }

        [DataMember]
        public string NombreCliente { get; set; }

        [DataMember]
        public string FormaPago { get; set; }

        [DataMember]
        public int? PlazoMeses { get; set; }

        [DataMember]
        public List<DetalleVentaRequest> Detalles { get; set; }
    }

    [DataContract]
    public class VentaResponse
    {
        [DataMember]
        public bool VentaExitosa { get; set; }

        [DataMember]
        public string Mensaje { get; set; }

        [DataMember]
        public int? IdFactura { get; set; }

        [DataMember]
        public decimal Subtotal { get; set; }

        [DataMember]
        public decimal Descuento { get; set; }

        [DataMember]
        public decimal Total { get; set; }

        [DataMember]
        public string FormaPago { get; set; }

        [DataMember]
        public string EstadoFactura { get; set; }

        [DataMember]
        public List<DetalleVentaResponse> Detalles { get; set; }
    }

    [DataContract]
    public class HealthResponse
    {
        [DataMember]
        public string Status { get; set; }

        [DataMember]
        public string Message { get; set; }

        [DataMember]
        public DateTime Timestamp { get; set; }
    }

    [DataContract]
    public class UsuariosInfoResponse
    {
        [DataMember]
        public Dictionary<string, string> Usuarios { get; set; }

        [DataMember]
        public DateTime Timestamp { get; set; }
    }
}
