package by.bank.server.repos;

import by.bank.common.entity.Alternative;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlternativeRepository extends BasicRepository<Alternative> {

    private static final String QUERY = "SELECT AlternativeId, Ordinal, Description, ExaminationId FROM Alternative ORDER BY ExaminationId, Ordinal";
    private static final String CREATE = "INSERT INTO Alternative (Ordinal, Description, ExaminationId) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE Alternative SET Ordinal = ?, Description = ?, ExaminationId = ? WHERE AlternativeId = ?";
    private static final String DELETE = "DELETE FROM Alternative WHERE AlternativeId = ?";
    private static final String USED = "SELECT 0";

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
    protected Alternative parse(ResultSet rs) throws SQLException {
        Alternative value = new Alternative();
        value.setId(rs.getInt(1));
        value.setOrdinal(rs.getInt(2));
        value.setDescription(rs.getString(3));
        value.setExaminationId(rs.getInt(4));
        return value;
    }

    @Override
    protected void prepareCreate(PreparedStatement st, Alternative value) throws SQLException {
        st.setInt(1, value.getOrdinal());
        st.setString(2, value.getDescription());
        st.setInt(3, value.getExaminationId());
    }

    @Override
    protected void prepareUpdate(PreparedStatement st, Alternative value) throws SQLException {
        prepareCreate(st, value);
        st.setInt(4, value.getId());
    }
}
