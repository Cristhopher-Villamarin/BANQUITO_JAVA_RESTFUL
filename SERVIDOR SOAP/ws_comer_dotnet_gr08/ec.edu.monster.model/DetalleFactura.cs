using System;

namespace ec.edu.monster.model
{
    public class DetalleFactura
    {
        public int IdDetalle { get; set; }
        public int IdFactura { get; set; }
        public int IdElectrodomestico { get; set; }
        public int Cantidad { get; set; }
        public decimal PrecioUnitario { get; set; }
        public decimal SubtotalLinea { get; set; }
    }
}
