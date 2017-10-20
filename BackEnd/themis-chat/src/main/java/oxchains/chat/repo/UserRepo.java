package oxchains.chat.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import oxchains.chat.entity.User;

/**
 * Created by xuqi on 2017/10/12.
 */
@Repository
public interface UserRepo extends CrudRepository<User,Long> {
    User findUserByUsernameAndPassword(String username, String password);
    User findUserByUsernameAndEmail(String username, String email);

}
