import org.scalatest._

/**
  * Frame class does not have any logic except for printing pinfalls
  */
class FrameTest extends FunSuite {

  // This will test a foul ball as one roll and a number 0 through 10 in the other ball for frames 1 through 10
  // Note that the test case of roll1 = 10 and roll2 = "F" is not valid though :)
  test(s"Test foul balls") {
    for (roll <- 0 to 10; frameNum <- 1 to 10) {
      val frame1 = Frame(frameNum, roll.toString, "F", 100, Some(roll.toString), Some(roll.toString))
      val frame2 = Frame(frameNum, "F", roll.toString, 100, Some(roll.toString), Some(roll.toString))
      if (roll != 10) { // non strike and spare situations
        assert(frame1.pinFalls == s"${roll}\tF")
        assert(frame2.pinFalls == s"F\t${roll}")
      } else { // strike or spare situations
        if (frameNum != 10) {
          assert(frame1.pinFalls == s" \tX")
          assert(frame2.pinFalls == s"F\t/")
        } else {
          assert(frame1.pinFalls == s"X\tX\tX") // strike in frame 10
          assert(frame2.pinFalls == s"F\t/\tX") // Spare in frame 10
        }
      }
    }
  }

  // This will test all combinations of spares and strike for frames 1 through 10
  // Like in the earlier loop, roll1 = 10 and roll2 = 0 is also not valid :)
  test(s"Test various spare and strike scenarios") {
    for (roll1 <- 0 to 10; frameNum <- 1 to 10) {
      val roll2 = 10 - roll1
      val frame1 = Frame(frameNum, roll1.toString, roll2.toString, 100, Some(roll1.toString), Some(roll1.toString))
      if (roll1 != 10 && roll2 != 10 && frameNum != 10) {
        assert(frame1.pinFalls == s"${roll1}\t/")
      }
      if (roll1 == 10) {
        if (frameNum != 10) {
          assert(frame1.pinFalls == s" \tX") // Strike in frame != 10
        } else {
          assert(frame1.pinFalls == s"X\tX\tX") // Strike in frame = 10
        }
      }
      if (roll2 == 10) {
        if (frameNum != 10) {
          assert(frame1.pinFalls == s"0\t/") // Spare in frame != 10
        } else {
          assert(frame1.pinFalls == s"0\t/\t0") // Spare in frame = 10
        }
      }
    }
  }


}
