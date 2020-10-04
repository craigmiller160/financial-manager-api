package io.craigmiller160.financialmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CategoryListDto (
        @JsonProperty("categories") List<CategoryDto> categories
) {}
