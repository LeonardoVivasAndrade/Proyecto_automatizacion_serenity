package co.com.automatizacion.exceptions;

public class ExcRuntime extends RuntimeException {
  public ExcRuntime() {
  }

  public ExcRuntime(String message) {
    super(message);
  }

  public ExcRuntime(String message, Throwable cause) {
    super(message, cause);
  }
}
