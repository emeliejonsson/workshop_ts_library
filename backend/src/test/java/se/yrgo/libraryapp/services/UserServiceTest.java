package se.yrgo.libraryapp.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.pac4j.core.credentials.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.UserId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
    private UserService userService;

    @Mock
    private UserDao userDao;
    @Mock
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        userService = new UserService(userDao, encoder);
    }

    //validate
    @Test
    void validateCorrectLogin() {
        final String userId = "1";
        final UserId id = UserId.of(userId);
        final String username = "testuser";
        final String password = "password";
        final String passwordHash =
                "hashed_password";

        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(new LoginInfo(id, passwordHash)));
        when(encoder.matches(password, passwordHash)).thenReturn(true);

        assertThat(userService.validate(username, password)).isEqualTo(Optional.of(id));
    }

    @Test
    void validateWrongPassword() {
        final UserId id = UserId.of("1");
        final String username = "bosse";
        final String password = "wrong_password";
        final String passwordHash = "hashed_password";

        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(new LoginInfo(id, passwordHash)));
        when(encoder.matches(password, passwordHash)).thenReturn(false);

        assertThat(userService.validate(username, password)).isEmpty();
        //om matches anropas med fel argument -> empty() eftersom mock returnerar false som standard
        //verify fångar detta!
        verify(encoder).matches(password, passwordHash);
    }

    @Test
    void validateUnknownUser() {
        final String username = "unknown_bosse";
        final String password = "password";

        when(userDao.getLoginInfo(username)).thenReturn(Optional.empty());

        assertThat(userService.validate(username, password)).isEmpty();
        verify(encoder, never()).matches(any(), any());
    }

    //register
    @Test
    void registerValidUser() {
        final String username = "Bo_Bo";
        final String realname = "Bo Jonsson";
        final String password = "password";
        final String passwordHashed = "hashed";

        when(encoder.encode(password)).thenReturn(passwordHashed);
        when(userDao.register(username, realname, passwordHashed)).thenReturn(true);

        assertThat(userService.register(username, realname, password)).isTrue();
    }

    @Test
    void registerRealNameWithApostrophe() {
        final String username = "Ian1";
        final String realname = "Ian O'Toole";
        final String password = "password";
        final String passwordHashed = "hashed_password";

        when(encoder.encode(password)).thenReturn(passwordHashed);
        when(userDao.register(username, realname, passwordHashed)).thenReturn(true);

        assertThat(userService.register(username, realname, password)).isTrue();
    }

    // isNameAvailable
    @Test
    void isNameAvailableTooShortUsername() {
        assertThat(userService.isNameAvailable("bo")).isFalse();
        verifyNoInteractions(userDao);
    }

    @Test
    void isNameAvailableTooShortUsernameWithSpace() {
        assertThat(userService.isNameAvailable("   bo   ")).isFalse();
        verifyNoInteractions(userDao);
    }

    @Test
    void isNameAvailableFreeUsername() {
        when(userDao.isNameAvailable("Bo_Bo")).thenReturn(true);
        assertThat(userService.isNameAvailable("Bo_Bo")).isTrue();
    }

    @Test
    void isNameAvailableTakenUsername() {
        when(userDao.isNameAvailable("Bo_Bo")).thenReturn(false);
        assertThat(userService.isNameAvailable("Bo_Bo")).isFalse();
    }
}