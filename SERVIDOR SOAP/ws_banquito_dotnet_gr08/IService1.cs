using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace ws_banquito_dotnet_gr08
{
    // NOTA: puede usar el comando "Rename" del menú "Refactorizar" para cambiar el nombre de interfaz "IService1" en el código y en el archivo de configuración a la vez.
    [ServiceContract]
    public interface IService1
    {
        [OperationContract]
        EvaluacionCreditoResponse EvaluarCredito(EvaluacionCreditoRequest request);

        [OperationContract]
        EvaluacionCreditoResponse VerificarSujetoCredito(string cedula);

        [OperationContract]
        List<CreditoResponse> ObtenerCreditosActivosPorCliente(string cedula);

        [OperationContract]
        decimal? ObtenerMontoMaximo(string cedula);

        [OperationContract]
        List<AmortizacionDTO> ObtenerTablaAmortizacion(int idCredito);

        [OperationContract]
        TablaAmortizacionResponse ObtenerTablaAmortizacionComercializadora(int idCredito);

        // TODO: agregue aquí sus operaciones de servicio
    }

    // Utilice un contrato de datos, como se ilustra en el ejemplo siguiente, para agregar tipos compuestos a las operaciones de servicio.
    // Puede agregar archivos XSD al proyecto. Después de compilar el proyecto, puede usar directamente los tipos de datos definidos aquí, con el espacio de nombres "ws_banquito_dotnet_gr08.ContractType".
    [DataContract]
    public class CompositeType
    {
        bool boolValue = true;
        string stringValue = "Hello ";

        [DataMember]
        public bool BoolValue
        {
            get { return boolValue; }
            set { boolValue = value; }
        }

        [DataMember]
        public string StringValue
        {
            get { return stringValue; }
            set { stringValue = value; }
        }
    }
}
