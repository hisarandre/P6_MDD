package com.openclassrooms.mddapi.mapper;


import com.openclassrooms.mddapi.dto.post.PostDetailsResponseDto;
import com.openclassrooms.mddapi.dto.post.PostResponseDto;
import com.openclassrooms.mddapi.dto.subject.SubjectResponseDto;
import com.openclassrooms.mddapi.dto.user.UserResponseDto;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    // Entity to DTO
    @Mapping(source = "author.username", target = "author")
    PostResponseDto toPostResponseDto(Post post);

    // Entity List to DTO List
    List<PostResponseDto> toPostResponseDtoList(List<Post> post);

    // Entity to DTO
    @Mapping(source = "author.username", target = "author")
    @Mapping(source = "subject.name", target = "subject")
    PostDetailsResponseDto toPostDetailsResponseDto(Post post);

}