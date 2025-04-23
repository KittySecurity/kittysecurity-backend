package pl.edu.pk.student.kittysecurity.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepo, AuthenticationManager authManager, JwtService jwt) {
        this.userRepo = userRepo;
        this.encoder = new BCryptPasswordEncoder(12);
        this.authManager = authManager;
        this.jwtService = jwt;
    }

    public User register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public String verify(User user){
        Authentication authentication = authManager.
                authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());
        return "Failed";
    }

    public List<User> getAllStudents() {
        return userRepo.findAll();
    }
}
