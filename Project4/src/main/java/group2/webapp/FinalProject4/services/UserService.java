package group2.webapp.FinalProject4.services;

import group2.webapp.FinalProject4.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    User getUserByUserNameAndPassWord(String username, String password);

    User getUserByUserName(String username);

    void saveUser(User user);

    User getUserByUserNameAndPassWordAndRole(String username, String password, int role);

    User getUserById(Integer id);

    boolean checkEdiUsername(String username, int id);

    List<User> findAll();

    void deleteUserById(Integer id);

    Page<User> pagingUser(int offset, int pagesize);

    List<User> findAllByRoleAndStatus(int role, int status);

    List<User> findUserByRole(int id);

    User getUserByUserNameAndRole(String username, int i);
}
