package checkout

import checkout.offers.Offers
import checkout.produce.{Produce, Stock}
import org.mockito.scalatest.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TillSpec extends AnyWordSpec with Matchers with MockitoSugar {
  "Till" when {
    "totaling a list of items" should {
      "return (Some of) the value of one item if only one item passed" in new Setup {
        till.total(Seq("apple")) shouldBe Some(applePrice)
      }

      "return (Some of) the value of both items if two different items passed" in new Setup {
        till.total(Seq("apple", "orange")) shouldBe Some(applePrice + orangePrice)
      }

      "return (Some of) the value of multiple items if multiple duplicate items passed" in new Setup {
        till.total(Seq("apple", "orange", "orange", "apple")) shouldBe Some(applePrice + orangePrice + orangePrice + applePrice)
      }

      "return Some(0) if no items passed" in new Setup {
        till.total(Seq.empty) shouldBe Some(BigDecimal(0))
      }

      "return None if an invalid item passed" in new Setup {
        till.total(Seq("apple", "orange", "pear")) shouldBe None
      }
    }

    "calculating discount" should {
      "return the discount" in new Setup {
        val items = Seq("apple", "apple")

        when(offersMock.calculateDiscount(stock, items)).thenReturn(applePrice)

        till.calculateDiscount(items) shouldBe applePrice
      }
    }

    "returning the total with the discount" should {
      "return the new total if discount is less than the total" in new Setup {
        val totalCost = BigDecimal("1.00")
        val discount = BigDecimal("0.75")
        till.totalWithDiscount(totalCost, discount) shouldBe BigDecimal("0.25")
      }

      "return the original total if discount is somehow greater than the total" in new Setup {
        val totalCost = BigDecimal("1.00")
        val discount = BigDecimal("1.75")
        till.totalWithDiscount(totalCost, discount) shouldBe BigDecimal("1.00")
      }
    }
  }

  class Setup {
    val applePrice = BigDecimal("0.60")
    val orangePrice = BigDecimal("0.25")
    val apples = Produce("apple", applePrice)
    val oranges = Produce("orange", orangePrice)
    val stock = Stock(Set(apples, oranges))

    val offersMock = mock[Offers]

    val till = new Till(stock, offersMock)
  }
}
