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

CREATE SEQUENCE categories_id_seq START 1;

CREATE TABLE categories (
    id BIGINT NOT NULL DEFAULT nextval('categories_id_seq'::regclass),
    name VARCHAR(255) NOT NULL,
    CONSTRAINT categories_id_pk PRIMARY KEY (id)
);

CREATE SEQUENCE transactions_id_seq START 1;

CREATE TABLE transactions (
    id BIGINT NOT NULL DEFAULT nextval('transactions_id_seq'::regclass),
    category_id BIGINT,
    description VARCHAR(255),
    amount DECIMAL,
    post_date DATE,
    CONSTRAINT transactions_id_pk PRIMARY KEY (id),
    CONSTRAINT category_id_fk FOREIGN KEY (category_id) REFERENCES categories (id)
);