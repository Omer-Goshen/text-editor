package goshen.projects.text_editor.presentation.swing;

import com.formdev.flatlaf.FlatDarkLaf;
import goshen.projects.text_editor.presentation.PresentationComponent;
import lombok.RequiredArgsConstructor;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@Component
@RequiredArgsConstructor
public class SwingPresentation implements PresentationComponent {
    private final SuggestionPopup suggestionPopup;

    @Override
    public void present() {
        SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setup();

            var frame = createFrame();
            var textArea = createTextArea();
            var scrollPane = createScrollPane(textArea);

            frame.getContentPane().add(createHeaderPanel(frame), BorderLayout.NORTH);
            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
            frame.setVisible(true);

            suggestionPopup.createSuggestionPopup(textArea);

            textArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    suggestionPopup.handleKeyTyped(e, textArea);
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    suggestionPopup.handleKeyPressed(e, textArea);
                }
            });
        });
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.setShape(new java.awt.geom.RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 20, 20));
        frame.getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.getContentPane().setBackground(new Color(30, 30, 30));

        return frame;
    }

    private JTextArea createTextArea() {
        var textArea = new RSyntaxTextArea();

        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setTabSize(2);

        Color lineHighlightColor = Color.decode("#2D2D2D"); // Subtle gray for line highlight
        textArea.setForeground(Color.decode("#D4D4D4")); // Light gray text
        textArea.setBackground(Color.decode("#1E1E1E")); // Dark background
        textArea.setCurrentLineHighlightColor(lineHighlightColor);
        textArea.setCaretColor(Color.WHITE); // White caret for contrast

        textArea.setHighlightCurrentLine(false);
        textArea.setPaintTabLines(false);
        textArea.setDoubleBuffered(true);
        textArea.getCaret().setBlinkRate(500);

        RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setBackground(Color.decode("#1E1E1E"));

        return textArea;
    }

    private JScrollPane createScrollPane(JTextArea textArea) {
        var scrollPane = new RTextScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setBackground(Color.decode("#1E1E1E"));

        return scrollPane;
    }

    private JPanel createHeaderPanel(JFrame frame) {
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.setBackground(new Color(45, 45, 45));
        header.setPreferredSize(new Dimension(frame.getWidth(), 30));

        JLabel title = new JLabel("Modern Text Editor");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setBorder(new EmptyBorder(0, 10, 0, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        buttonPanel.setOpaque(false);

        JLabel minimize = new JLabel("—");
        minimize.setFont(new Font("SansSerif", Font.BOLD, 14));
        minimize.setForeground(Color.WHITE);
        minimize.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        minimize.addMouseListener(new HoverEffect(
                Color.LIGHT_GRAY, Color.WHITE,
                () -> frame.setState(JFrame.ICONIFIED)
        ));

        JLabel close = new JLabel("X");
        close.setFont(new Font("SansSerif", Font.BOLD, 14));
        close.setForeground(Color.WHITE);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.addMouseListener(new HoverEffect(
                Color.RED, Color.WHITE,
                frame::dispose
        ));

        buttonPanel.add(minimize);
        buttonPanel.add(close);

        header.add(title, BorderLayout.WEST);
        header.add(buttonPanel, BorderLayout.EAST);

        DragWindowListener dragListener = new DragWindowListener(frame);
        header.addMouseListener(dragListener);
        header.addMouseMotionListener(dragListener);

        return header;
    }
}
