package checkout.offers

import checkout.produce.Stock

import scala.annotation.tailrec

case class Offers(offers: Set[Offer]) {
  /**
   * calculate the discount for the scanned items
   *
   * @param stock the stock details
   * @param items the items scanned by the till
   * @return the discount to be applied
   */
  def calculateDiscount(stock: Stock, items: Seq[String]): BigDecimal = {
    val itemsAndQty = items.groupBy(identity).map { case (k, v) => k -> v.size }

    itemsAndQty.foldLeft(BigDecimal(0)) { case (discount, (produceName, qtyScanned)) =>
      (for {
        offer <- offers.find(_.produceName == produceName)
        price <- stock.getPrice(produceName)
      } yield discount + calculateDiscountForProduce(offer, price, qtyScanned)).getOrElse(discount)
    }
  }

  private def calculateDiscountForProduce(offer: Offer, price: BigDecimal, qtyScanned: Int): BigDecimal = {
    @tailrec
    def calculateDiscountForProduceAccumulator(qtyScannedLeft: Int, discountTotal: BigDecimal): BigDecimal =
      if (qtyScannedLeft < offer.qualifyingQuantity)
        discountTotal
      else
        calculateDiscountForProduceAccumulator(qtyScannedLeft - offer.qualifyingQuantity, discountTotal + price * offer.numFree)

    calculateDiscountForProduceAccumulator(qtyScanned, BigDecimal(0))
  }
}
