package com.support_dashboard.TellMe.repository;

import com.support_dashboard.TellMe.model.Incident;
import com.support_dashboard.TellMe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Tells Spring this is a repository bean — Spring manages its lifecycle and injects it wherever needed. It also enables Spring's exception translation — database-specific exceptions get converted to Spring's unified exception hierarchy, so you handle DataAccessException instead of PostgreSQL-specific errors
public interface UserRepository extends JpaRepository<User,Long> { //eliminated long codes using interface, inherits from JpaRepository
//    This is modern Java — Optional was introduced specifically to eliminate null pointer exceptions on database lookups

    Optional<User> findByEmail(String email); //Optional<User> forces to handle the "not found" case explicitly

//    Just returns true or false. No data transferred, faster query, cleaner code.
//    SELECT EXISTS(SELECT 1 FROM users WHERE email = ?)

    boolean existsByEmail(String email);

//    Traditional JAVA approach
//  This helps in removing this approach
//    public User findByEmail(String email) {
//    String SQL = "SELECT * FROM users WHERE email = ?";
//    PreparedStatement stmt = connection.prepareStatement(sql);
//    stmt.setString(1, email);
//    ResultSet rs = stmt.executeQuery();
//    // manually map each column to User fields...
//}

}



