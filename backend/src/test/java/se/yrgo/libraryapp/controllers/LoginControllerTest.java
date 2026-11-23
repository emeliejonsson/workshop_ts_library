package se.yrgo.libraryapp.controllers;

import io.jooby.Context;
import io.jooby.Cookie;
import io.jooby.StatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.entities.forms.LoginData;
import se.yrgo.libraryapp.services.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class LoginControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private RoleDao roleDao;
    @Mock
    private SessionDao sessionDao;
    @Mock
    private Context context;

   private LoginController controller;

    @BeforeEach
    void setUp() {
        controller = new LoginController(userService, roleDao, sessionDao);
    }

    @Test
    void loginWithVaildUserInfo() {
        UUID sessionId = UUID.randomUUID();
        UserId userId = UserId.of(1);
        LoginData login = new LoginData("bobbo", "validPassword");

        when(userService.validate(login.getUsername(), login.getPassword())).thenReturn(Optional.of(userId));
        when(sessionDao.create(userId)).thenReturn(sessionId);
        when(roleDao.get(userId)).thenReturn(List.of(Role.USER));

        List<Role> roles = controller.login(context, null, login);
        assertThat(roles).containsExactly(Role.USER);
    }

    @Test
    void loginInWithIncorrectUserInfo() {
        LoginData login = new LoginData("bobbo", "wrongPassword");

        when(userService.validate(login.getUsername(), login.getPassword())).thenReturn(Optional.empty());
        List<Role> roles = controller.login(context, null, login);

        assertThat(roles).isEmpty();
        verify(context).setResponseCode(StatusCode.UNAUTHORIZED);
        verify(context, never()).setResponseCookie(any(Cookie.class));
        verifyNoInteractions(sessionDao, roleDao);
    }

}
