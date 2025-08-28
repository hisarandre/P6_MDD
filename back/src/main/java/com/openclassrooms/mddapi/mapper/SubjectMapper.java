package com.openclassrooms.mddapi.mapper;


import com.openclassrooms.mddapi.dto.subject.SubjectResponseDto;
import com.openclassrooms.mddapi.dto.subject.SubjectWithSubscriptionResponseDto;
import com.openclassrooms.mddapi.dto.user.UserResponseDto;
import com.openclassrooms.mddapi.entity.Subject;
import com.openclassrooms.mddapi.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    // Entity to DTO
    SubjectResponseDto toSubjectResponseDto(Subject subject);

    // Entity List to DTO List
    List<SubjectResponseDto> toSubjectResponseDtoList(List<Subject> subjects);

    // DTO to Entity
    Subject toSubject(SubjectResponseDto subjectDto);

    // DTO List to Entity List
    List<Subject> toSubjectList(List<SubjectResponseDto> subjectDtos);

    default SubjectWithSubscriptionResponseDto toSubjectWithSubscriptionDto(Subject subject, boolean isSubscribed) {
        if (subject == null) {
            return null;
        }
        SubjectWithSubscriptionResponseDto dto = new SubjectWithSubscriptionResponseDto();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        dto.setDescription(subject.getDescription());
        dto.setIsSubscribed(isSubscribed);
        return dto;
    }

    default List<SubjectWithSubscriptionResponseDto> toSubjectWithSubscriptionDtoList(
            List<Subject> subjects, List<Long> subscribedSubjectIds) {

        return subjects.stream()
                .map(subject -> toSubjectWithSubscriptionDto(subject, subscribedSubjectIds.contains(subject.getId())))
                .toList();
    }
}