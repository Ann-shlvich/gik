package by.bank.server.repos;

import by.bank.common.entity.Examination;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExaminationRepository extends BasicRepository<Examination> {

    private static final String QUERY = "SELECT ExaminationId, Title, Description, StartDate, EndDate FROM Examination ORDER BY StartDate, Title";
    private static final String CREATE = "INSERT INTO Examination (Title, Description, StartDate, EndDate) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE Examination SET Title = ?, Description = ?, StartDate = ?, EndDate = ? WHERE ExaminationId = ?";
    private static final String DELETE = "DELETE FROM Examination WHERE ExaminationId = ?";
    private static final String USED = "SELECT COUNT(*) FROM Opinion WHERE ExaminationId = ?";

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
    protected Examination parse(ResultSet rs) throws SQLException {
        Examination value = new Examination();
        value.setId(rs.getInt(1));
        value.setTitle(rs.getString(2));
        value.setDescription(rs.getString(3));
        value.setStartDate(rs.getDate(4).toLocalDate());
        value.setEndDate(rs.getDate(5).toLocalDate());
        return value;
    }

    @Override
    protected void prepareCreate(PreparedStatement st, Examination value) throws SQLException {
        st.setString(1, value.getTitle());
        st.setString(2, value.getDescription());
        st.setDate(3, Date.valueOf(value.getStartDate()));
        st.setDate(4, Date.valueOf(value.getEndDate()));
    }

    @Override
    protected void prepareUpdate(PreparedStatement st, Examination value) throws SQLException {
        prepareCreate(st, value);
        st.setInt(5, value.getId());
    }
}
