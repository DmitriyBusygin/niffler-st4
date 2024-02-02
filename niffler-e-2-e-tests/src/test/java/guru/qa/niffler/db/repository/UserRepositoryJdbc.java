package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.JdbcUrl;
import guru.qa.niffler.db.model.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryJdbc implements UserRepository {

  private final DataSource authDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.AUTH);
  private final DataSource udDs = DataSourceProvider.INSTANCE.dataSource(JdbcUrl.USERDATA);

  private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Override
  public UserAuthEntity createInAuth(UserAuthEntity user) {
    try (Connection conn = authDs.getConnection()) {
      conn.setAutoCommit(false);

      try (PreparedStatement userPs = conn.prepareStatement(
          "INSERT INTO \"user\" " +
              "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
              "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
           PreparedStatement authorityPs = conn.prepareStatement(
               "INSERT INTO \"authority\" " +
                   "(user_id, authority) " +
                   "VALUES (?, ?)")
      ) {

        userPs.setString(1, user.getUsername());
        userPs.setString(2, pe.encode(user.getPassword()));
        userPs.setBoolean(3, user.getEnabled());
        userPs.setBoolean(4, user.getAccountNonExpired());
        userPs.setBoolean(5, user.getAccountNonLocked());
        userPs.setBoolean(6, user.getCredentialsNonExpired());

        userPs.executeUpdate();

        UUID authUserId;
        try (ResultSet keys = userPs.getGeneratedKeys()) {
          if (keys.next()) {
            authUserId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }

        for (Authority authority : Authority.values()) {
          authorityPs.setObject(1, authUserId);
          authorityPs.setString(2, authority.name());
          authorityPs.addBatch();
          authorityPs.clearParameters();
        }

        authorityPs.executeBatch();
        conn.commit();
        user.setId(authUserId);
      } catch (Exception e) {
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public UserEntity createInUserdata(UserEntity user) {
    try (Connection conn = udDs.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO \"user\" " +
              "(username, currency) " +
              "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getCurrency().name());
        ps.executeUpdate();

        UUID userId;
        try (ResultSet keys = ps.getGeneratedKeys()) {
          if (keys.next()) {
            userId = UUID.fromString(keys.getString("id"));
          } else {
            throw new IllegalStateException("Can`t find id");
          }
        }
        user.setId(userId);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return user;
  }

  @Override
  public void deleteInAuthById(UUID id) {
    try (Connection connection = authDs.getConnection()) {
      connection.setAutoCommit(false);
      try(PreparedStatement psAuthority = connection.prepareStatement(
              "DELETE FROM \"authority\" WHERE user_id = ?");
          PreparedStatement psUser = connection.prepareStatement(
                  "DELETE FROM \"user\" WHERE id = ?")) {
        psAuthority.setObject(1, id);
        psAuthority.executeUpdate();
        psUser.setObject(1, id);
        psUser.executeUpdate();
        connection.commit();
      } catch (SQLException e) {
        connection.rollback();
        throw e;
      } finally {
        connection.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteInUserdataById(UUID id) {
    try (Connection connection = udDs.getConnection()) {
      try(PreparedStatement psUser = connection.prepareStatement(
              "DELETE FROM \"user\" WHERE id = ?")) {
        psUser.setObject(1, id);
        psUser.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void editInAuth(UserAuthEntity user) {
    try (Connection connection = authDs.getConnection()) {
      try(PreparedStatement psUser = connection.prepareStatement(
              "UPDATE \"user\" SET " +
              "username = ?, " +
              "password = ?, " +
              "enabled = ?, " +
              "account_non_expired = ?, " +
              "account_non_locked = ?, " +
              "credentials_non_expired = ? " +
              "WHERE id = ?")) {
        psUser.setString(1, user.getUsername() != null ? user.getUsername() : "");
        psUser.setString(2, user.getPassword() != null ? user.getPassword() : "");
        psUser.setBoolean(3, user.getEnabled() != null ? user.getEnabled() : true);
        psUser.setBoolean(4, user.getAccountNonExpired() != null ? user.getAccountNonExpired() : true);
        psUser.setBoolean(5, user.getAccountNonLocked() != null ? user.getAccountNonLocked() : true);
        psUser.setBoolean(6, user.getCredentialsNonExpired() != null ? user.getCredentialsNonExpired() : true);
        psUser.setObject(7, user.getId());

        psUser.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void editInUserData(UserEntity user) {
    try (Connection connection = udDs.getConnection()) {
      try(PreparedStatement psUser = connection.prepareStatement(
              "UPDATE \"user\" SET " +
              "username = ? ," +
              "currency = ? ," +
              "firstname = ? ," +
              "surname = ? ," +
              "photo = ? " +
              "WHERE id = ?")) {
        psUser.setString(1, user.getUsername() != null ? user.getUsername() : "");
        psUser.setString(2, user.getCurrency() != null ? user.getCurrency().name() : CurrencyValues.RUB.name());
        psUser.setString(3, user.getFirstname());
        psUser.setString(4, user.getSurname());
        psUser.setBytes(5, user.getPhoto());
        psUser.setObject(6, user.getId());

        psUser.executeUpdate();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserAuthEntity> findByIdInAuth(UUID id) {
    try (Connection connection = authDs.getConnection()) {
      try(PreparedStatement psUser = connection.prepareStatement(
              "SELECT * FROM \"user\" WHERE id = ?");
      PreparedStatement authUser = connection.prepareStatement(
              "SELECT * FROM authority WHERE user_id = ?"
      )) {
        psUser.setObject(1, id);
        ResultSet rs = psUser.executeQuery();
        UserAuthEntity userAuthEntity = null;
        if (rs.next()) {
          userAuthEntity = new UserAuthEntity();
          userAuthEntity.setId(rs.getObject("id", UUID.class));
          userAuthEntity.setUsername(rs.getString("username"));
          userAuthEntity.setPassword(rs.getString("password"));
          userAuthEntity.setEnabled(rs.getBoolean("enabled"));
          userAuthEntity.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          userAuthEntity.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          userAuthEntity.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
        }

        authUser.setObject(1, id);
        ResultSet authRs = authUser.executeQuery();
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        while (authRs.next()) {
          AuthorityEntity authority = new AuthorityEntity();
          authority.setId(UUID.fromString(authRs.getString("id")));
          authority.setAuthority(Authority.valueOf(authRs.getString("authority")));
          authorityEntities.add(authority);
        }

        if (userAuthEntity != null) {
          userAuthEntity.setAuthorities(authorityEntities);
        }

        return Optional.ofNullable(userAuthEntity);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findByIdInUserData(UUID id) {
    try (Connection connection = udDs.getConnection()) {
      try(PreparedStatement psUser = connection.prepareStatement(
              "SELECT * FROM \"user\" WHERE id = ?")) {
        psUser.setObject(1, id);
        ResultSet rs = psUser.executeQuery();

        UserEntity userFind = null;
        if (rs.next()) {
          userFind = new UserEntity();
          userFind.setId(rs.getObject("id", UUID.class));
          userFind.setUsername(rs.getString("username"));
          userFind.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          userFind.setFirstname(rs.getString("firstname"));
          userFind.setSurname(rs.getString("surname"));
          userFind.setPhoto(rs.getBytes("photo"));
        }
        return Optional.ofNullable(userFind);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
