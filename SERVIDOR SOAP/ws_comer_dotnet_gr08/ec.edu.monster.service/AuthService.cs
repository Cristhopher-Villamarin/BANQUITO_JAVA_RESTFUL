using System.Collections.Generic;
using System.Linq;
using ec.edu.monster.model;
using ws_comer_dotnet_gr08;

namespace ec.edu.monster.service
{
    public class AuthService
    {
        private readonly Dictionary<string, Usuario> _usuarios;

        public AuthService()
        {
            _usuarios = new Dictionary<string, Usuario>();
            InicializarUsuarios();
        }

        private void InicializarUsuarios()
        {
            var monster = new Usuario("MONSTER", "MONSTER9", "USER");
            _usuarios["MONSTER"] = monster;

            var admin = new Usuario("admin", "admin", "ADMIN");
            _usuarios["admin"] = admin;
        }

        public LoginResponse Autenticar(LoginRequest request)
        {
            if (request == null || string.IsNullOrWhiteSpace(request.Username) || string.IsNullOrWhiteSpace(request.Password))
            {
                return LoginResponse.Fallido("Credenciales inválidas");
            }

            if (!_usuarios.TryGetValue(request.Username, out var usuario))
            {
                return LoginResponse.Fallido("Usuario no encontrado");
            }

            if (!usuario.Activo)
            {
                return LoginResponse.Fallido("Usuario inactivo");
            }

            if (usuario.Password != request.Password)
            {
                return LoginResponse.Fallido("Contraseña incorrecta");
            }

            return LoginResponse.Exitoso(usuario.Username, usuario.Rol);
        }

        public bool TieneRol(string username, string rolRequerido)
        {
            return _usuarios.TryGetValue(username, out var usuario) && usuario.TieneRol(rolRequerido);
        }

        public bool EsAdmin(string username)
        {
            return TieneRol(username, "ADMIN");
        }

        public bool EsUsuarioValido(string username)
        {
            return _usuarios.TryGetValue(username, out var usuario) && usuario.Activo;
        }

        public Dictionary<string, string> ListarUsuariosDescripcion()
        {
            return _usuarios.Values.ToDictionary(
                u => u.Username,
                u => u.Username == "MONSTER" ? "USER (Solo ventas, sin CRUD)" : "ADMIN (Acceso completo including CRUD)");
        }
    }
}
