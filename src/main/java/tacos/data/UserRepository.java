package tacos.data;


import org.springframework.data.repository.CrudRepository;
import tacos.User;

public interface UserRepository extends CrudRepository<User, Long> {
    //메서드 추가 정의
    User findByUsername(String username);
}
