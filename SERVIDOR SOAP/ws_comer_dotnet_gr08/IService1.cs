using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace ws_comer_dotnet_gr08
{
    [ServiceContract]
    public interface IService1
    {
        // Autenticación
        [OperationContract]
        LoginResponse Login(LoginRequest request);

        [OperationContract]
        HealthResponse GetAuthHealth();

        [OperationContract]
        UsuariosInfoResponse GetAuthUsuarios();

        // Gestión de electrodomésticos (CRUD)
        [OperationContract]
        HealthResponse GetElectrodomesticoHealth();

        [OperationContract]
        List<ElectrodomesticoResponse> ListarElectrodomesticos();

        [OperationContract]
        ElectrodomesticoResponse ObtenerElectrodomesticoPorId(int id);

        [OperationContract]
        ElectrodomesticoResponse CrearElectrodomestico(ElectrodomesticoRequest request);

        [OperationContract]
        ElectrodomesticoResponse ActualizarElectrodomestico(int id, ElectrodomesticoRequest request);

        [OperationContract]
        bool EliminarElectrodomestico(int id);

        // Ventas
        [OperationContract]
        HealthResponse GetVentasHealth();

        [OperationContract]
        VentaResponse ProcesarVenta(VentaRequest request);
    }

    // Se conservan los DataContracts en ComercializadoraDtos.cs
}
