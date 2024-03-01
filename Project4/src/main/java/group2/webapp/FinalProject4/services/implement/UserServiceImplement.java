package group2.webapp.FinalProject4.services.implement;

import group2.webapp.FinalProject4.models.User;
import group2.webapp.FinalProject4.repositories.UserRepository;
import group2.webapp.FinalProject4.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class UserServiceImplement implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User getUserByUserNameAndPassWord(String username, String password) {

        return userRepository.getUserByUserNameAndPassWord(username, password);
    }

    @Override
    public User getUserByUserName(String username) {
        return userRepository.getUserByUserName(username);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByUserNameAndPassWordAndRole(String username, String password, int role) {

        return userRepository.getUserByUserNameAndPassWordAndRole(username, password, role);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.getById(id);
    }

    @Override
    public boolean checkEdiUsername(String username, int id) {
        return userRepository.findUserByUserNameAndId(username, id).isPresent();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> pagingUser(int offset, int pagesize) {
        return userRepository.findAll(PageRequest.of(offset, pagesize, Sort.by("id").descending()));
    }

    @Override
    public List<User> findAllByRoleAndStatus(int role, int status) {
        return userRepository.findAllByRoleAndStatus(role, status);
    }

    @Override
    public List<User> findUserByRole(int id) {
        return userRepository.findUserByRole(id);
    }

    @Override
    public User getUserByUserNameAndRole(String username, int i) {
        return null;
    }

}
