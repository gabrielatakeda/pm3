package app.auth;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Login {
	
	@JsonAlias("login")
	private String usuario;
	private String senha;

}
