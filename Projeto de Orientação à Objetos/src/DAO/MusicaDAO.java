package DAO;

import model.Musica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicaDAO {
    public List<Musica> buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM musicas WHERE titulo LIKE ?";
        List<Musica> musicas = new ArrayList<>();
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Musica m = new Musica();
                m.setId(rs.getInt("id"));
                m.setTitulo(rs.getString("titulo"));
                m.setArtista(rs.getString("artista"));
                m.setGenero(rs.getString("genero"));
                m.setDuracaoSegundos(rs.getInt("duracao"));
                musicas.add(m);
            }
        }
        return musicas;
    }
    
    // Outros métodos: curtirMusica, buscarPorArtista, etc.
}