using System;

namespace ec.edu.monster.model
{
    public class Cliente
    {
        public string Cedula { get; set; }
        public string Nombre { get; set; }
        public DateTime FechaNacimiento { get; set; }
        public string EstadoCivil { get; set; }

        public int Edad
        {
            get
            {
                var today = DateTime.Today;
                var age = today.Year - FechaNacimiento.Year;
                if (FechaNacimiento.Date > today.AddYears(-age))
                {
                    age--;
                }
                return age;
            }
        }

        public bool EsCasado()
        {
            return EstadoCivil == "C";
        }
    }
}
