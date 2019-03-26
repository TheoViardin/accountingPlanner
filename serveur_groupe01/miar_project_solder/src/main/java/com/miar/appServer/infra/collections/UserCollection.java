package com.miar.appServer.infra.collections;

import com.miar.appServer.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCollection extends MongoRepository<User, String> {
    User findByPseudo(String pseudo);
}
