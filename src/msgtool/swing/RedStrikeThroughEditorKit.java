// File: RedStrikeThroughEditorKit.java - last edit:
// Yoshiki Shibata 5-Oct-2026

// Copyright (c) 2026 by Yoshiki Shibata
// All rights reserved.

package msgtool.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.AbstractDocument;
import javax.swing.text.Element;
import javax.swing.text.GlyphView;
import javax.swing.text.LabelView;
import javax.swing.text.Segment;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * StyledEditorKit which always paints strike-through lines in red,
 * regardless of the foreground color of the text.
 */
@SuppressWarnings("serial")
class RedStrikeThroughEditorKit extends StyledEditorKit {

    private static final Color STRIKE_THROUGH_COLOR = Color.red;

    public ViewFactory getViewFactory() {
        if (fViewFactory == null) {
            final ViewFactory defaultFactory = super.getViewFactory();
            fViewFactory = new ViewFactory() {
                public View create(Element elem) {
                    if (AbstractDocument.ContentElementName.equals(elem.getName()))
                        return new RedStrikeThroughLabelView(elem);
                    return defaultFactory.create(elem);
                }
            };
        }
        return fViewFactory;
    }

    private ViewFactory fViewFactory = null;

    private static class RedStrikeThroughLabelView extends LabelView {

        public RedStrikeThroughLabelView(Element elem) {
            super(elem);
        }

        public boolean isStrikeThrough() {
            // Suppress the default strike-through while painting,
            // which is painted with the foreground color.
            return !fSuppressStrikeThrough && super.isStrikeThrough();
        }

        public void paint(Graphics g, Shape a) {
            boolean strikeThrough = super.isStrikeThrough();

            fSuppressStrikeThrough = true;
            try {
                super.paint(g, a);
            } finally {
                fSuppressStrikeThrough = false;
            }

            if (strikeThrough)
                paintStrikeThrough(g, a);
        }

        // Calculates the line position in the same way as GlyphView.
        private void paintStrikeThrough(Graphics g, Shape a) {
            GlyphView.GlyphPainter painter = getGlyphPainter();
            if (painter == null)
                return;

            int p0 = getStartOffset();
            int p1 = getEndOffset();
            View parent = getParent();
            if ((parent != null) && (parent.getEndOffset() == p1)) {
                // strip whitespace on end
                Segment s = getText(p0, p1);
                while ((s.count > 0) && Character.isWhitespace(s.last())) {
                    p1 -= 1;
                    s.count -= 1;
                }
            }
            if (p1 <= p0)
                return;

            Rectangle alloc = (a instanceof Rectangle) ? (Rectangle) a : a.getBounds();
            int x0 = alloc.x;
            int x1 = x0 + (int) painter.getSpan(this, p0, p1, getTabExpander(), x0);
            int y = alloc.y + (int) (painter.getHeight(this) - painter.getDescent(this));
            y -= (int) (painter.getAscent(this) * 0.3f);

            Color savedColor = g.getColor();
            g.setColor(STRIKE_THROUGH_COLOR);
            g.drawLine(x0, y, x1, y);
            g.setColor(savedColor);
        }

        private boolean fSuppressStrikeThrough = false;
    }
}

// LOG
// 2.62 :  5-Oct-26 Y.Shibata	created
