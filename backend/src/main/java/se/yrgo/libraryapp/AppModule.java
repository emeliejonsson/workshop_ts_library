package se.yrgo.libraryapp;

import com.google.inject.AbstractModule;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PasswordEncoder.class).to(Argon2PasswordEncoder.class);
    }
}
