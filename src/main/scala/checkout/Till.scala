package checkout

import checkout.offers.Offers
import checkout.produce.Stock

import cats.implicits._

class Till(stock: Stock, offers: Offers) {
  /**
   * total the prices of the scanned items
   *
   * @param stock the stock details
   * @param items the items scanned by the till
   * @return Some(total) of the items' prices, or None if any item cannot be found
   */
  def total(items: Seq[String]): Option[BigDecimal] =
    items.traverse(stock.getPrice).map(_.sum)

  /**
   * calculate the total discount for the scanned items
   *
   * @param items the items scanned by the till
   * @return the total discount to be applied
   */
  def calculateDiscount(items: Seq[String]): BigDecimal =
    offers.calculateDiscount(stock, items)

  /**
   * return the total with discount applied. If the discount is greater than the total cost
   * return the original cost and output an error
   *
   * @param totalCost the total cost before discount
   * @param discount the discount to be applied
   * @return the amount with the discount applied
   */
  def totalWithDiscount(totalCost: BigDecimal, discount: BigDecimal): BigDecimal =
    if (discount > totalCost) {
      // This would be logged/alerted in a real application since it shouldn't happen
      println("Somehow the discount exceeds the total cost!")
      totalCost
    } else
      totalCost - discount
}
