package de.dipf.edutec.thriller.experiencesampling.indicator.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserIdsDto {
    private List<String> ids = new ArrayList<>();
    private Long time;
}
