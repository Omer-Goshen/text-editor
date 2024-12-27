package goshen.projects.text_editor.presentation.swing;

import goshen.projects.text_editor.AutoCompletionSuggester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SuggestionPopup {
    private final AutoCompletionSuggester autoCompleter;
    private JPopupMenu suggestionPopup;
    private JList<String> suggestionList;
    private Timer hidePopupTimer;

    public JPopupMenu createSuggestionPopup(JTextArea textArea) {
        suggestionPopup = new JPopupMenu();
        suggestionList = new JList<>();
        initializeSuggestionList();
        suggestionPopup.add(new JScrollPane(suggestionList));

        suggestionList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handlePopupKeyPressed(e, textArea);
            }
        });

        suggestionList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                insertSuggestion(textArea);
            }
        });

        suggestionPopup.setFocusable(false);
        return suggestionPopup;
    }

    public void handleKeyTyped(KeyEvent e, JTextArea textArea) {
        if (!suggestionPopup.isVisible()) {
            updatePopupWithSuggestions(textArea);
        } else {
            resetHidePopupTimer();
        }
    }

    public void handleKeyPressed(KeyEvent e, JTextArea textArea) {
        if (suggestionPopup.isVisible()) {
            var selectedIndex = suggestionList.getSelectedIndex();
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    moveSelectionDown(selectedIndex);
                    break;
                case KeyEvent.VK_UP:
                    moveSelectionUp(selectedIndex);
                    break;
                case KeyEvent.VK_ENTER, KeyEvent.VK_TAB:
                    insertSuggestion(textArea);
                    break;
            }
        }
    }

    private void updatePopupWithSuggestions(JTextArea textArea) {
        var typedText = textArea.getText().substring(0, textArea.getCaretPosition());
        var prefix = extractPrefix(typedText);

        if (!prefix.isEmpty()) {
            var suggestions = autoCompleter.getSuggestions(prefix);
            if (!suggestions.isEmpty()) {
                updateSuggestionPopup(suggestions, textArea);
            }
        } else {
            suggestionPopup.setVisible(false);
        }
    }

    private void insertSuggestion(JTextArea textArea) {
        var selectedSuggestion = suggestionList.getSelectedValue();
        if (selectedSuggestion != null) {
            var typedText = textArea.getText().substring(0, textArea.getCaretPosition());
            var prefix = extractPrefix(typedText);
            textArea.replaceRange(selectedSuggestion, textArea.getCaretPosition() - prefix.length(), textArea.getCaretPosition());
            suggestionPopup.setVisible(false);
        }
    }

    private String extractPrefix(String text) {
        var words = text.split("\\W+");
        return words.length > 0 ? words[words.length - 1] : "";
    }

    private void updateSuggestionPopup(List<String> suggestions, JTextArea textArea) {
        suggestionList.setListData(suggestions.toArray(new String[0]));

        if (!suggestionPopup.isVisible()) {
            showPopupBelowCaret(textArea);
        }

        suggestionList.setSelectedIndex(0);
    }

    private void showPopupBelowCaret(JTextArea textArea) {
        var caretPosition = textArea.getCaret().getMagicCaretPosition();
        if (caretPosition != null) {
            var x = caretPosition.x;
            var y = caretPosition.y + textArea.getFont().getSize();

            if (y + suggestionList.getPreferredSize().height > textArea.getHeight()) {
                y = caretPosition.y - suggestionList.getPreferredSize().height;
            }

            suggestionPopup.show(textArea, x, y);
        }
    }

    private void moveSelectionDown(int selectedIndex) {
        if (selectedIndex < suggestionList.getModel().getSize() - 1) {
            suggestionList.setSelectedIndex(selectedIndex + 1);
        }
    }

    private void moveSelectionUp(int selectedIndex) {
        if (selectedIndex > 0) {
            suggestionList.setSelectedIndex(selectedIndex - 1);
        }
    }

    private void handlePopupKeyPressed(KeyEvent e, JTextArea textArea) {
        var selectedIndex = suggestionList.getSelectedIndex();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                moveSelectionDown(selectedIndex);
                break;
            case KeyEvent.VK_UP:
                moveSelectionUp(selectedIndex);
                break;
            case KeyEvent.VK_ENTER, KeyEvent.VK_TAB:
                insertSuggestion(textArea);
                break;
        }
    }

    private void resetHidePopupTimer() {
        if (hidePopupTimer != null && hidePopupTimer.isRunning()) {
            hidePopupTimer.stop();
        }

        hidePopupTimer = new Timer(10, e -> suggestionPopup.setVisible(false));
        hidePopupTimer.setRepeats(false);
        hidePopupTimer.start();
    }

    private void initializeSuggestionList() {
        suggestionList.setBackground(Color.decode("#1E1E1E"));
        suggestionList.setForeground(Color.decode("#D4D4D4"));
        suggestionList.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suggestionList.setFocusable(false);
    }
}
