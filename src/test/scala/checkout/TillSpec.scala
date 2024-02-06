package checkout

import checkout.produce.{Produce, Stock}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TillSpec extends AnyWordSpec with Matchers {
  "Till" when {
    "totaling a list of items" should {
      "return (Some of) the value of one item if only one item passed" in new Setup {
        Till.total(stock, Seq("apple")) shouldBe Some(applePrice)
      }

      "return (Some of) the value of both items if two different items passed" in new Setup {
        Till.total(stock, Seq("apple", "orange")) shouldBe Some(applePrice + orangePrice)
      }

      "return (Some of) the value of multiple items if multiple duplicate items passed" in new Setup {
        Till.total(stock, Seq("apple", "orange", "orange", "apple")) shouldBe Some(applePrice + orangePrice + orangePrice + applePrice)
      }

      "return Some(0) if no items passed" in new Setup {
        Till.total(stock, Seq.empty) shouldBe Some(BigDecimal(0))
      }

      "return None if an invalid item passed" in new Setup {
        Till.total(stock, Seq("apple", "orange", "pear")) shouldBe None
      }
    }
  }

  class Setup {
    val applePrice = BigDecimal("0.60")
    val orangePrice = BigDecimal("0.25")
    val apples = Produce("apple", applePrice)
    val oranges = Produce("orange", orangePrice)
    val stock = Stock(Set(apples, oranges))
  }
}
