package Appcenter.study.domain.repository;

import Appcenter.study.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
