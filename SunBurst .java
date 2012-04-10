import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Hashtable;
import javax.swing.JFrame;

public class SunBurst extends JFrame {

    public Image map;
    public int width = 500, height = 500;
    public Color blue = new Color(173, 216, 230);
    public Color gray = new Color(169, 169, 169);
    public Color pink = new Color(255, 183, 193);
    public Color str = new Color(121, 151, 161);
    public Color[] colors = {pink, gray, blue};
    private float dx1 = 1300, dx0 = 0;
    private float r0 = 0, r1 = width;
    public String[] ticks={
        "Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"
    };
    public int[][] data = {
        {230, 460, 440},
        {180, 520, 1270},
        {155, 350, 935},
        {195, 195, 560},
        {180, 155, 550},
        {330, 130, 650},
        {260, 130, 430},
        {290, 110, 490},
        {355, 100, 290},
        {135, 95, 245},
        {100, 140, 325},
        {40, 120, 215}
    };

    public int t(int v) {
        float sunit = (r1 - r0) + r0;
        float dunit = (v - dx0) / (dx1 - dx0);
        System.out.println("sunit " + sunit + "dunit " + dunit);
        int l = (int) (sunit * dunit);
        System.out.println("v" + v + "to" + l);
        return l;
    }

    public void wedge(Graphics2D g,int cx,int cy,int r,int startangle,int endangle){
        int top = cx-r;
        int left= cy-r;
         g.fillArc(cx-r, cy-r, r*2, r*2, startangle, endangle);
    }
    public SunBurst() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        this.setTitle("Sunburst");
        this.setVisible(true);
        this.setResizable(false);
        
        map = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        renderChart();
    }

    public void renderChart() {
        Graphics2D g = (Graphics2D) map.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.black);
        //g.translate(200, 200);
        g.setStroke(new BasicStroke(0.5f));
        /*
        g.setColor(Color.red);
        //g.drawOval(0, 0, 400, 400);
        g.fillArc(0, 0, 400, 400, 0, 10);
        
        g.setColor(Color.blue);
        g.fillArc(0, 0, 400, 400, 20, 40);
        g.fillArc(100, 100, 200, 200, 0, 10);
        g.setColor(Color.green);
        g.drawArc(0, 0, 400, 400, 0, 10);
        g.drawArc(0, 0, 400, 400, 20, 40);
        
         */
        int startangle = 0;
        int endangle = 0;
        AffineTransform tr = AffineTransform.getRotateInstance(0, width / 2, height / 2);
        for (int i = 0; i < data.length; i++) {

            Hashtable<Integer, Color> colorseries = new Hashtable<Integer, Color>();
            for (int z = 0; z < data[i].length; z++) {
                colorseries.put(data[i][z], colors[z]);
            }
            Arrays.sort(data[i]);
            startangle = endangle;
            endangle = endangle + (360 / data.length);
            System.out.println("xxxxxxxxxxxxxxxxxxxxx");
            double bigarc=0;
            for (int j = data[i].length - 1; j >= 0; j--) {
                g.setColor(colorseries.get(data[i][j]));
                double c = t(data[i][j]);
                bigarc=c >bigarc?c:bigarc;
                double co = ((width - c) / 2);
                System.out.println("start angle -" + startangle + " end angle-" + endangle + " coor-" + co + " width-" + c);
                //g.fillArc(co, co, c, c, startangle, (360 / data.length));
                //wedge(g, 200, 200, c, startangle, endangle);
                //g.setColor(str);
                double rad=c;
                Arc2D dou =new Arc2D.Double(co,co,c,c,startangle,(360/data.length),Arc2D.PIE);
                
                //stroke.createStrokedShape(dou);
               dou.setArcByCenter(width/2, width/2, rad/2, startangle,(360/data.length), Arc2D.PIE);
                g.fill(dou);
                g.setColor(str);
                 g.draw(dou);
                 
                 
                //g.drawArc(co, co, c, c, startangle, (360 / data.length));
                //tr.rotate(startangle);
                //g.drawLine(width/2, height/2, (int)tr.getTranslateX(), (int)tr.getTranslateY());
            }
            //Draw  text
            
            g.setColor(Color.BLACK);Font f = new Font("Sans Serif",Font.PLAIN,9);
            TextStroke stroke = new TextStroke(ticks[i],f,false,false );
            Arc2D textpath = new Arc2D.Double(Arc2D.OPEN);
            double midangle = (360/data.length)/2;
                 textpath.setArcByCenter(width/2, width/2, (bigarc/2)+25, startangle+(360/data.length)-midangle+1,-((360/data.length)-2), Arc2D.OPEN);
                 g.fill(stroke.createStrokedShape(textpath));
        
    }
        Font f = new Font("Sans Serif",Font.PLAIN,9);
       g.setFont(f);
        g.setColor(pink);
        g.fillRect(30,30, 10, 10);
        g.setColor(Color.black);
        g.drawString("Wounded", 30+15, 30+9);
        g.setColor(gray);
        g.fillRect(30,30+10+10, 10, 10);
        g.setColor(Color.black);
        g.drawString("Diseased", 30+15, 30+10+10+9);
        g.setColor(blue);
        g.fillRect(30,30+10+10+10+10, 10, 10);
        g.setColor(Color.black);
        g.drawString("All", 30+15, 30+10+10+10+10+9);
    }

    public void paint(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(0, 0, 900, 900);
        g.drawImage(map, 50, 50, Color.WHITE, null);
    }

    public static void main(String[] arg) {
        SunBurst s = new SunBurst();
    }
}

