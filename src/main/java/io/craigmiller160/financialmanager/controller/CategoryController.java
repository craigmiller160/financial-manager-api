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

package io.craigmiller160.financialmanager.controller;

import io.craigmiller160.financialmanager.dto.CategoryDto;
import io.craigmiller160.financialmanager.dto.CategoryListDto;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @GetMapping
    public CategoryListDto getAllCategories() {
        // TODO finish this
        return null;
    }

    @PostMapping
    public CategoryDto createCategory(@RequestBody final CategoryDto category) {
        // TODO finish this
        return null;
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable("id") final long id, @RequestBody CategoryDto category) {
        // TODO finish this
        return null;
    }

    @DeleteMapping("/{id}")
    public CategoryDto deleteCategory(@PathVariable("id") final long id) {
        // TODO finish this
        return null;
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable("id") final long id) {
        // TODO finish this
        return null;
    }

}
