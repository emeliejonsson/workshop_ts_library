package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@H2
public class UserDaoIntegrationTest {
    private static DataSource ds;
    private UserDao userDao;

    @BeforeAll
    static void initDataSource() {
// this way we do not need to create a new datasource every time
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        UserDaoIntegrationTest.ds = ds;
    }

    @BeforeEach
    void setUp() { userDao = new UserDao(ds); }

    //get
    @Test
    void getUserById() {
// this data comes from the test migration files
        final String username = "test";
        final UserId userId = UserId.of(1);
        Optional<User> maybeUser = userDao.get(Integer.toString(userId.getId()));
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getName()).isEqualTo(username);
        assertThat(maybeUser.get().getId()).isEqualTo(userId);
    }

    //register
    @Test
    void registerValidUser() {
        boolean registered = userDao.register("Bo_Bo", "Bo Jonsson", "hashed_password");
        assertThat(registered).isTrue();

        Optional<LoginInfo> loginInfo = userDao.getLoginInfo("Bo_Bo");
        assertThat(loginInfo).isPresent();
        assertThat((loginInfo).get().getPasswordHash()).isEqualTo("hashed_password");

        String id = Integer.toString(loginInfo.get().getUserId().getId());
        Optional<User> newValidUser = userDao.get(id);
        assertThat(newValidUser).isPresent();
        assertThat(newValidUser.get().getRealname()).isEqualTo("Bo Jonsson");
    }


    @Test
    void registerRealNameWithApostropheCorrectly() {
        boolean registerWithApostrophe = userDao.register("Ian1", "Ian O'Toole", "hashed_password");
        assertThat(registerWithApostrophe).isTrue();

        Optional<LoginInfo> loginInfo = userDao.getLoginInfo("Ian1");
        assertThat(loginInfo).isPresent();
        String id = Integer.toString(loginInfo.get().getUserId().getId());
        Optional<User> nameWithApostrophe = userDao.get(id);

        assertThat(nameWithApostrophe).isPresent();
        assertThat(nameWithApostrophe.get().getRealname()).isEqualTo("Ian O'Toole");
    }

    @Test
    void registerUserWithAlreadyTakenName() {
        boolean firstRegisteredUser = userDao.register("first_user", "Bo First", "hashed_password");
        assertThat(firstRegisteredUser).isTrue();

        boolean secondRegisteredUser = userDao.register("first_user", "Bo Second", "hashed_password");
        assertThat(secondRegisteredUser).isFalse();
    }

    //isNameAvailable
    @Test
    void isNameAvailableReturnsTrueWhenNameIsFree() {
        boolean freeName = userDao.isNameAvailable("b0b0");
        assertThat(freeName).isTrue();
    }

    @Test
    void isNameAvailableReturnsFalseWhenNameIsTaken() {
        boolean registered = userDao.register("already_taken", "Morgan Takenman", "hashed_password");
        assertThat(registered).isTrue();

        boolean takenName = userDao.isNameAvailable("already_taken");
        assertThat(takenName).isFalse();
    }
}