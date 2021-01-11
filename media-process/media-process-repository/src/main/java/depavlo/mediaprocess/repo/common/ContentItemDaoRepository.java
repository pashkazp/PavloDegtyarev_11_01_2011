package depavlo.mediaprocess.repo.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import depavlo.mediaprocess.dao.ContentItemDao;

/**
 * The Interface ContentItemDaoRepository that declares the required methods for
 * work with database
 * 
 * @author Pavlo Degtyaryev
 */
@Repository
public interface ContentItemDaoRepository extends JpaRepository<ContentItemDao, Long> {

	/**
	 * Gets the all ContentItemDao from database.
	 *
	 * @return the all content item dao
	 */
	@Query(value = "SELECT u FROM ContentItemDao u")
	List<ContentItemDao> getAllContentItemDao();
}
