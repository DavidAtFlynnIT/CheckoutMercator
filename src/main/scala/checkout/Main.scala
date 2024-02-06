package checkout

import checkout.offers.{Offer, Offers}
import checkout.produce.{Produce, Stock}

import scala.io.StdIn.readLine

object Main {
  private def initialiseStock(): Stock = {
    val applePrice = BigDecimal("0.60")
    val orangePrice = BigDecimal("0.25")
    val apples = Produce("apple", applePrice)
    val oranges = Produce("orange", orangePrice)
    Stock(Set(apples, oranges))
  }

  private def initialiseOffers(): Offers = {
    val appleOffer = Offer("apple", 2, 1)
    val orangeOffer = Offer("orange", 3, 1)
    Offers(Set(appleOffer, orangeOffer))
  }

  private def getItemsFromUser(stock: Stock): Option[Seq[String]] = {
    val input = readLine

    if (input.isEmpty) {
      println("No input entered.")
      None
    }
    else {
      val items = input.split(",").toSeq

      if (items.isEmpty)  {
        println("No items entered.")
        None
      } else {
        if (checkItemsAreValid(stock, items))
          Some(items)
        else
          None
      }
    }
  }

  private def checkItemsAreValid(stock: Stock, items: Seq[String]) = {
    items.forall(item =>
      if (!stock.isStocked(item)) {
        println(s"Invalid item '$item' found.")
        false
      } else
        true
    )
  }

  def main(args: Array[String]): Unit = {
    val stock = initialiseStock()
    val offers = initialiseOffers()

    val till = new Till(stock, offers)

    println("Please enter the list of scanned items in lowercase separated by commas without whitespace.")
    val maybeItems = getItemsFromUser(stock)

    maybeItems.fold(
      println("Invalid input found, cannot proceed.")
    ){ items =>
      till.total(items).fold(
        println("No or invalid items found, cannot proceed.") // This shouldn't happen
      ){totalCost =>
        val discount = till.calculateDiscount(items)
        val totalWithDiscount = till.totalWithDiscount(totalCost, discount)

        println(s"The amount to pay is £$totalWithDiscount (total cost £$totalCost with £$discount discount).")
      }
    }
  }
}
