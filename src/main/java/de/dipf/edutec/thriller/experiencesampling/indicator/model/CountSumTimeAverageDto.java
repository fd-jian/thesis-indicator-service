package de.dipf.edutec.thriller.experiencesampling.indicator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CountSumTimeAverageDto {

  private Long count;
  private String time;
  private String timeSumSec;
  private String countPerSecond;

}










