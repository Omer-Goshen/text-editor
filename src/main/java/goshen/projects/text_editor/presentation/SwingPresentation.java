package goshen.projects.text_editor.presentation;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@Component
public class SwingPresentation implements PresentationComponent {
    @Override
    public void present() {
        SwingUtilities.invokeLater(() -> {
            var frame = createFrame();
            var textArea = createTextArea();
            var scrollPane = createScrollPane(textArea);

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame("Real-Time Text Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.getContentPane().setBackground(new Color(43, 43, 43)); // Set dark theme
        return frame;
    }

    private JTextArea createTextArea() {
        var textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Monospaced font for code
        textArea.setForeground(Color.WHITE);
        textArea.setCurrentLineHighlightColor(Color.DARK_GRAY);
        textArea.setCaretColor(Color.WHITE); // White caret (cursor)
        textArea.setBackground(new Color(30, 30, 30)); // Dark background
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
                System.out.println("Key pressed: " + keyChar);
            }
        });

        return textArea;
    }

    private JScrollPane createScrollPane(JTextArea textArea) {
        var scrollPane = new RTextScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }
}
