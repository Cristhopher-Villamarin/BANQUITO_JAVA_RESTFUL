using System;

namespace ec.edu.monster.model
{
    public class Credito
    {
        public int IdCredito { get; set; }
        public string Cedula { get; set; }
        public decimal MontoSolicitado { get; set; }
        public decimal MontoAprobado { get; set; }
        public int PlazoMeses { get; set; }
        public decimal TasaAnual { get; set; }
        public decimal CuotaFija { get; set; }
        public string Estado { get; set; }
        public DateTime FechaSolicitud { get; set; }
        public DateTime? FechaAprobacion { get; set; }

        public bool EstaActivo()
        {
            return string.Equals(Estado, "ACTIVO", StringComparison.OrdinalIgnoreCase);
        }

        public void Activar()
        {
            Estado = "ACTIVO";
        }
    }
}
