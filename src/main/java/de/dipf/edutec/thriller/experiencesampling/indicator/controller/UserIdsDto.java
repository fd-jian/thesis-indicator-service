package de.dipf.edutec.thriller.experiencesampling.indicator.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserIdsDto {
    private List<String> ids;
    private Long time;
}
