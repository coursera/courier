import org.coursera.courier.AndroidGenerator;
import org.coursera.courier.api.DefaultGeneratorRunner;
import org.coursera.courier.api.GeneratorRunnerOptions;

// TODO: delete this and add proper tests
public class HelloWorld {
  public static void main(String[] args) throws Throwable {
    new DefaultGeneratorRunner().run(
      new AndroidGenerator(),
      new GeneratorRunnerOptions(
        "/Users/jbetz/base/coursera/courier/android/src/test/mainGeneratedPegasus",
        new String[] {
            "/Users/jbetz/base/coursera/courier/android/src/test/pegasus/org/coursera/records/WithPrimitives.pdsc"
        },
        "/Users/jbetz/base/coursera/courier/android/src/test/pegasus"));
  }
}
