import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class FernFractal extends JFrame {
    
    public int size = 600;
    public BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_3BYTE_BGR);
    
    public void run() {
        setTitle("Fractal Fern");
        setSize(size, size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int max = 300000;
        int x = 0;
        int y = 0;
        double v = 0;
        Point2D xy = new Point2D.Double(0, 0);
        int c = (new Color(0, 192, 0)).getRGB();
        for (int i = 0; i < max; i++) {
            v = Math.random();
            x = (int) (size * (xy.getX() / 6 + 0.5));
            y = (int) (size * (1.0 - xy.getY() / 10));
            
            if (x >= 0 && x < size && y >= 0 && y < size) {
                image.setRGB((int) x, (int) y, c);
            }
            
            if (v < .1) {
                xy.setLocation(0.0, 0.16 * xy.getY());
            } else if (v < .86) {
                xy.setLocation(0.85 * xy.getX() + 0.04 * xy.getY(), -0.04 * xy.getX() + 0.85 * xy.getY() + 1.6);
            } else if (v < .94) {
                xy.setLocation(0.2 * xy.getX() - 0.26 * xy.getY(), 0.23 * xy.getX() + 0.22 * xy.getY() + 1.6);
            } else {
                xy.setLocation(-0.15 * xy.getX() + 0.28 * xy.getY(), 0.26 * xy.getX() + 0.24 * xy.getY() + 0.44);
            }

         }
          setVisible(true);
    }
    

    public void paint(Graphics go) {
        Graphics2D g =(Graphics2D)go;
        g.clearRect(0, 0, size, size);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(image, 0, 0, null);
        
    }
    
    public static void main(String[] arg) {
        FernFractal sample = new FernFractal();
        sample.run();
        
    }
}

