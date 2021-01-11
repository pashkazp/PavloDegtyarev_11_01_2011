package depavlo.mediaprocess.repo.serviceimpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import depavlo.mediaprocess.repo.common.ContentItemDaoRepository;
import depavlo.mediaprocess.repo.mapper.ContentItemMapper;
import depavlo.mediaprocess.service.ContentItemService;
import depavlo.mediaprocess.service.model.ContentItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ContentItemServiceImpl is implementation of ContentItemService
 * interface.
 * 
 * @author Pavlo Degtyaryev
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ContentItemServiceImpl implements ContentItemService {

	/** The Data Access Object. */
	private final ContentItemDaoRepository dao;

	/**
	 * Gets content items from the database and converts them to a map.
	 *
	 * @return the items map
	 */
	@Override
	public Map<Long, ContentItem> getItemsMap() {
		log.debug("getItemsMap] get contentItemDao(s) from database.");
		var items = dao.getAllContentItemDao();
		var newItems = new HashMap<Long, ContentItem>();

		log.debug("getItemsMap] convert contentItemDao(s) to ContentItem(s) to items map.");
		items.forEach((contentItemDao) -> {
			var newItem = ContentItemMapper.MAPPER.fromDto(contentItemDao);
			newItems.put(newItem.getId(), newItem);
		});

		log.debug("getItemsMap] return items map.");
		return newItems;
	}

	/**
	 * Convert map of elements to true tree.
	 *
	 * @param items the items
	 * @return the optional
	 */
	@Override
	public Optional<ContentItem> convertToTree(Map<Long, ContentItem> items) {
		log.debug("convertToTree]");
		var values = items.values();
		ContentItem root = null;

		for (ContentItem item : values) {
			log.debug("convertToTree] item {}", item.getId());
			if (root == null && item.isDirectory() && item.getParentId() == null) {
				log.debug("convertToTree] set it to root");
				root = item;
				root.setLevel(0);
			}
			if (item.getParentId() != null) {
				var parent = items.get(item.getParentId());
				if (parent != null) {
					log.debug("convertToTree] connect parent with child {}:{}", parent.getId(), item.getId());
					parent.addContentItem(item);
				}
			}
		}

		if (root == null) {
			return Optional.empty();
		} else {
			return Optional.of(root);
		}
	}

}
