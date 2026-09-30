//Nathan Armstrong 2026

import com.formdev.flatlaf.*; //used to set UI look and feel
import javax.swing.*; //used for UI components
import java.awt.*; //used for UI component properties (colours etc)

/*
    This class is the entry point of the program
*/

public class ConformalMapper
{
    public static MainFrame frame;
    public static String version = "v.1.6";

    private static void setLookAndFeel()
    {
        FlatDarkLaf.setup();

        try
        {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UIManager.put("TabbedPane.showTabSeparators", true);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2,2,2,2));
            UIManager.put("defaultFont", new Font("Segoe", Font.PLAIN, 14));
            UIManager.put("MenuBar.selectionArc", 8);
            UIManager.put("MenuItem.selectionArc", 8);
            UIManager.put("MenuBar.selectionEmbeddedInsets", new Insets(5, 2, 5, 2));
            UIManager.put("MenuItem.selectionInsets", new Insets(1, 3, 1, 3));
            UIManager.put("Slider.thumbSize", new Dimension(4, 8));
            UIManager.put("Slider.thumbArc;", 60);
            UIManager.put("Component.arc", 10 );
            UIManager.put("Button.arc", 10);
            UIManager.put("ProgressBar.arc", 999);
            UIManager.put("ComboBox.buttonSeparatorWidth", 10);
            UIManager.put("CheckBox.icon.selectedBackground", new Color(255,255,255));
            UIManager.put("CheckBox.icon.checkmarkColor", new Color(60, 63, 65));
            UIManager.put("CheckBox.icon.borderColor",  new Color(60, 63, 65));
            UIManager.put("CheckBox.icon.selectedBorderColor", new Color(255,255,255));
        }
        catch (Exception e)
        {
            System.out.println("Failed to set up look and feel: " +e);
        }
    } //set look and feel for GUI interface

    public static void main(String[] args)
    {
        FlatDarkLaf.registerCustomDefaultsSource("theme");
        setLookAndFeel();
        frame = new MainFrame();
        System.out.println(frame);
    } //create application window
}
