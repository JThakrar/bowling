
import scala.collection.mutable

/**
  * Player is just a mechanism to print the details for a player - viz. name, pinfalls and score.
  * @param name
  * @param frames
  */
case class Player(name: String, frames: Seq[Frame]) {

  val TAB = FileParser.TAB
  override def toString: String = {
    val output = new mutable.StringBuilder()
    output.append(s"${name}\n")
    output.append(s"PinFalls${TAB}${frames.map(f => f.pinFalls).mkString(TAB)}\n")
    output.append(s"Score${TAB}${TAB}${frames.map(f => f.cumulativeScore).mkString(TAB+TAB)}")
    output.toString
  }

}
