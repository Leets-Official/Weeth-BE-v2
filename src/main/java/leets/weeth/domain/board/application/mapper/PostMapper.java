package leets.weeth.domain.board.application.mapper;

import leets.weeth.domain.board.application.dto.PostDTO;
import leets.weeth.domain.board.domain.entity.Post;
import leets.weeth.domain.comment.application.mapper.CommentMapper;
import leets.weeth.domain.user.domain.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = CommentMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", source = "user")
    })
    Post fromPostDto(PostDTO.Save dto, List<String> fileUrls, User user);

//    @Mapping(target = "id", source = "postId")
//    @Mapping(target = "user", source = "user")
//    Post update(Long postId, PostDTO.Update dto, User user);

    @Mappings({
            @Mapping(target = "name", source = "user.name"),
            @Mapping(target = "comments", source = "comments"),
            @Mapping(target = "time", source = "modifiedAt")
    })
    PostDTO.Response toPostDto(Post post);

}
