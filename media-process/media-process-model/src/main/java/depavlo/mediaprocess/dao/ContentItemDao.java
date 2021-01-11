package depavlo.mediaprocess.dao;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import depavlo.mediaprocess.util.ContentItemDtoStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class ContentItemDao that represent the persisted Content Items.
 * 
 * @author Pavlo Degtyaryev
 */
@ToString(of = { "id", "fileName", "parentID", "isDirectory" })
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "san_content_999_calculated")
@Slf4j
public class ContentItemDao implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 969409648368855431L;

	/** The id. */
	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ContentID")
	private Long id;

	/** The file name. */
	@Column(name = "FileName", nullable = true, length = 1020, unique = true)
	private String fileName;

	/** The level. */
	@Column(name = "Level", nullable = true)
	private Integer level;

	/** The size. */
	@Column(name = "Size", nullable = true)
	private Long size;

	/** The count. */
	@Column(name = "Count", nullable = false)
	private Integer count = 0;

	/** The status. */
	@Column(name = "Status", nullable = false)
	@Enumerated(EnumType.STRING)
	private ContentItemDtoStatus status = ContentItemDtoStatus.NEW;

	/** The parent ID. */
	@Column(name = "ParentID", nullable = true)
	private Long parentID = 0L;

	/** The modified on. */
	@Column(name = "ModifiedOn", nullable = true)
	private Timestamp modifiedOn;

	/** The max modified on. */
	@Column(name = "MaxModifiedOn", nullable = true)
	private Timestamp maxModifiedOn;

	/** The deleted on. */
	@Column(name = "DeletedOn", nullable = true)
	private Timestamp deletedOn;

	/** The is directory. */
	@Column(name = "IsDirectory", nullable = true)
	private boolean isDirectory;

	/** The owner. */
	@Column(name = "Owner", nullable = true, length = 127)
	private String owner;

	/** The group. */
	@Column(name = "Group", nullable = true, length = 127)
	private String group;

	/** The mode. */
	@Column(name = "Mode", nullable = true, length = 15)
	private String mode;

	/** The updated on. */
	@Column(name = "UpdatedOn", nullable = true)
	private Timestamp updatedOn;

	/** The t S in future notified. */
	@Column(name = "TSInFutureNotified", nullable = true)
	private Boolean tSInFutureNotified;

	/** The tree lmts hash. */
	@Column(name = "TreeLmtsHash", nullable = true, length = 32)
	private String treeLmtsHash;

	/** The predictive state ID. */
	@Column(name = "PredictiveStateID", nullable = false)
	private Integer predictiveStateID = 0;

}