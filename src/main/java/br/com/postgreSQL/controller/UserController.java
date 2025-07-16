package br.com.postgreSQL.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postgreSQL.dto.user.UserDto;
import br.com.postgreSQL.dto.user.UserFindByLoginDto;
import br.com.postgreSQL.dto.user.UserTokenDto;
import br.com.postgreSQL.dto.user.response.ResponseCreateUserDto;
import br.com.postgreSQL.dto.user.response.ResponseTokenDto;
import br.com.postgreSQL.model.User;
import br.com.postgreSQL.service.user.TokenService;
import br.com.postgreSQL.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@Tag(name = "Usuarios", description = "Endpoints para criação, geração de token e busca de usuarios")
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class UserController {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	private final UserService userService;
	
    @Operation(
            summary = "Cria uma conta usuario.",
            description = "Cria uma conta para que seja possivel realizar a solicitação do Token."
    )
	@PostMapping("/create")
	public ResponseEntity<ResponseCreateUserDto> createUser(@RequestBody @Valid UserDto userDto) {
		userService.createUser(userDto);
		ResponseCreateUserDto response = new ResponseCreateUserDto(ResponseCreateUserDto.CREATE_WITH_SUCESS);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
    @Operation(
            summary = "Gera um token.",
            description = "Gera um token para que seja possivel realizar as requisições."
    )    
	@PostMapping("/token")
	public ResponseEntity token(@RequestBody @Valid UserTokenDto userTokenDto) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(userTokenDto.login(), userTokenDto.password());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		var token = tokenService.generateToken((User)auth.getPrincipal());
		return ResponseEntity.ok(new ResponseTokenDto(token));
	}
	
    @Operation(
            summary = "Retorna usuario.",
            description = "Retorna usuario pelo login."
    )
	@GetMapping("/user/{login}")
	public ResponseEntity findUserByLogin(@PathVariable String login) {
		UserFindByLoginDto user = userService.findUserByLogin(login);
		return ResponseEntity.ok().body(user);
	}
}
