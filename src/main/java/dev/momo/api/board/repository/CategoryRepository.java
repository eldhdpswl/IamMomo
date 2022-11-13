package dev.momo.api.board.repository;

import dev.momo.api.board.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

//CrudRepository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    //List<Category> findCategory(Pageable pageable);
}
