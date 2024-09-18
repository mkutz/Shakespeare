package examples.java;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.YearMonth;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.shakespeareframework.Actor;
import org.shakespeareframework.Fact;

class FactDocTest {

  @Test
  void act1() {
    // tag::create-actor[]
    var kate = new Actor("Kate");
    // end::create-actor[]
    // tag::learn-fact[]
    kate.learns(new PhoneNumber("+49 0180 4 100 100"));
    // end::learn-fact[]
    // tag::remember-fact[]
    PhoneNumber katesPhoneNumber = kate.remembers(PhoneNumber.class);
    // end::remember-fact[]
    assertThat(katesPhoneNumber).isInstanceOf(PhoneNumber.class);
    // tag::relearn-fact[]
    kate.learns(new PhoneNumber("+1 (303) 499-7111"));
    // end::relearn-fact[]
    // tag::remember-changed-fact[]
    PhoneNumber katesNewPhoneNumber = kate.remembers(PhoneNumber.class);
    assertThat(katesNewPhoneNumber).isEqualTo(new PhoneNumber("+1 (303) 499-7111"));
    // end::remember-changed-fact[]
  }

  // tag::fact[]
  record PhoneNumber(String number) implements Fact {}

  // end::fact[]

  @Test
  void act2() {
    var robin = new Actor("Robin");
    // tag::learn-poly-fact[]
    robin.learns(new PhoneNumbers("+49 0180 4 100 100", "+1 (303) 499-7111"));
    // end::learn-poly-fact[]
    // tag::remember-poly-fact[]
    var robinsPhoneNumbers = robin.remembers(PhoneNumbers.class);
    var robinsHomePhoneNumber = robinsPhoneNumbers.home();
    var robinsWorkPhoneNumber = robinsPhoneNumbers.work();
    // end::remember-poly-fact[]
    assertThat(robinsPhoneNumbers).isInstanceOf(PhoneNumbers.class);
    assertThat(robinsHomePhoneNumber).isNotBlank();
    assertThat(robinsWorkPhoneNumber).isNotBlank();
  }

  // tag::poly-fact[]
  record PhoneNumbers(String home, String work) implements Fact {}

  // end::poly-fact[]

  @Test
  void act3() {
    var bill = new Actor("Bill");
    // tag::learn-default-fact
    bill.learns(CreditCard.DEFAULT_VISA);
    // end::learn-default-fact
    assertThat(bill.remembers(CreditCard.class)).isEqualTo(CreditCard.DEFAULT_VISA);
    // tag::learn-generated-fact
    bill.learns(EmailAddress.random());
    // end::learn-generated-fact
    assertThat(bill.remembers(EmailAddress.class)).isNotEqualTo(EmailAddress.random());
  }

  // tag::fact-with-default[]
  record CreditCard(String type, String number, YearMonth expiration) implements Fact {
    public static CreditCard DEFAULT_VISA =
        new CreditCard("visa", "4111111111111111", YearMonth.of(2026, 10));
  }

  // end::fact-with-default[]

  // tag::fact-with-generator[]
  record EmailAddress(String address) implements Fact {
    public static EmailAddress random() {
      return new EmailAddress("%s@shakespeareframework.org".formatted(UUID.randomUUID()));
    }
  }
  // end::fact-with-generator[]

}
