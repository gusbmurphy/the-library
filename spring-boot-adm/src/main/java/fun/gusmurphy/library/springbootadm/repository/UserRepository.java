package fun.gusmurphy.library.springbootadm.repository;

import fun.gusmurphy.library.springbootadm.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {}
