package com.openclassrooms.mddapi.mapper;


import com.openclassrooms.mddapi.dto.comment.CommentResponseDto;
import com.openclassrooms.mddapi.dto.subject.SubjectResponseDto;
import com.openclassrooms.mddapi.dto.user.UserResponseDto;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    // Entity to DTO
    @Mapping(source = "author.username", target = "author")
    CommentResponseDto toCommentResponseDto(Comment comment);

    // Entity List to DTO List
    List<CommentResponseDto> toCommentResponseDtoList(List<Comment> comments);

}