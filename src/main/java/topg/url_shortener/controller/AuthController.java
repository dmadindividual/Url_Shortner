package topg.url_shortener.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topg.url_shortener.dto.JwtResponseDto;
import topg.url_shortener.dto.LoginRequestDto;
import topg.url_shortener.dto.UserRequestDto;
import topg.url_shortener.dto.UserResponseDto;
import topg.url_shortener.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;


    @PostMapping("/public/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> createUser( @RequestBody UserRequestDto userRequestDto){
        UserResponseDto message = userService.createUser(userRequestDto);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/public/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JwtResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto){
        JwtResponseDto message = userService.loginUser(loginRequestDto);
        return ResponseEntity.ok(message);
    }

}
