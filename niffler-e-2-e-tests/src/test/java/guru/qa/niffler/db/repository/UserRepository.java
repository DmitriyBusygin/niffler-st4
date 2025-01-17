package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    UserAuthEntity createInAuth(UserAuthEntity user);

    UserEntity createInUserdata(UserEntity user);

    UserAuthEntity updateUserInAuth(UserAuthEntity user);

    UserEntity updateUserInUserdata(UserEntity user);

    Optional<UserAuthEntity> findByIdInAuth(UUID id);

    Optional<UserEntity> findByIdInUserData(UUID id);

    void deleteInAuthById(UUID id);

    void deleteInUserdataById(UUID id);
}
