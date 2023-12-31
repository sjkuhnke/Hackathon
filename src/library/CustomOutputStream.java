package library;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

public class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    public CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        // Append the text to the JTextArea
        textArea.append(String.valueOf((char)b));
        
        // Move the caret to the end of the JTextArea
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
