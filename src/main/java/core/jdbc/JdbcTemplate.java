package core.jdbc;

import core.jdbc.annotations.GeneratedValue;
import core.jdbc.annotations.Id;
import core.jdbc.converter.IntegerConverter;
import core.jdbc.converter.LocalDateTimeConverter;
import core.jdbc.converter.LongConverter;
import core.jdbc.converter.PropertyConverter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class JdbcTemplate {

    private static final Map<Class<?>, PropertyConverter> converters = new HashMap<>();

    private final DataSource dataSource;

    static {
        converters.put(Integer.class, new IntegerConverter());
        converters.put(Long.class, new LongConverter());
        converters.put(LocalDateTime.class, new LocalDateTimeConverter());
    }

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SuppressWarnings("unchecked")
    private PreparedStatement createPreparedStatement(
        Connection conn, 
        String sql, 
        Object... args
    ) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < args.length; i++) {
            String value = args[i].toString();
            if (converters.containsKey(args[i].getClass())) {
                value = converters.get(args[i].getClass()).toString(args[i]);
            }
            pstmt.setString(i + 1, value);
        }
        return pstmt;
    }

    private void setId(Object model, Object id) {
        Field field = Arrays.stream(model.getClass().getDeclaredFields())
            .filter(f -> f.isAnnotationPresent(Id.class))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No Id field"));
        if (!field.isAnnotationPresent(GeneratedValue.class)) {
            return;
        }
        field.setAccessible(true);
        try {
            //TODO: 현재 @Id 필드가 Long 타입이라고 가정. 자동 타입 형변환 추가 필요
            field.set(model, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object execute(String sql, Object... args) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = createPreparedStatement(con, sql, args)) {
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getObject(1);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Object model, String sql, Object... args) {
        setId(model, execute(sql, args));
    }

    public void update(String sql, Object... args) {
        execute(sql, args);
    }

    private <T> T createResult(
        Class<T> clazz,
        ResultSet rs
    ) throws IllegalAccessException, SQLException, InstantiationException {
        T instance = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = rs.getString(field.getName());
            if (converters.containsKey(field.getType())) {
                value = converters.get(field.getType())
                    .fromString(rs.getString(field.getName()));
            }
            field.set(instance, value);
        }
        return instance;
    }

    public <T> List<T> select(Class<T> clazz, String sql, Object... args) {
        List<T> result = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
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

    public <T> List<T> select(String sql, RowMapper<T> rowMapper, Object... args) {
        List<T> result = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = createPreparedStatement(con, sql, args);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                T r = rowMapper.map(rs);
                setId(r, rs.getObject(1));
                result.add(r);
            }
            log.info("result: {}", result);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public <T> Optional<T> selectOne(Class<T> clazz, String sql, Object... args) {
        try (Connection con = dataSource.getConnection();
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

    public <T> Optional<T> selectOne(String sql, RowMapper<T> rowMapper, Object... args) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = createPreparedStatement(con, sql, args);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                T r = rowMapper.map(rs);
                setId(r, rs.getObject(1));
                return Optional.of(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public void delete(String sql, Object... args) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pstmt = createPreparedStatement(con, sql, args)) {
            int result = pstmt.executeUpdate();
            if (result == 0) {
                throw new IllegalArgumentException("No data to delete");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
