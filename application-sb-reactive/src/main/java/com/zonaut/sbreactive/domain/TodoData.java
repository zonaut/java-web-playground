package com.zonaut.sbreactive.domain;

import com.zonaut.common.database.JsonDataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoData extends JsonDataType {

    private String title;
    private String somethingElse;

}
