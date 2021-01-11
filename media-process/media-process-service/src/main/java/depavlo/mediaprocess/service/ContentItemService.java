package depavlo.mediaprocess.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import depavlo.mediaprocess.service.model.ContentItem;

/**
 * ContentItemService interface that declares the required methods.
 * 
 * @author Pavlo Degtyaryev
 */
@Service
public interface ContentItemService {

	/**
	 * Gets the items map.
	 *
	 * @return the items map
	 */
	public Map<Long, ContentItem> getItemsMap();

	/**
	 * Convert to tree.
	 *
	 * @param items the items
	 * @return the optional
	 */
	public Optional<ContentItem> convertToTree(Map<Long, ContentItem> items);

}
