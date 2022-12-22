package ru.ellada.ecommerce.repos;

import ru.ellada.ecommerce.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A repository for {@link User} objects providing a set of JPA methods for working with the database.
 * Inherits interface {@link JpaRepository}.
 *
 *@author Govorukhin Dmitriy
 * @version 1.0
 * @see User
 * @see JpaRepository
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Returns the user from the database that has the same name as the value of the input parameter.
     *
     * @param username user name to return.
     * @return The {@link User} class object.
     */
    User findByUsername(String username);

    /**
     * Returns the user from the database that has the same activation code as the value of the input parameter.
     *
     * @param code activation code to return.
     * @return The {@link User} class object.
     */
    User findByActivationCode(String code);


    /**
     * Returns the user from the database by email as the value of the input parameter.
     *
     * @param email user email to return.
     * @return The {@link User} class object.
     */
    User findUserByEmail(String email);
}