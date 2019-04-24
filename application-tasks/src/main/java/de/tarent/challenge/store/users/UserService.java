package de.tarent.challenge.store.users;

import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserCatalog userCatalog;

	public UserService(UserCatalog productCatalog) {
		this.userCatalog = productCatalog;
	}

	
}
