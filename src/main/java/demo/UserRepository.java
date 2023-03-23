package demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    public List<User> findUsersByAgeAfter(int age);
    public User findUserByEmail(String email);
    public User deleteByEmail(String email);
}
