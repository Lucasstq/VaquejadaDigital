package br.com.sistemavaquejada.vaquejada_api.repository;

import br.com.sistemavaquejada.vaquejada_api.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
