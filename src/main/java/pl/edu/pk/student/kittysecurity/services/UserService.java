package pl.edu.pk.student.kittysecurity.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pk.student.kittysecurity.entity.User;
import pl.edu.pk.student.kittysecurity.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepo;

    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
        this.encoder = new BCryptPasswordEncoder(12);
    }

    public User register(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
