package goshen.projects.text_editor.presentation.swing;

import lombok.AllArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@AllArgsConstructor
class HoverEffect extends MouseAdapter {
    private final Color hoverColor;
    private final Color normalColor;
    private final Runnable onClick;

    @Override
    public void mouseEntered(MouseEvent e) {
        ((JLabel) e.getSource()).setForeground(hoverColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        ((JLabel) e.getSource()).setForeground(normalColor);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (onClick != null) {
            onClick.run();
        }
    }
}
