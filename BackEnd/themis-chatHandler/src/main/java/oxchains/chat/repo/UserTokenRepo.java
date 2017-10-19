package oxchains.chat.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import oxchains.chat.auth.UserToken;

/**
 * Created by xuqi on 2017/10/13.
 */
@Repository
public interface UserTokenRepo extends CrudRepository<UserToken,String> {

}
