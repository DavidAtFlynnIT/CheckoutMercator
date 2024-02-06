package checkout

import checkout.produce.Stock

import cats.implicits._

object Till {
  /**
   * total the prices of the scanned items
   *
   * @param stock the stock details
   * @param items the items scanned by the till
   * @return Some(total) of the items' prices, or None if any item cannot be found
   */
  def total(stock: Stock, items: Seq[String]): Option[BigDecimal] =
    items.traverse(stock.getPrice).map(_.sum)
}
