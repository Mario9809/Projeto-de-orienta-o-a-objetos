package controller;

import dao.UsuarioDAO;
import model.Usuario;
import java.sql.SQLException;

public class AuthController {
    private final UsuarioDAO usuarioDAO;
    
    public AuthController() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    public boolean cadastrarUsuario(Usuario usuario) {
        try {
            if (usuarioDAO.emailExiste(usuario.getEmail())) {
                return false;
            }
            return usuarioDAO.cadastrar(usuario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Usuario login(String email, String senha) {
        try {
            return usuarioDAO.login(email, senha);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean emailExiste(String email) {
        try {
            return usuarioDAO.emailExiste(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}