import javax.swing.*;
import java.awt.*;
import java.net.*;

/*
    This class is used to build the splash screen window
    that is displayed while the MainFrame object is
    being constructed when the program is started.
 */

public class SplashWindow extends JFrame
{
    private URL iconUrl = MainFrame.class.getResource("/resources/icon.png");
    public SplashWindow()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        this.setSize(256, 300);
        this.setUndecorated(true);
		this.setBackground(new Color(0, 0 ,0 ,0));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);
        this.setIconImage(new ImageIcon(iconUrl).getImage());
        this.setVisible(true);
    } //set window settings

    @Override
    public void paint(Graphics g)
    {
        super.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); 
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2d.drawImage(new ImageIcon(new ImageIcon(iconUrl).getImage().getScaledInstance(256, 256, Image.SCALE_SMOOTH)).getImage(), 0, 0, this);
        g2d.setColor(Color.white);
        g2d.drawString("Created By Nathan Armstrong 2026 - "+ConformalMapper.version, 15, 276);
        g2d.drawString("Loading...", 100, 290);
    } //window rendering
}