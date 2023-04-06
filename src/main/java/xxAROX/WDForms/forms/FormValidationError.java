package xxAROX.WDForms.forms;

public class FormValidationError extends RuntimeException{
    public FormValidationError(String message) {
        super(message);
    }
}
