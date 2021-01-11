package depavlo.mediaprocess.repo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import depavlo.mediaprocess.dao.ContentItemDao;
import depavlo.mediaprocess.service.model.ContentItem;

/**
 * The Interface ContentItemMapper that declares the required methods for
 * mapping DAO objects to Content Items.
 * 
 * @author Pavlo Degtyaryev
 */
@Mapper()
public interface ContentItemMapper {

	/** The mapper. */
	public ContentItemMapper MAPPER = Mappers.getMapper(ContentItemMapper.class);

	/**
	 * convert DTO to Content Item.
	 *
	 * @param contentItemDao the content item dao
	 * @return the content item
	 */
	@Mapping(source = "fileName", target = "name")
	@Mapping(source = "parentID", target = "parentId")
	@Mapping(target = "level", ignore = true)
	@Mapping(target = "dirs", ignore = true)
	@Mapping(target = "files", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "parentItem", ignore = true)
	public abstract ContentItem fromDto(ContentItemDao contentItemDao);
}
