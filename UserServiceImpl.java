package in.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.main.entities.User;
import in.main.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Override
    public User register(User user) throws Exception {
        if (user == null) {
            throw new Exception("User is null");
        }

        Optional<User> existing = repo.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            throw new Exception("Email already registered");
        }

        return repo.save(user);
    }

    @Override
    public User login(String email, String password) {
        Optional<User> optionalUser = repo.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return null; // user not found
        }

        User user = optionalUser.get();
        if (user.getPassword().equals(password)) {
            return user;
        }

        return null; // invalid password
    }
}
