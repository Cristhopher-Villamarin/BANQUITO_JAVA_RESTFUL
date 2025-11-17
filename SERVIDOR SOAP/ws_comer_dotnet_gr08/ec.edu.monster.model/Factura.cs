using System;

namespace ec.edu.monster.model
{
    public class Factura
    {
        public int IdFactura { get; set; }
        public string Cedula { get; set; }
        public DateTime Fecha { get; set; }
        public string FormaPago { get; set; }
        public decimal Subtotal { get; set; }
        public decimal Descuento { get; set; }
        public decimal Total { get; set; }
        public string Estado { get; set; }

        public bool EsEfectivo()
        {
            return string.Equals(FormaPago, "EFECTIVO", StringComparison.OrdinalIgnoreCase);
        }

        public bool EsCreditoDirecto()
        {
            return string.Equals(FormaPago, "CREDITO_DIRECTO", StringComparison.OrdinalIgnoreCase);
        }
    }
}
