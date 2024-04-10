package core.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplate {
    private static final Logger log = LoggerFactory.getLogger(JdbcTemplate.class);

    private PreparedStatement createPreparedStatement(
        Connection conn, 
        String sql, 
        String... args
    ) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            pstmt.setString(i + 1, args[i]);
        }
        return pstmt;
    }

    public void insert(String sql, String... args) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = createPreparedStatement(con, sql, args)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String sql, String... args) {
        insert(sql, args);
    }

    private <T> T createResult(
        Class<T> clazz,
        ResultSet rs
    ) throws IllegalAccessException, SQLException, InstantiationException {
        T instance = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(instance, rs.getString(field.getName()));
        }
        return instance;
    }

    public <T> List<T> select(Class<T> clazz, String sql, String... args) {
        List<T> result = new ArrayList<>();
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = createPreparedStatement(con, sql, args);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                result.add(createResult(clazz, rs));
            }
            log.info("result: {}", result);
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public <T> Optional<T> selectOne(Class<T> clazz, String sql, String... args) {
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement pstmt = createPreparedStatement(con, sql, args);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(createResult(clazz, rs));
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
