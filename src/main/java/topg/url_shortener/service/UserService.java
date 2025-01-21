package topg.url_shortener.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import topg.url_shortener.Jwt.JwtUtils;
import topg.url_shortener.Jwt.UserImplDetails;
import topg.url_shortener.dto.*;
import topg.url_shortener.repository.UserRepository;
import topg.url_shortener.models.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        if (StringUtils.isBlank(userRequestDto.email()) ||
                StringUtils.isBlank(userRequestDto.password()) ||
                StringUtils.isBlank(userRequestDto.username())) {
            return UserResponseDto.builder()
                    .success(false)
                    .message("Email, password, or username cannot be blank.")
                    .data(null)
                    .build();
        }

        // Check if the email already exists
        if (userRepository.findByEmail(userRequestDto.email()).isPresent()) {
            return UserResponseDto.builder()
                    .success(false)
                    .message("Email is already taken.")
                    .data(null)
                    .build();
        }

        User user = User.builder()
                .email(userRequestDto.email())
                .username(userRequestDto.username())
                .password(passwordEncoder.encode(userRequestDto.password()))
                .role("ROLE_USER")
                .build();
        userRepository.save(user);
        UserDto userDto = new UserDto(user.getEmail(), user.getUsername());

        // Return a success response
        return UserResponseDto.builder()
                .success(true)
                .message("User created successfully.")
                .data(userDto) // Optionally, return the saved user object or its DTO version
                .build();
    }


    public JwtResponseDto loginUser(LoginRequestDto loginRequestDto) {
        // Create the authentication token with the provided email and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.username(),
                        loginRequestDto.password()
                )
        );

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserImplDetails userImplDetails = (UserImplDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userImplDetails);

        return new JwtResponseDto(jwt);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User with username " + username + " not found"));
    }

}
