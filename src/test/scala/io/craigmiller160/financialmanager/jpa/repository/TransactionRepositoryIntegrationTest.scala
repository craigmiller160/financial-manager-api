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

package io.craigmiller160.financialmanager.jpa.repository

import io.craigmiller160.oauth2.config.OAuthConfig
import org.junit.jupiter.api.`extension`.ExtendWith
import org.scalatest.BeforeAndAfterEach
import org.scalatest.wordspec.AnyWordSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestContextManager
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(Array(classOf[SpringExtension]))
class TransactionRepositoryIntegrationTest extends AnyWordSpec with BeforeAndAfterEach {

    @MockBean
    private var oauthConfig: OAuthConfig = _

    @Autowired
    private var transactionRepo: TransactionRepository = _

    new TestContextManager(getClass).prepareTestInstance(this)

    override def beforeEach(): Unit = {
        println(transactionRepo) // TODO delete this
    }

    "First Test" when {
        "do stuff" should {
            "happy" in {
                println("Working")
            }
        }

        "other" in {
            println("Working 2")
        }
    }

}
