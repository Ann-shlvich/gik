package by.bank.server.repos;

import by.bank.common.entity.Entity;
import by.bank.server.helper.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BasicRepository<T extends Entity> {

    protected ConnectionManager pool = ConnectionManager.getInstance();
    protected Logger logger = Logger.getInstance();

    public List<T> query(SearchCriteria<T> criteria) {
        List<T> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            con = pool.getConnection();
            st = con.prepareStatement(query());
            rs = st.executeQuery();
            while(rs.next()) {
                list.add(parse(rs));
            }
        } catch (SQLException ignored) {
            logger.log("Ошибка чтения записей из БД");
        } finally {
            closeResources(rs, st, con);
            pool.releaseConnection(con);
        }
        if (criteria != null) {
            list = list.stream().filter(criteria::accepted).collect(Collectors.toList());
        }
        return list;
    }

    public T get(int id) {
        List<T> result = query(m -> m.getId() == id);
        return result.isEmpty() ? null : result.get(0);
    }

    public boolean create(T value) {
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = pool.getConnection();
            st = con.prepareStatement(create());
            prepareCreate(st, value);
            return st.executeUpdate() == 1;
        } catch (SQLException ignored) {
            logger.log("Ошибка создания записи в БД");
        } finally {
            closeResources(null, st, con);
        }
        return false;
    }

    public boolean update(T value) {
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = pool.getConnection();
            st = con.prepareStatement(update());
            prepareUpdate(st, value);
            return st.executeUpdate() == 1;
        } catch (SQLException ignored) {
            logger.log("Ошибка редактирования записи в БД");
        } finally {
            closeResources(null, st, con);
        }
        return false;
    }

    public boolean remove(T value) {
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = pool.getConnection();
            st = con.prepareStatement(remove());
            prepareRemove(st, value);
            return st.executeUpdate() == 1;
        } catch (SQLException ignored) {
            logger.log("Ошибка удаления записи в БД");
        } finally {
            closeResources(null, st, con);
        }
        return false;
    }

    public boolean isUsed(T value) {
        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            con = pool.getConnection();
            st = con.prepareStatement(isUsed());
            prepareIsUsed(st, value);
            rs = st.executeQuery();
            if (rs.next()) {
                result = (rs.getInt(1) != 0);
            }
        } catch (SQLException ignored) {
            logger.log("Ошибка проверки использования записи из БД");
        } finally {
            closeResources(rs, st, con);
            pool.releaseConnection(con);
        }
        return result;
    }

    protected abstract String query();
    protected abstract String create();
    protected abstract String update();
    protected abstract String remove();
    protected abstract String isUsed();

    protected abstract T parse(ResultSet rs) throws SQLException;
    protected abstract void prepareCreate(PreparedStatement st, T value) throws SQLException;
    protected abstract void prepareUpdate(PreparedStatement st, T value) throws SQLException;

    protected void prepareRemove(PreparedStatement st, T value) throws SQLException {
        st.setInt(1, value.getId());
    }

    protected void prepareIsUsed(PreparedStatement st, T value) throws SQLException {
        st.setInt(1, value.getId());
    }

    protected void closeResources(ResultSet rs, PreparedStatement st, Connection con) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ignored) {
            }
        }
        pool.releaseConnection(con);
    }
}
