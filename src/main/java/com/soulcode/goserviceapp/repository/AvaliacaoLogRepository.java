package com.soulcode.goserviceapp.repository;

import com.soulcode.goserviceapp.domain.AvaliacaoLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AvaliacaoLogRepository extends MongoRepository<AvaliacaoLog, String> {
}
