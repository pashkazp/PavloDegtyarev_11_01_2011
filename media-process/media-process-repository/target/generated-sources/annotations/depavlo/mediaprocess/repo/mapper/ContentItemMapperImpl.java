package depavlo.mediaprocess.repo.mapper;

import depavlo.mediaprocess.dao.ContentItemDao;
import depavlo.mediaprocess.service.model.ContentItem;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-01-11T16:23:34+0200",
    comments = "version: 1.4.1.Final, compiler: Eclipse JDT (IDE) 1.3.1200.v20200916-0645, environment: Java 11.0.2 (Oracle Corporation)"
)
public class ContentItemMapperImpl implements ContentItemMapper {

    @Override
    public ContentItem fromDto(ContentItemDao contentItemDao) {
        if ( contentItemDao == null ) {
            return null;
        }

        ContentItem contentItem = new ContentItem();

        contentItem.setName( contentItemDao.getFileName() );
        contentItem.setParentId( contentItemDao.getParentID() );
        contentItem.setDirectory( contentItemDao.isDirectory() );
        contentItem.setId( contentItemDao.getId() );

        return contentItem;
    }
}
