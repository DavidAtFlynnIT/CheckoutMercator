package checkout.offers

import checkout.produce.{Produce, Stock}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class OffersSpec extends AnyWordSpec with Matchers {
  "Offers" when {
    "calculating discount" should {
      "return 0 if no items were scanned" in new Setup {
        offers.calculateDiscount(stock, Seq.empty) shouldBe BigDecimal(0)
      }

      "return 0 if not enough apples or oranges were scanned to qualify" in new Setup {
        val items = Seq("apple", "orange", "oranges")
        offers.calculateDiscount(stock, items) shouldBe BigDecimal(0)
      }

      "return 0 if some other items were scanned" in new Setup {
        val items = Seq("pear", "banana")
        offers.calculateDiscount(stock, items) shouldBe BigDecimal(0)
      }

      "return 0 if one apple was scanned" in new Setup {
        val items = Seq("apple")
        offers.calculateDiscount(stock, items) shouldBe BigDecimal(0)
      }

      "return the apple price if two apples were scanned" in new Setup {
        val items = Seq("apple", "apple")
        offers.calculateDiscount(stock, items) shouldBe applePrice
      }

      "return the apple price if three apples were scanned" in new Setup {
        val items = Seq("apple", "apple", "apple")
        offers.calculateDiscount(stock, items) shouldBe applePrice
      }

      "return twice the apple price if four apples were scanned" in new Setup {
        val items = Seq("apple", "apple", "apple", "apple")
        offers.calculateDiscount(stock, items) shouldBe applePrice * 2
      }

      "return 0 if one orange was scanned" in new Setup {
        val items = Seq("orange")
        offers.calculateDiscount(stock, items) shouldBe BigDecimal(0)
      }

      "return 0 if two oranges were scanned" in new Setup {
        val items = Seq("orange", "orange")
        offers.calculateDiscount(stock, items) shouldBe BigDecimal(0)
      }

      "return the orange price if three oranges were scanned" in new Setup {
        val items = Seq("orange", "orange", "orange")
        offers.calculateDiscount(stock, items) shouldBe orangePrice
      }

      "return the apple price if two apples and two oranges were scanned" in new Setup {
        val items = Seq("apple", "apple", "orange", "orange")
        offers.calculateDiscount(stock, items) shouldBe applePrice
      }

      "return the orange price if one apple and three oranges were scanned" in new Setup {
        val items = Seq("apple", "orange", "orange", "orange")
        offers.calculateDiscount(stock, items) shouldBe orangePrice
      }

      "return the apple price plus the orange price if two apples and three oranges were scanned but not grouped" in new Setup {
        val items = Seq("orange", "apple", "orange", "apple", "orange")
        offers.calculateDiscount(stock, items) shouldBe applePrice + orangePrice
      }

      "return 0 if no offers were available" in new Setup {
        val items = Seq("apple", "apple")
        noOffers.calculateDiscount(stock, items) shouldBe BigDecimal(0)
      }

      "return the apple price if two apples and three oranges were scanned but the oranges weren't on offer" in new Setup {
        val items = Seq("apple", "apple", "orange", "orange", "orange")
        justAppleOffer.calculateDiscount(stock, items) shouldBe applePrice
      }

      "return the apple price plus the orange price if two apples and three oranges (and somehow a pear) were scanned" in new Setup {
        val items = Seq("apple", "apple", "orange", "orange", "pear", "orange")
        offers.calculateDiscount(stock, items) shouldBe applePrice + orangePrice
      }
    }
  }

  class Setup {
    val applePrice = BigDecimal("0.60")
    val orangePrice = BigDecimal("0.25")
    val apples = Produce("apple", applePrice)
    val oranges = Produce("orange", orangePrice)
    val stock = Stock(Set(apples, oranges))

    val appleOffer = Offer("apple", 2, 1)
    val orangeOffer = Offer("orange", 3, 1)
    val offers = Offers(Set(appleOffer, orangeOffer))
    val justAppleOffer = Offers(Set(appleOffer))
    val noOffers = Offers(Set.empty)
  }
}
