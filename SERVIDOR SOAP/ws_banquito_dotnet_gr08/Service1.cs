using System;
using System.Collections.Generic;
using System.Text;
using ec.edu.monster.service;

namespace ws_banquito_dotnet_gr08
{
    public class Service1 : IService1
    {
        private readonly CreditoService _creditoService = new CreditoService();

        public EvaluacionCreditoResponse EvaluarCredito(EvaluacionCreditoRequest request)
        {
            var response = new EvaluacionCreditoResponse();

            if (request == null || string.IsNullOrWhiteSpace(request.Cedula))
            {
                response.SujetoCredito = false;
                response.CreditoAprobado = false;
                response.Mensaje = "Datos de solicitud inválidos";
                return response;
            }

            if (request.PlazoMeses < 3 || request.PlazoMeses > 24)
            {
                response.SujetoCredito = false;
                response.CreditoAprobado = false;
                response.Mensaje = "El plazo debe estar entre 3 y 24 meses";
                return response;
            }

            if (request.MontoElectrodomestico <= 0)
            {
                response.SujetoCredito = false;
                response.CreditoAprobado = false;
                response.Mensaje = "El monto del electrodoméstico debe ser mayor a 0";
                return response;
            }

            return _creditoService.EvaluarCredito(request);
        }

        public EvaluacionCreditoResponse VerificarSujetoCredito(string cedula)
        {
            if (string.IsNullOrWhiteSpace(cedula))
            {
                return new EvaluacionCreditoResponse
                {
                    SujetoCredito = false,
                    CreditoAprobado = null,
                    Mensaje = "Cédula inválida"
                };
            }

            return _creditoService.VerificarSujetoCredito(cedula);
        }

        public List<CreditoResponse> ObtenerCreditosActivosPorCliente(string cedula)
        {
            if (string.IsNullOrWhiteSpace(cedula))
            {
                return new List<CreditoResponse>();
            }

            return _creditoService.ObtenerCreditosActivosPorCliente(cedula);
        }

        public decimal? ObtenerMontoMaximo(string cedula)
        {
            if (string.IsNullOrWhiteSpace(cedula))
            {
                return null;
            }

            var evaluacion = _creditoService.VerificarSujetoCredito(cedula);
            if (evaluacion.SujetoCredito == true)
            {
                return evaluacion.MontoMaximoCredito;
            }

            return null;
        }

        public List<AmortizacionDTO> ObtenerTablaAmortizacion(int idCredito)
        {
            if (idCredito <= 0)
            {
                return new List<AmortizacionDTO>();
            }

            return _creditoService.ObtenerTablaAmortizacion(idCredito);
        }

        public TablaAmortizacionResponse ObtenerTablaAmortizacionComercializadora(int idCredito)
        {
            if (idCredito <= 0)
            {
                return null;
            }

            return _creditoService.ObtenerTablaAmortizacionParaComercializadora(idCredito);
        }
    }
}
