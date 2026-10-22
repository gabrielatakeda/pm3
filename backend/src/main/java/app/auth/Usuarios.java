package app.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import app.entity.Auditable;
import app.entity.Emprestimos;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuarios extends Auditable<String> implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Por favor, informe o nome do usuario")
    private String nome;

    @NotBlank(message="Por favor, informe o papel do usuario")
    private String role;

    @Column(unique=true)
    @NotBlank(message="Por favor, informe o CPF do usuario")
    @CPF(message="CPF inválido")
    private String cpf;

    @Column(unique=true)
    @NotBlank(message="Por favor, informe o LOGIN do usuario")
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    private boolean ativo;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnoreProperties({"usuario", "aluno"})
    private List<Emprestimos> emprestimos;

    // ===== Spring Security =====

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(
            new SimpleGrantedAuthority(
                role.startsWith("ROLE_") ? role : "ROLE_" + role
            )
        );
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

	public Usuarios(@NotBlank(message = "Por favor, informe o nome do usuario") String nome,
			@NotBlank(message = "Por favor, informe o papel do usuario") String role,
			@NotBlank(message = "Por favor, informe o CPF do usuario") @CPF(message = "CPF inválido") String cpf,
			@NotBlank(message = "Por favor, informe o LOGIN do usuario") String login, String senha, boolean ativo) {
		this.nome = nome;
		this.role = role;
		this.cpf = cpf;
		this.login = login;
		this.senha = senha;
		this.ativo = ativo;
	}
}
