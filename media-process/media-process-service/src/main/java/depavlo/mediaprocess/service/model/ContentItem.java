package depavlo.mediaprocess.service.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import depavlo.mediaprocess.util.ContentItemState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ContentItem that represent Item in file\directory structure.
 * 
 * @author Pavlo Degtyaryev
 */
@ToString(of = { "id", "name", "isDirectory", "parentId" })
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class ContentItem implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4044449262929604449L;

	/** The id. */
	private Long id;

	/** The name of Item. */
	private String name = "";

	/** The is directory. */
	private boolean isDirectory;

	/** The parent id. */
	private Long parentId;

	/** The parent item. */
	private ContentItem parentItem;

	/** The level. */
	private int level = 0;

	/**
	 * Sets the dirs.
	 *
	 * @param dirs the new dirs
	 */
	@Setter(value = AccessLevel.PRIVATE)
	private List<ContentItem> dirs = new ArrayList<>();

	/**
	 * Sets the files.
	 *
	 * @param files the new files
	 */
	@Setter(value = AccessLevel.PRIVATE)
	private List<ContentItem> files = new ArrayList<>();

	/** The status. */
	private ContentItemState status = ContentItemState.WAITING;

	/**
	 * Checks if is file.
	 *
	 * @return true, if is file
	 */
	public boolean isFile() {
		return !this.isDirectory;
	}

	/**
	 * Checks if is empty.
	 *
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return files.size() == 0 && dirs.size() == 0;
	}

	/**
	 * Adds the content item.
	 *
	 * @param item the item
	 */
	public void addContentItem(ContentItem item) {
		if (item.isDirectory) {
			dirs.add(item);
		} else {
			files.add(item);
		}
		item.setParentItem(this);
		item.setLevel(this.getLevel() + 1);
	}

	/**
	 * Gets the qualified name based on parent names.
	 *
	 * @return the qualified name
	 */
	public String getQualifiedName() {
		log.debug("getQualifiedName]");
		StringBuilder sb = new StringBuilder();

		sb.append(this.name);
		log.debug("getQualifiedName] add name part \"{}\"", this.name);
		var parent = getParentItem();

		while (parent != null) {
			sb.insert(0, "\\");
			sb.insert(0, parent.getName());
			log.debug("getQualifiedName] add name part \"{}\"", parent.getName());
			parent = parent.getParentItem();
		}

		return sb.toString();
	}
}
