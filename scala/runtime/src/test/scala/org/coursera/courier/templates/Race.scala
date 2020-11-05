package org.coursera.courier.templates

trait Race {
  private def prepareThread(name:String, task: => Unit) = {
    val thread = new Thread(name) {
      override def run() = {
        task
      }
    }
    thread.setDaemon(true)
    thread
  }

  private def endThread(thread:Thread):Boolean = {
    thread.join(1000)
    val alive = thread.isAlive
    if (alive) thread.interrupt()
    alive
  }

  def race(a: => Unit, b: => Unit): Boolean = {
    val aThread = prepareThread("A", a )
    val bThread = prepareThread("B", b )
    aThread.start()
    bThread.start()
    // check for deadlock.
    aThread.join(1000)
    bThread.join(1000)
    val aAlive = endThread(aThread)
    val bAlive = endThread(bThread)
    !(aAlive || bAlive)
  }
}
