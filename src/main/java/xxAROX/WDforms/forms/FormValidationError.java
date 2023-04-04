package xxAROX.WDforms.forms;

public class FormValidationError extends RuntimeException{
    public FormValidationError(String message) {
        super(message);
    }
}
