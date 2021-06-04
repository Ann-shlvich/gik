package by.bank.server.repos;

import by.bank.common.entity.Opinion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OpinionRepository extends BasicRepository<Opinion> {

    private static final String QUERY = "SELECT OpinionId, ExaminationId, UserId, Datum FROM Opinion";
    private static final String CREATE = "INSERT INTO Opinion (ExaminationId, UserId, Datum) VALUES (?, ?, ?)";
    private static final String UPDATE = "UPDATE Opinion SET ExaminationId = ?, UserId = ?, Datum = ? WHERE OpinionId = ?";
    private static final String DELETE = "DELETE FROM Opinion WHERE OpinionId = ?";
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
    protected Opinion parse(ResultSet rs) throws SQLException {
        Opinion value = new Opinion();
        value.setId(rs.getInt(1));
        value.setExaminationId(rs.getInt(2));
        value.setUserId(rs.getInt(3));
        value.setData(rs.getBytes(4));
        return value;
    }

    @Override
    protected void prepareCreate(PreparedStatement st, Opinion value) throws SQLException {
        st.setInt(1, value.getExaminationId());
        st.setInt(2, value.getUserId());
        st.setBytes(3, value.getData());
    }

    @Override
    protected void prepareUpdate(PreparedStatement st, Opinion value) throws SQLException {
        prepareCreate(st, value);
        st.setInt(4, value.getId());
    }
}
