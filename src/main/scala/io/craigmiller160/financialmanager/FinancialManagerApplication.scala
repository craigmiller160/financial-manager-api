package io.craigmiller160.financialmanager

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class FinancialManagerApplication {
  println("Working") // TODO delete this
}
object FinancialManagerApplication extends App {
  SpringApplication.run(classOf[FinancialManagerApplication])
}
