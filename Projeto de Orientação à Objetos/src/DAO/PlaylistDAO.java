package dao;

import model.Playlist;
import model.Musica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {
    public int criarPlaylist(int usuarioId, String nome) throws SQLException {
        String sql = "INSERT INTO usuario_playlists (usuario_id, nome) VALUES (?, ?) RETURNING id";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            stmt.setString(2, nome);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            return -1;
        }
    }

    public boolean adicionarMusica(int playlistId, int musicaId) throws SQLException {
        String sql = "INSERT INTO playlist_musicas (playlist_id, musica_id) VALUES (?, ?)";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, playlistId);
            stmt.setInt(2, musicaId);
            
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean removerMusica(int playlistId, int musicaId) throws SQLException {
        String sql = "DELETE FROM playlist_musicas WHERE playlist_id = ? AND musica_id = ?";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, playlistId);
            stmt.setInt(2, musicaId);
            
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Playlist> listarPlaylistsUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT id, nome, data_criacao FROM usuario_playlists WHERE usuario_id = ?";
        List<Playlist> playlists = new ArrayList<>();
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Playlist playlist = new Playlist();
                playlist.setId(rs.getInt("id"));
                playlist.setNome(rs.getString("nome"));
                playlist.setUsuarioId(usuarioId);
                playlist.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
                playlists.add(playlist);
            }
        }
        return playlists;
    }

    public Playlist obterPlaylistComMusicas(int playlistId) throws SQLException {
        String sqlPlaylist = "SELECT id, nome, usuario_id, data_criacao FROM usuario_playlists WHERE id = ?";
        String sqlMusicas = "SELECT m.id, m.titulo, m.artista_id, a.nome as artista_nome, m.genero, m.duracao_segundos " +
                           "FROM playlist_musicas pm " +
                           "JOIN musicas m ON pm.musica_id = m.id " +
                           "JOIN artistas a ON m.artista_id = a.id " +
                           "WHERE pm.playlist_id = ? " +
                           "ORDER BY pm.ordem, pm.data_adicao";
        
        Playlist playlist = null;
        
        try (Connection conn = Conexao.getConnection()) {
            // Obter informações da playlist
            try (PreparedStatement stmt = conn.prepareStatement(sqlPlaylist)) {
                stmt.setInt(1, playlistId);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    playlist = new Playlist();
                    playlist.setId(rs.getInt("id"));
                    playlist.setNome(rs.getString("nome"));
                    playlist.setUsuarioId(rs.getInt("usuario_id"));
                    playlist.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
                }
            }
            
            if (playlist != null) {
                // Obter músicas da playlist
                List<Musica> musicas = new ArrayList<>();
                try (PreparedStatement stmt = conn.prepareStatement(sqlMusicas)) {
                    stmt.setInt(1, playlistId);
                    ResultSet rs = stmt.executeQuery();
                    
                    while (rs.next()) {
                        Musica musica = new Musica();
                        musica.setId(rs.getInt("id"));
                        musica.setTitulo(rs.getString("titulo"));
                        musica.setArtistaId(rs.getInt("artista_id"));
                        musica.setArtistaNome(rs.getString("artista_nome"));
                        musica.setGenero(rs.getString("genero"));
                        musica.setDuracaoSegundos(rs.getInt("duracao_segundos"));
                        musicas.add(musica);
                    }
                }
                playlist.setMusicas(musicas);
            }
        }
        return playlist;
    }

    public boolean renomearPlaylist(int playlistId, String novoNome) throws SQLException {
        String sql = "UPDATE usuario_playlists SET nome = ? WHERE id = ?";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, novoNome);
            stmt.setInt(2, playlistId);
            
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean excluirPlaylist(int playlistId) throws SQLException {
        String sqlDeleteMusicas = "DELETE FROM playlist_musicas WHERE playlist_id = ?";
        String sqlDeletePlaylist = "DELETE FROM usuario_playlists WHERE id = ?";
        
        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Primeiro remove as músicas da playlist
                try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteMusicas)) {
                    stmt.setInt(1, playlistId);
                    stmt.executeUpdate();
                }
                
                // Depois remove a playlist
                try (PreparedStatement stmt = conn.prepareStatement(sqlDeletePlaylist)) {
                    stmt.setInt(1, playlistId);
                    int affectedRows = stmt.executeUpdate();
                    conn.commit();
                    return affectedRows > 0;
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public boolean musicaNaPlaylist(int playlistId, int musicaId) throws SQLException {
        String sql = "SELECT 1 FROM playlist_musicas WHERE playlist_id = ? AND musica_id = ?";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, playlistId);
            stmt.setInt(2, musicaId);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}