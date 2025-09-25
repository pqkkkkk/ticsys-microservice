package org.pqkkkkk.ticsys.promotion_service.infrastructure.client.mock;

import org.pqkkkkk.ticsys.promotion_service.domain.service.external.IdentityService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class IdentityMockClient implements IdentityService {

    @Override
    public boolean isValidUser(Long userId) {
        return true;
    }

}
