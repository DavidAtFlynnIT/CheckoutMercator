package checkout.produce

final case class Stock(stock: Set[Produce]) {
  /**
   * Check if an item is stocked
   *
   * @param name the item name
   * @return true if stocked, false if not
   */
  def isStocked(name: String): Boolean = stock.exists(_.name == name)

  /**
   * Get the price of an item
   *
   * @param name the item name
   * @return Some(price) if found, None if not found
   */
  def getPrice(name: String): Option[BigDecimal] = stock.find(_.name == name).map(_.price)
}
