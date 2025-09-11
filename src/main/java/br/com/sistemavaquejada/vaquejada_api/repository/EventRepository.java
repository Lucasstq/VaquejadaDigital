package br.com.sistemavaquejada.vaquejada_api.repository;

import br.com.sistemavaquejada.vaquejada_api.entity.Event;
import br.com.sistemavaquejada.vaquejada_api.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByStatus(Status status);

}
