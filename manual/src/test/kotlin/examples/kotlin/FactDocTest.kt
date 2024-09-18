package examples.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.shakespeareframework.Actor
import org.shakespeareframework.Fact

import java.time.YearMonth
import java.util.UUID

class FactDocTest {

  @Test
  fun act1() {
    // tag::create-actor[]
    val kate = Actor("Kate")
    // end::create-actor[]
    // tag::learn-fact[]
    kate.learns(PhoneNumber("+49 0180 4 100 100"))
    // end::learn-fact[]
    // tag::remember-fact[]
    val katesPhoneNumber = kate.remembers(PhoneNumber::class.java)
    // end::remember-fact[]
    assertThat(katesPhoneNumber).isInstanceOf(PhoneNumber::class.java)
    // tag::relearn-fact[]
    kate.learns(PhoneNumber("+1 (303) 499-7111"))
    // end::relearn-fact[]
    // tag::remember-changed-fact[]
    val katesNewPhoneNumber = kate.remembers(PhoneNumber::class.java)
    assertThat(katesNewPhoneNumber).isEqualTo(PhoneNumber("+1 (303) 499-7111"))
    // end::remember-changed-fact[]
  }

  // tag::fact[]
  data class PhoneNumber(val number: String) : Fact

  // end::fact[]

  @Test
  fun act2() {
    val robin = Actor("Robin")
    // tag::learn-poly-fact[]
    robin.learns(PhoneNumbers(home = "+49 0180 4 100 100", work = "+1 (303) 499-7111"))
    // end::learn-poly-fact[]
    // tag::remember-poly-fact[]
    val robinsPhoneNumbers = robin.remembers(PhoneNumbers::class.java)
    val robinsHomePhoneNumber = robinsPhoneNumbers.home
    val robinsWorkPhoneNumber = robinsPhoneNumbers.work
    // end::remember-poly-fact[]
    assertThat(robinsPhoneNumbers).isInstanceOf(PhoneNumbers::class.java)
    assertThat(robinsHomePhoneNumber).isNotBlank
    assertThat(robinsWorkPhoneNumber).isNotBlank
  }

  // tag::poly-fact[]
  data class PhoneNumbers(val home: String, val work: String) : Fact

  // end::poly-fact[]

  @Test
  fun act3() {
    val bill = Actor("Bill")
    // tag::learn-default-fact
    bill.learns(CreditCard.DEFAULT_VISA)
    // end::learn-default-fact
    assertThat(bill.remembers(CreditCard::class.java)).isEqualTo(CreditCard.DEFAULT_VISA)
    // tag::learn-generated-fact
    bill.learns(EmailAddress.random())
    // end::learn-generated-fact
    assertThat(bill.remembers(EmailAddress::class.java)).isNotEqualTo(EmailAddress.random())
  }

  // tag::fact-with-default[]
  data class CreditCard(val type: String, val number: String, val expiration: YearMonth) : Fact {
    companion object {
      val DEFAULT_VISA =
        CreditCard("visa", "4111111111111111", YearMonth.of(2026, 10))
    }
  }

  // end::fact-with-default[]

  // tag::fact-with-generator[]
  data class EmailAddress(val address: String) : Fact {
    companion object {
      fun random() = EmailAddress("${UUID.randomUUID()}@shakespeareframework.org")
    }
  }
  // end::fact-with-generator[]

}
