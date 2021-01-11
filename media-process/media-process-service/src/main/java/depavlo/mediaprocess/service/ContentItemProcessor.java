package depavlo.mediaprocess.service;

import java.util.Comparator;
import java.util.Map;

import depavlo.mediaprocess.service.model.ContentItem;
import depavlo.mediaprocess.util.ContentItemState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ContentItemProcessor.
 */
@ToString
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class ContentItemProcessor {

	/** The root item of Content Item tree . */
	private final ContentItem rootItem;

	/** The all content items. */
	private final Map<Long, ContentItem> items;

	/**
	 * Fold content item tree.
	 */
	public void foldContentItemTree() {
		log.debug("foldContentItemTree]");

		processEmptyDir();
		processTiff();
		processMovie();
		processCopiedFiles();

	}

	/**
	 * Process tiff files.
	 */
	private void processTiff() {
		log.debug("processTiff]");
		boolean foldedSomething;
		do {
			foldedSomething = false;
			var tifNamedItem = items.values()
					.stream()
					.filter(v -> v.isFile())
					.filter(v -> v.getName().matches("^.*\\.tif$"))
					.sorted(Comparator.comparing(ContentItem::getLevel))
					.findFirst();

			log.debug("processTiff] get tiff item if it present: {}", tifNamedItem);

			if (tifNamedItem.isPresent() && tifNamedItem.get().getParentItem() != null) {
				var parentItem = tifNamedItem.get().getParentItem();
				log.debug("processTiff] get items parent forder if exist and fold it: {}", parentItem);
				foldTiffParentItem(parentItem);
				pushStatus(parentItem, ContentItemState.HASCODED);
				foldedSomething = true;
			}

		} while (foldedSomething);
	}

	/**
	 * Process movie files.
	 */
	private void processMovie() {
		log.debug("processMovie]");
		boolean foldedSomething;
		do {
			foldedSomething = false;
			var movieNamedItem = items.values()
					.stream()
					.filter(v -> v.isFile())
					.filter(v -> v.getStatus() == ContentItemState.WAITING)
					.filter(v -> v.getName().matches("^.*\\.avi|^.*\\.mov"))
					.sorted(Comparator.comparing(ContentItem::getLevel))
					.findFirst();

			log.debug("processMovie] get \"mov\"|\"avi\" item if it present: {}", movieNamedItem);

			if (movieNamedItem.isPresent()) {
				foldMovieItem(movieNamedItem.get());
				pushStatus(movieNamedItem.get(), ContentItemState.HASCODED);
				foldedSomething = true;
			}

		} while (foldedSomething);
	}

	/**
	 * Process empty dirs.
	 */
	private void processEmptyDir() {
		log.debug("processEmptyDir]");
		boolean foldedSomething;
		do {
			foldedSomething = false;

			var emptyDirItem = items.values()
					.stream()
					.filter(v -> v.isDirectory())
					.filter(v -> v.isEmpty())
					.findAny();

			log.debug("processEmptyDir] get empty dir item if it present: {}", emptyDirItem);

			if (emptyDirItem.isPresent()) {
				deflate(emptyDirItem.get());
				foldedSomething = true;
			}

		} while (foldedSomething);
	}

	/**
	 * Process copied files.
	 */
	private void processCopiedFiles() {
		log.debug("processCopiedFiles]");
		for (ContentItem item : items.values()) {
			if (item.isFile() && item.getStatus() == ContentItemState.WAITING) {
				item.setStatus(ContentItemState.COPIED);
				pushStatus(item, ContentItemState.HASCOPIED);
				log.debug("processCopiedFiles] mark files as copied: {}", item);
			}
		}
		boolean foldedSomething;
		do {
			foldedSomething = false;
			var hasCopiedItem = items.values()
					.stream()
					.filter(v -> v.isDirectory())
					.filter(v -> v.getStatus() == ContentItemState.HASCOPIED)
					.sorted(Comparator.comparing(ContentItem::getLevel))
					.findFirst();

			log.debug("processCopiedFiles] get HASCOPIED dir item if it present and deflate it: {}", hasCopiedItem);

			if (hasCopiedItem.isPresent()) {
				reduceItems(hasCopiedItem.get());
				hasCopiedItem.get().setStatus(ContentItemState.COPIED);
				foldedSomething = true;
			}
		} while (foldedSomething);
	}

	/**
	 * Deflate.
	 *
	 * @param item the item
	 */
	private void deflate(ContentItem item) {
		log.debug("deflate] try deflate item: {}", item);
		while (item != null && item.isEmpty()) {
			ContentItem parent = item.getParentItem();
			if (parent != null) {
				parent.getDirs().remove(item);
			}
			items.remove(item.getId());
			item = parent;
			log.debug("deflate] try deflate upward item: {}", item);
		}

	}

	/**
	 * Fold tiff parent directory item.
	 *
	 * @param parentItem the parent item
	 */
	private void foldTiffParentItem(ContentItem parentItem) {
		log.debug("foldTiffParentItem] fold item: {}", parentItem);
		reduceItems(parentItem);
		parentItem.setStatus(ContentItemState.CODED);
		log.debug("foldTiffParentItem] mark dir as folded and coded: {}", parentItem);
	}

	/**
	 * Fold movie item.
	 *
	 * @param item the item
	 */
	private void foldMovieItem(ContentItem item) {
		log.debug("foldMovieItem] mark item as coded: {}", item);
		item.setStatus(ContentItemState.CODED);
	}

	/**
	 * Reduce items which were in the deleted folder.
	 *
	 * @param item the item
	 */
	private void reduceItems(ContentItem item) {
		log.debug("reduceItems] reduce item: {}", item);
		for (ContentItem i : item.getFiles()) {
			log.debug("reduceItems] remove file from items: {}", i);
			items.remove(i.getId());
		}
		for (ContentItem i : item.getDirs()) {
			reduceItems(i);
			log.debug("reduceItems] remove dir from items: {}", i);
			items.remove(i.getId());
		}
	}

	/**
	 * Normalize Content Item names.
	 */
	public void normalizeNames() {
		log.debug("normalizeNames]");
		items.forEach((k, item) -> {
			String[] nameParts = item.getName().split("\\\\");
			if (nameParts.length > item.getLevel()) {
				log.debug("normalizeNames] get part \"{}\" from: {}", nameParts[item.getLevel()], item.getName());
				item.setName(nameParts[item.getLevel()]);
			}
		});
	}

	/**
	 * Push status.
	 *
	 * @param item   the item
	 * @param status the status
	 */
	public void pushStatus(ContentItem item, ContentItemState status) {
		log.debug("pushStatus]");
		var parent = item.getParentItem();
		while (parent != null && parent.getStatus() == ContentItemState.WAITING) {
			parent.setStatus(status);
			log.debug("pushStatus] set status {} to item: {}", status, parent);
			parent = parent.getParentItem();
		}
	}

}
