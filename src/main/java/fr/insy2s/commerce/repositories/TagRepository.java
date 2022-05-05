package fr.insy2s.commerce.repositories;

import fr.insy2s.commerce.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

  @Transactional
  @Modifying
  @Query("delete from Tag t where t.id = ?1")
  int deleteTagById(Long id);
}
