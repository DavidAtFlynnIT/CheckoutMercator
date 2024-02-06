package checkout

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
    println("Please enter the list of scanned items in lowercase separated by commas without whitespace.")
    val maybeItems = getItemsFromUser(stock)

    maybeItems.fold(
      println("Invalid input found, cannot proceed.")
    ){ items =>
      Till.total(stock, items).fold(
        println("No or invalid items found, cannot proceed.") // This shouldn't happen
      )(totalCost => println(s"The total cost is £$totalCost."))
    }
  }
}
