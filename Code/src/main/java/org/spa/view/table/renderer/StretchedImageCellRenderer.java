package org.spa.view.table.renderer;

import org.spa.view.control.ImageViewer;
import org.spa.view.table.TableCellValue;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * A cell renderer to use for stretching images over the entire cell content
 *
 * @author Haim Adrian
 * @since 16-May-20
 */
public class StretchedImageCellRenderer extends DefaultTableCellRenderer {
   private final int margin;
   private final Border originalBorder;
   private final Border focusBorder;
   private final String adsAttributeName;

   public StretchedImageCellRenderer() {
      this(0);
   }

   public StretchedImageCellRenderer(int margin) {
      this(margin, "");
   }

   public StretchedImageCellRenderer(int margin, String adsAttributeName) {
      this.margin = margin;
      this.adsAttributeName = adsAttributeName;

      // Get the focus border of the LAF we use
      focusBorder = (Border) UIManager.get("Table.focusCellHighlightBorder");
      originalBorder = BorderFactory.createEmptyBorder();
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocused, int row, int column) {
      if (value == null) {
         return this;
      }

      Object actualValue = value;
      String ads = "";
      if (value instanceof TableCellValue) {
         actualValue = ((TableCellValue<?>) value).getValue();

         if (!adsAttributeName.isEmpty()) {
            ads = String.valueOf(((TableCellValue<?>) value).getItem().getAttributeValue(adsAttributeName));
         }
      }

      Image image = ((ImageIcon) actualValue).getImage();
      if (image == null) {
         return this;
      }

      ImageViewer imageViewer = new ImageViewer(image, margin, ads);
      // Set transparency if it is an odd row cause Nimbus L&F uses a different background color
      // for such rows. In addition, check if it is selected or focused cause it got a different background
      imageViewer.setOpaque(row % 2 == 1 || isSelected || isFocused);

      if (isSelected) {
         imageViewer.setForeground(table.getSelectionForeground());
         imageViewer.setBackground(table.getSelectionBackground());
      }

      if (isFocused) {
         imageViewer.setBorder(focusBorder);
      } else {
         imageViewer.setBorder(originalBorder);
      }

      return imageViewer;
   }
}
