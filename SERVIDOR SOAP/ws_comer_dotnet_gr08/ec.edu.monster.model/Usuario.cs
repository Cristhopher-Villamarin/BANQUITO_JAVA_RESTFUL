namespace ec.edu.monster.model
{
    public class Usuario
    {
        public string Username { get; set; }
        public string Password { get; set; }
        public string Rol { get; set; }
        public bool Activo { get; set; }

        public Usuario()
        {
        }

        public Usuario(string username, string password, string rol)
        {
            Username = username;
            Password = password;
            Rol = rol;
            Activo = true;
        }

        public bool TieneRol(string rol)
        {
            return Rol != null && Rol == rol;
        }
    }
}
