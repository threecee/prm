package utils


object Timer {

  def ptime[F](name:String, f: => F) = {
    var t0 = System.nanoTime
    val ans = f
    printf(name + ": elapsed: %.3f s\n",1e-9*(System.nanoTime-t0))
    ans
  }


}
