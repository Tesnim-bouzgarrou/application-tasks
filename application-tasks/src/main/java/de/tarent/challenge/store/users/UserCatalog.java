package de.tarent.challenge.store.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCatalog extends JpaRepository<User, Long> {


}
