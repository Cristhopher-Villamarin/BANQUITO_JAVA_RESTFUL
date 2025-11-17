using System;

namespace ec.edu.monster.model
{
    public class Movimiento
    {
        public int CodigoMovimiento { get; set; }
        public string NumCuenta { get; set; }
        public string Tipo { get; set; }
        public decimal Valor { get; set; }
        public DateTime Fecha { get; set; }

        public bool EsDeposito()
        {
            return Tipo == "DEP";
        }

        public bool EsRetiro()
        {
            return Tipo == "RET";
        }
    }
}
