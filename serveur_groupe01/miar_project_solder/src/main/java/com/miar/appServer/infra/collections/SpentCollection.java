package com.miar.appServer.infra.collections;

import com.miar.appServer.entities.Event;
import com.miar.appServer.entities.Spent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SpentCollection extends MongoRepository<Spent, String> {
    Spent findBySpentId(String id);
    //Spent findByCreatorPseudo(String pseudo);
    Spent findByCreationDate(Date date);
    void delete(Spent spent);
	List<Spent> findByCreatorPseudoAndEventId(String CreatorPseudo, String eventId);
	List<Spent> findByCreatorPseudo(String pseudo);
}
