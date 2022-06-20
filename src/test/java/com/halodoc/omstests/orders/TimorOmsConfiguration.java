package com.halodoc.omstests.orders;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.halodoc.transformers.havelock.configuration.HavelockConfiguration;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties (ignoreUnknown = true)
public class TimorOmsConfiguration {
    @NotNull
    private HavelockConfiguration havelockConfiguration;
}