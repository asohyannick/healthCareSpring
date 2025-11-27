package com.medicalSolutionsInc.service.user;
import com.medicalSolutionsInc.config.jwtConfig.JWTConfiguration;
import com.medicalSolutionsInc.dto.userDTO.AuthRequestDTO;
import com.medicalSolutionsInc.dto.userDTO.UserResponseDTO;
import com.medicalSolutionsInc.entity.user.User;
import com.medicalSolutionsInc.mappers.userMapper.UserMapper;
import com.medicalSolutionsInc.repository.user.UserRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JWTConfiguration jwtConfiguration;
    private final JavaMailSender mailSender;
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper,
            JWTConfiguration jwtConfiguration,
            JavaMailSender mailSender
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtConfiguration = jwtConfiguration;
        this.mailSender = mailSender;
    }

    // Generate a random 6-digit two-factor authentication code
    private String generateTwoFactorCode(User savedUser) {
        String optCode = String.valueOf((int) ((Math.random() * 900000) + 100000));
        savedUser.setTwoFactorExpiry(LocalDateTime.now().plusMinutes(5).toString());
        savedUser.setTwoFactorCode(optCode);
        savedUser.setTwoFactorAttempts(0);
        userRepository.save(savedUser);
        return optCode;
    }

    public void send2FACodeEmail(String toEmail, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Your Two-Factor Authentication Code");
            String htmlContent = """
                    <p>Your two-factor authentication code is: <strong>%s</strong></p>
                    <p>This code will expire in 5 minutes.</p>
                    """.formatted(code);

            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);
            log.info("2FA email sent to {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send 2FA email to {}: {}", toEmail, e.getMessage(), e);
        }
    }
    public UserResponseDTO registerUser(AuthRequestDTO authRequestDTO) {
       if (userRepository.existsByEmail(authRequestDTO.email())) {
           throw new IllegalArgumentException("Email already exists");
       }
       User user = userMapper.toEntity(authRequestDTO);
       user.setPassword(passwordEncoder.encode(user.getPassword()));

       // Generate and send two-factor authentication code
        String twoFactorCode = generateTwoFactorCode(user);
        user.setTwoFactorCode(twoFactorCode);
        user.setIsTwoFactorVerified(Boolean.valueOf(String.valueOf(false)));


       // Generate tokens for the new user
        String accessToken = jwtConfiguration.generateAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtConfiguration.generateRefreshToken(user.getEmail());
        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);
        user = userRepository.save(user);
        // Sent the 2FA code via email
        send2FACodeEmail(user.getEmail(), twoFactorCode);
       return userMapper.toResponseDTO(user);
    }
}
