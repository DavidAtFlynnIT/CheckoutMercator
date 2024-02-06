package checkout.produce

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class StockSpec extends AnyWordSpec with Matchers {
  "Stock" when {
    "checked for an item that is stocked" should {
      "return true if stocked" in new Setup {
        stock.isStocked("apple") shouldBe true
        stock.isStocked("orange") shouldBe true
      }

      "return false if not stocked or not produce" in new Setup {
        stock.isStocked("pear") shouldBe false
      }
    }

    "checked for a price" should {
      "return Some(price) if found" in new Setup {
        stock.getPrice("apple") shouldBe Some(applePrice)
      }

      "return None if not found" in new Setup {
        stock.getPrice("pear") shouldBe None
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
