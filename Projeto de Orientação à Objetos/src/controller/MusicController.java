package Controller;

import dao.MusicaDAO;
import model.Musica;
import java.sql.SQLException;
import java.util.List;

public class MusicController {
    public List<Musica> buscarMusicas(String termo, String tipoBusca) {
        try {
            MusicaDAO dao = new MusicaDAO();
            switch (tipoBusca) {
                case "nome":
                    return dao.buscarPorNome(termo);
                case "artista":
                    return dao.buscarPorArtista(termo);
                case "genero":
                    return dao.buscarPorGenero(termo);
                default:
                    return dao.listarTodas();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void curtirMusica(int musicaId, int usuarioId) {
        // Implementar lógica de curtir
    }
}