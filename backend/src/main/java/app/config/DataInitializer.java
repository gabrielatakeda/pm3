package app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import app.auth.Usuarios;
import app.auth.UsuariosRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuariosRepository usuariosRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UsuariosRepository usuariosRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuariosRepository.findByLogin("admin").isPresent()) {
            log.info("ADMIN ALREADY CREATED, SKIP");
            return;
        }

        Usuarios admin = new Usuarios();
        admin.setNome("Administrador");
        admin.setRole("Admin");
        admin.setCpf("529.982.247-25");
        admin.setLogin("admin");
        admin.setSenha(passwordEncoder.encode("admin123"));
        admin.setAtivo(true);

        usuariosRepository.save(admin);
        log.info("DEFAULT ADMIN USER CREATED");
    }
}
