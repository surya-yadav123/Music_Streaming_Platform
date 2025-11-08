package in.main.services;
import in.main.entities.User;


public interface UserService {
User register(User user) throws Exception;
User login(String email, String password);
}
