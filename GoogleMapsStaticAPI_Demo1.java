package gllabs;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class GoogleMapsStaticAPI_Demo1 extends Canvas implements ImageObserver{

    public GoogleMapsStaticAPI_Demo1() throws MalformedURLException, IOException {
        map = ImageIO.read(new URL(url));
    }

    Image map;
    int size=600;
    String url = "http://maps.googleapis.com/maps/api/staticmap?"
                    + "center=Brooklyn+Bridge,New+York,NY"
                    + "&zoom=14"
                    + "&size=512x512"
                    + "&maptype=roadmap"
                    + "&markers=color:blue%7Clabel:S%7C40.702147,-74.015794"
                    + "&markers=color:green%7Clabel:G%7C40.711614,-74.012318"
                    + "&markers=color:red%7Ccolor:red%7Clabel:C%7C40.718217,-73.998284"
                    + "&sensor=false";
    
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0,size, size);
        g.drawImage(map, 10, 10, this);
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        if((infoflags&ImageObserver.ALLBITS)!=0){
            repaint();
            return true;
        }
        return false;
    }
    
    public static void main(String[] arg) throws MalformedURLException, IOException{
        JFrame frame = new JFrame();
        frame.setTitle("Google Static Map API - Demo 1");
        frame.setSize(550,570);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(new GoogleMapsStaticAPI_Demo1());
        frame.setVisible(true);
    }
}

