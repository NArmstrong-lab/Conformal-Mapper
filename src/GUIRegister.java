//Nathan Armstrong 2023, updated 2026

import javax.swing.*; //used for creating windows and runtime elements
import javax.swing.text.NumberFormatter;

import java.awt.*; //used for GUI elemts
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/*
 * This class is used to set the properties
 * of GUI elements that the program uses. This
 * is done because all buttons, textboxes etc., have
 * the same style which creates clutter in the code when
 * setting their properties and makes it harder to read.
 */

public class GUIRegister
{
    public GUIRegister()
    {

    } //empty constructor

    public JButton button(JButton button, String label, int x, int y, int width, int height)
    {
        button.setBounds(x, y, width, height); //x-coordinate, y-coordinate, width, height of button
        button.setPreferredSize(new Dimension(width, height));
        button.setHorizontalTextPosition(JButton.CENTER); //sets the horizontal position of the button text
        button.setVerticalTextPosition(JButton.CENTER); //sets the verticle position of the button text
        button.setText(label); //gives a lable to the button that is displayed
        button.setEnabled(true); //enables the button
            return button;
    } //sets the properties of a button, including its size, postion, label etc.

    public JButton button(JButton button, String label, int width, int height)
    {
        button.setPreferredSize(new Dimension(width, height));
        button.setHorizontalTextPosition(JButton.CENTER); //sets the horizontal position of the button text
        button.setVerticalTextPosition(JButton.CENTER); //sets the verticle position of the button text
        button.setText(label); //gives a lable to the button that is displayed
        button.setEnabled(true); //enables the button
            return button;
    } //sets the properties of a button, including its size, postion, label etc.

    public JButton button(JButton button, String label, int size)
    {
        button.setPreferredSize(new Dimension(size, size));
        button.setHorizontalTextPosition(JButton.CENTER); //sets the horizontal position of the button text
        button.setVerticalTextPosition(JButton.CENTER); //sets the verticle position of the button text
        button.setText(label); //gives a lable to the button that is displayed
        button.setEnabled(true); //enables the button
            return button;
    } //sets the properties of a button, including its size, postion, label etc.

    public JTextField textBox(JTextField textBox, int x, int y, int width, int height)
    {
        textBox.setBounds(x, y, width, height); //x-coordinate, y-coordinate, width, height of textfield
        textBox.setPreferredSize(new Dimension(width, height)); //sets the preferred size of the textfield
            return textBox;
    } //sets the properties of a textbox, including it's size and postion etc.

    public JLabel label(JLabel label, String text, int x, int y, int width, int height)
    {
        label.setText(text); //sets the text of the label
        label.setBounds(x, y, width, height); //x-coordinate, y-coordinate, width, height of button
            return label;
    } //sets the properties of a label, including it's size, postion, text etc.

    public JComboBox<String> comboBox(JComboBox<String> comboBox, int x, int y, int width, int height)
    {
        comboBox.setBounds(x, y, width, height); //x-coordinate, y-coordinate, width, height of button
        comboBox.setFocusable(false); //prevents the comBox from being focusable (removes a selection border when clicked)
            return comboBox;
    } //sets the properties of a combo box

    public JSlider slider(JSlider slider, int min, int max, int startingValue, int x, int y, int width, int height)
    {
        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.setValue(startingValue);
        slider.setBounds(x, y, width, height);
        slider.setPreferredSize(new Dimension(width, height));
        slider.setFocusable(false);
            return slider;
    }

    public JCheckBox checkBox(JCheckBox checkbox, String label, int x, int y, int width, int height)
    {
        checkbox.setText(label);
        checkbox.setBounds(x, y, width, height);
        checkbox.setFocusable(false);
            return checkbox;
    }

    public JSpinner spinner(JSpinner spinner, int x, int y, int width, int height)
    {
        spinner.setBounds(x, y, width, height);
        spinner.setFocusable(false);
        spinner.setModel(new SpinnerNumberModel(0.0, null, null, 0.1));
        
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');

        DecimalFormat format = new DecimalFormat("#0.###########", symbols);

        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner);
        JFormattedTextField tf = editor.getTextField();

        NumberFormatter formatter = (NumberFormatter) tf.getFormatter();

        formatter.setFormat(format);

        spinner.setEditor(editor);
            return spinner;
    }
}