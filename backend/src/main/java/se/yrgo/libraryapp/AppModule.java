package se.yrgo.libraryapp;

import com.google.inject.AbstractModule;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.pac4j.core.credentials.password.SpringSecurityPasswordEncoder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PasswordEncoder.class).toInstance(new SpringSecurityPasswordEncoder(Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()));
    }
}
