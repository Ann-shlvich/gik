package by.bank.server.repos;

import by.bank.common.entity.Role;
import by.bank.common.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends BasicRepository<User> {

    private static final String QUERY = "SELECT UserId, FullName, Phone, Login, Password, Role FROM User ORDER BY FullName";
    private static final String CREATE = "INSERT INTO User (FullName, Phone, Login, Password, Role) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE User SET FullName = ?, Phone = ?, Login = ?, Password = ?, Role = ? WHERE UserId = ?";
    private static final String DELETE = "DELETE FROM User WHERE UserId = ?";
    private static final String USED = "SELECT COUNT(*) FROM Opinion WHERE UserId = ?";

    @Override
    protected String query() {
        return QUERY;
    }

    @Override
    protected String create() {
        return CREATE;
    }

    @Override
    protected String update() {
        return UPDATE;
    }

    @Override
    protected String remove() {
        return DELETE;
    }

    @Override
    protected String isUsed() {
        return USED;
    }

    @Override
    protected User parse(ResultSet rs) throws SQLException {
        User value = new User();
        value.setId(rs.getInt(1));
        value.setFullName(rs.getString(2));
        value.setPhone(rs.getString(3));
        value.setLogin(rs.getString(4));
        value.setPassword(rs.getString(5));
        value.setRole(Role.valueOf(rs.getString(6)));
        return value;
    }

    @Override
    protected void prepareCreate(PreparedStatement st, User value) throws SQLException {
        st.setString(1, value.getFullName());
        st.setString(2, value.getPhone());
        st.setString(3, value.getLogin());
        st.setString(4, value.getPassword());
        st.setString(5, value.getRole().name());
    }

    @Override
    protected void prepareUpdate(PreparedStatement st, User value) throws SQLException {
        prepareCreate(st, value);
        st.setInt(6, value.getId());
    }
}
