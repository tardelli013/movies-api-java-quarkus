package br.com.tardelli;

import br.com.tardelli.controller.MovieResourceTest;
import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeExampleResourceIT extends MovieResourceTest {

  // Execute the same tests but in native mode.
}