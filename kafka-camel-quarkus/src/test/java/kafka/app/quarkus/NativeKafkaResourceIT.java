package kafka.app.quarkus;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeKafkaResourceIT extends KafkaResourceTest {

    // Execute the same tests but in native mode.
}