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
public class AccelerometerRecordDto {

  private long time;
  private Float x;
  private Float y;
  private Float z;

}










