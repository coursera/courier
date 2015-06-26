import org.coursera.courier.templates.DataTemplates
import org.example.Fortune
import org.example.Wrapper

object ExampleApp extends App {
  println(DataTemplates.writeDataMap(Wrapper(Fortune(message = "Today is your lucky day!")).data()))
}
