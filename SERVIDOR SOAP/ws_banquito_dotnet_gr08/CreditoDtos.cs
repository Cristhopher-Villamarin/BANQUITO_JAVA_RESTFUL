using System;
using System.Collections.Generic;
using System.Runtime.Serialization;

namespace ws_banquito_dotnet_gr08
{
    [DataContract]
    public class EvaluacionCreditoRequest
    {
        [DataMember]
        public string Cedula { get; set; }

        [DataMember]
        public decimal MontoElectrodomestico { get; set; }

        [DataMember]
        public int PlazoMeses { get; set; }
    }

    [DataContract]
    public class AmortizacionDTO
    {
        [DataMember]
        public int NumeroCuota { get; set; }

        [DataMember]
        public decimal ValorCuota { get; set; }

        [DataMember]
        public decimal InteresPagado { get; set; }

        [DataMember]
        public decimal CapitalPagado { get; set; }

        [DataMember]
        public decimal Saldo { get; set; }
    }

    [DataContract]
    public class EvaluacionCreditoResponse
    {
        [DataMember]
        public bool? SujetoCredito { get; set; }

        [DataMember]
        public bool? CreditoAprobado { get; set; }

        [DataMember]
        public decimal? MontoMaximoCredito { get; set; }

        [DataMember]
        public string Mensaje { get; set; }

        [DataMember]
        public int? IdCredito { get; set; }

        [DataMember]
        public decimal? CuotaMensual { get; set; }

        [DataMember]
        public List<AmortizacionDTO> TablaAmortizacion { get; set; }
    }

    [DataContract]
    public class CreditoResponse
    {
        [DataMember]
        public int IdCredito { get; set; }

        [DataMember]
        public string Cedula { get; set; }

        [DataMember]
        public decimal MontoSolicitado { get; set; }

        [DataMember]
        public decimal MontoAprobado { get; set; }

        [DataMember]
        public int PlazoMeses { get; set; }

        [DataMember]
        public decimal TasaAnual { get; set; }

        [DataMember]
        public decimal CuotaFija { get; set; }

        [DataMember]
        public string Estado { get; set; }

        [DataMember]
        public string FechaSolicitud { get; set; }

        [DataMember]
        public string FechaAprobacion { get; set; }
    }

    [DataContract]
    public class CuotaAmortizacionResponse
    {
        [DataMember]
        public int NumeroCuota { get; set; }

        [DataMember]
        public decimal Capital { get; set; }

        [DataMember]
        public decimal Interes { get; set; }

        [DataMember]
        public decimal Cuota { get; set; }

        [DataMember]
        public decimal SaldoFinal { get; set; }
    }

    [DataContract]
    public class TablaAmortizacionResponse
    {
        [DataMember]
        public int IdFactura { get; set; }

        [DataMember]
        public string NombreCliente { get; set; }

        [DataMember]
        public string CedulaCliente { get; set; }

        [DataMember]
        public decimal MontoTotal { get; set; }

        [DataMember]
        public decimal TasaInteres { get; set; }

        [DataMember]
        public int NumeroCuotas { get; set; }

        [DataMember]
        public decimal CuotaMensual { get; set; }

        [DataMember]
        public List<CuotaAmortizacionResponse> Cuotas { get; set; }
    }
}
