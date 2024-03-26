package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    // Busca um departamento pelo ID
    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM department WHERE Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                return obj;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar departamento por ID: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    // Retorna todos os departamentos
    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM department ORDER BY Name");
            rs = st.executeQuery();
            List<Department> list = new ArrayList<>();
            while (rs.next()) {
                Department obj = new Department();
                obj.setId(rs.getInt("Id"));
                obj.setName(rs.getString("Name"));
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException("Erro ao buscar todos os departamentos: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    // Insere um novo departamento
    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getName());
            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
            } else {
                throw new DbException("Erro inesperado! Nenhuma linha afetada!");
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao inserir departamento: " + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    // Atualiza um departamento existente
    @Override
    public void update(Department obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");
            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Erro ao atualizar departamento: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

    // Deleta um departamento pelo ID
    /*   @Override
   public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            // Prepara a instrução SQL para excluir o departamento com base no ID fornecido
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
            
            // Define o ID do departamento a ser excluído como o primeiro parâmetro na consulta preparada
            st.setInt(1, id);
            
            // Executa a instrução SQL para excluir o departamento
            st.executeUpdate();
        } catch (SQLException e) {
            // Em caso de exceção SQL, lança uma exceção DbIntegrityException com uma mensagem descritiva
            throw new DbIntegrityException("Erro ao excluir departamento. Este departamento possui vendedores associados.");
        } finally {
            // Fecha o PreparedStatement para liberar recursos
            DB.closeStatement(st);
        }*/
    }
