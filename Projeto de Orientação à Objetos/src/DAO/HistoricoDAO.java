package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoricoDAO {
    public void registrarBusca(int usuarioId, String termo, String tipo) throws SQLException {
        String sql = "INSERT INTO historico_buscas (usuario_id, termo_busca, tipo_busca) VALUES (?, ?, ?)";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            stmt.setString(2, termo);
            stmt.setString(3, tipo);
            
            stmt.executeUpdate();
        }
    }

    public List<String> listarUltimasBuscas(int usuarioId, int limite) throws SQLException {
        String sql = "SELECT termo_busca FROM historico_buscas WHERE usuario_id = ? ORDER BY data_busca DESC LIMIT ?";
        List<String> buscas = new ArrayList<>();
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, limite);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                buscas.add(rs.getString("termo_busca"));
            }
        }
        return buscas;
    }
}