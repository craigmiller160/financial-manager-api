/*
 *     Financial Manager API
 *     Copyright (C) 2020 Craig Miller
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.craigmiller160.financialmanager.integration;

import io.craigmiller160.financialmanager.dto.CategoryDto;
import io.craigmiller160.financialmanager.dto.CategoryListDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CategoryControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Test
    public void test_getAllCategories() {
        var result = apiTestProcessor.call(apiConfig -> {
            apiConfig.request(requestConfig -> {
                requestConfig.setPath("/categories");
            });
        }).convert(CategoryListDto.class);

        assertEquals(2, result.categories().size());
        assertEquals(new CategoryDto(1L, "First"), result.categories().get(0));
        assertEquals(new CategoryDto(2L, "Second"), result.categories().get(1));
    }

    @Test
    public void test_createCategory() {
        throw new RuntimeException();
    }

}
