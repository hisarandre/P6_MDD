package com.openclassrooms.mddapi.dto.subject;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectWithSubscriptionResponseDto {
    Long id;
    String name;
    String description;
    Boolean isSubscribed;
}