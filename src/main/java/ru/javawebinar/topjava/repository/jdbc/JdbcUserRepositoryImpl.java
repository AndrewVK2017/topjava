package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final RowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay  WHERE id=:id", parameterSource) == 0) {
            return null;
        }
//        insertBatch(user);
        String[] sql = sqlUpdateUserRoles(user);
        jdbcTemplate.batchUpdate(sql);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users JOIN user_roles ON users.id = user_roles.user_id WHERE id=?",
                userSetExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE email=?",
                userSetExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id ORDER BY name, email",
                userSetExtractor());
    }

    private ResultSetExtractor<List<User>> userSetExtractor() {
        return (ResultSetExtractor<List<User>>) rs -> {
            User user = null;
            Map<Integer, User> mapUser = new LinkedHashMap<>();
            Map<Integer, Set<Role>> mapRole = new HashMap<>();
            while (rs.next()) {
                int idUser = rs.getInt("id");
                user = mapUser.get(idUser);
                if (user == null) {
                    user = ROW_MAPPER.mapRow(rs, rs.getRow());
                    mapUser.put(idUser, user);
                }
                Integer userId = rs.getInt("user_id");
                if (userId != null) {
                    Set<Role> roles = new HashSet<Role>();
                    if (mapRole.get(idUser) != null) roles = mapRole.get(idUser);
                    roles.add(Role.valueOf(rs.getString("role")));
                    mapRole.put(idUser, roles);
                }
            }
            for (Map.Entry<Integer, User> entry : mapUser.entrySet()) {
                Integer key = entry.getKey();
                entry.getValue().setRoles(mapRole.get(key));
            }
            return new ArrayList<User>(mapUser.values());
        };
    }

    private String[] sqlUpdateUserRoles(User user) {
        List<String> sqlString = new ArrayList<>();
        sqlString.add("DELETE FROM user_roles WHERE user_id=" + user.getId());
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            sqlString.add("INSERT INTO user_roles (role, user_id) VALUES" +
                    "  ('" + role + "', " + user.getId() + ")");
        }
        String[] result = new String[sqlString.size()];
        return sqlString.toArray(result);
    }

//    public void insertBatch(User user){
//        jdbcTemplate.update("DELETE FROM users WHERE id=?", user.getId());
//        String sql = "INSERT INTO user_roles (role, user_id) VALUES (?, ?)";
//        List<Object[]> parameters = new ArrayList<Object[]>();
//        Set<Role> roles = user.getRoles();
//        for (Role role : roles) {
//            parameters.add(new Object[] {role, user.getId()});
//        }
//        jdbcTemplate.batchUpdate(sql, parameters);
//    }

}
