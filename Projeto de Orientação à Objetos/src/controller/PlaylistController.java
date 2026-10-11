package controller;

import dao.PlaylistDAO;
import model.Playlist;
import java.sql.SQLException;
import java.util.List;

public class PlaylistController {
    private PlaylistDAO playlistDAO;
    
    public PlaylistController() {
        this.playlistDAO = new PlaylistDAO();
    }
    
    public boolean criarPlaylist(int usuarioId, String nome) {
        try {
            return playlistDAO.criarPlaylist(usuarioId, nome);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean adicionarMusicaPlaylist(int playlistId, int musicaId) {
        try {
            return playlistDAO.adicionarMusicaPlaylist(playlistId, musicaId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Playlist> listarPlaylistsUsuario(int usuarioId) {
        try {
            return playlistDAO.listarPlaylistsUsuario(usuarioId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}