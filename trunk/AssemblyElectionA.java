import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AssemblyElectionA extends JFrame {

    public static int width = 3000;
    public static int height = 3000;
    public int maxwidth = 0;
    FontMetrics fm;
    public BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

    public AssemblyElectionA() {
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setState(JFrame.MAXIMIZED_BOTH);
        this.add(new JScrollPane(new Panel()));
        this.pack();
        this.setVisible(true);
    }

    class Panel extends JPanel {

        public Dimension getPreferredSize() {
            return new Dimension(width + 100, height + 100);
        }

        public void paint(Graphics g) {
            g.setColor(Color.gray);
            g.fillRect(0, 0, width + 100, height + 100);
            g.drawImage(image, 50, 50, null);
        }
    }

    public void draw() {
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(253, 253, 253));
        g.fillRect(0, 0, width, height);
        AffineTransform t;
        t = g.getTransform();
        drawArea(g);
        g.setTransform(t);
        drawDistrictPie(g);
        g.setTransform(t);
        drawParty(g);
        g.setTransform(t);
        drawCandidate(g);
        g.setTransform(t);
        drawMapping(g);
        g.setTransform(t);
        g.setColor(Color.BLACK);
        g.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 35));
        g.drawString("The Assembly Election (2011) - TamilNadu", 500, 300);
        g.setTransform(t);
        drawLegend(g);
       

    }

    public void drawLegend(Graphics2D g){
        g.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 12));
        g.translate(500, width-300);
        RedHue red = new RedHue();
        int x=0 ,y=0,width=60;
        for(int i=ElectionData.parties.length-1;i>=0;i--){
            g.setColor(red.next());
            g.fillRect(x, 0, width, 30);
            g.setColor(Color.black);
            g.drawRect(x, 0, width, 30);
            System.out.println("x "+x+" x1 "+(x+width));
            
            g.drawString(ElectionData.parties[i], x, 60);
            x+=width;
            
        }
    }
    public double getCandIndex(int areaIndex){
        String area = ElectionData.locations[areaIndex];
        String cand = ElectionData.hashtable.get(area);
        for(int i=0;i<ElectionData.cand.length;i++){
            if(ElectionData.cand[i].equals(cand)){
                return i;
            }
        }
        return 0;
    }
    public void drawMapping(Graphics2D g) {
       
        g.setStroke(new BasicStroke(0.5f));
        g.setColor(Color.gray);
        for(int i=0;i<ElectionData.locations.length;i++){
            double candI = getCandIndex(i);
            double areaI = i;
            AffineTransform org = g.getTransform();
            //set up can
            Point2D candP = new Point2D.Double();
            AffineTransform candT = new AffineTransform();
            double inc = Math.PI / ElectionData.locations.length;
            double d = 0;
            candT.translate(width / 2 + 100, height / 2);
            d += candI * inc;
            candT.rotate(-d);
            candT.translate(0, 880);
            candT.rotate(-Math.PI / 2);
            candT.scale(-1, -1);
            candT.transform(new Point2D.Double(0, 0), candP);
             //g.setTransform(org);
            //set up area
            Point2D areaP = new Point2D.Double();
            AffineTransform areaT = new AffineTransform();
            double inc2 = Math.PI / ElectionData.locations.length;
            double d2 = 0;
            areaT.translate(width / 2, height / 2);
            d2 += areaI * inc2;
            areaT.rotate(d2);
            areaT.translate(0, 880);
            areaT.rotate(Math.PI / 2);
            areaT.scale(-1, -1);
            areaT.transform(new Point2D.Double(0, 0), areaP);
            System.out.println(areaP.toString());
            QuadCurve2D curve =new QuadCurve2D.Double(candP.getX(), candP.getY(), width/2+50, height/2, areaP.getX(), areaP.getY());
            Line2D l = new Line2D.Double(candP, areaP);
            g.draw(curve);
            //g.setTransform(org);
        }
           }

    public void drawCandidate(Graphics2D g) {
        setMax(g);
        g.translate(width / 2 + 100, height / 2);
        double inc = Math.PI / ElectionData.locations.length;
        g.setColor(Color.black);
        g.setStroke(new BasicStroke(1f));
        Font f = g.getFont();
        double d = 0;
        double innerRadius = 1000 - (maxwidth + 12);
        maxwidth = fm.stringWidth("vivek") + 40;
        for (int i = ElectionData.cand.length - 1; i >= 0; i--) {
            AffineTransform t = g.getTransform();
            g.rotate(-d);
            g.translate(0, 900);
            g.rotate(-Math.PI / 2);
            g.scale(-1, -1);
            //g.scale(1,-1);
            //axis(g);
            String label = "vivek";
            int lblwidth = fm.stringWidth(label);
            int lblheight = 2;
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine((maxwidth - lblwidth) - 15, -lblheight, (maxwidth - lblwidth) - 3, -lblheight);
            g.setColor(Color.BLACK);
            g.drawString(ElectionData.cand[i], 0 + (maxwidth - lblwidth), 0);
            //g.drawOval( 0,-10,2,2);
            g.setTransform(t);
            d += inc;
        }
    }

    public void drawParty(Graphics2D g) {
        g.setColor(Color.black);
        g.translate(width / 2 + 100, height / 2);
        g.rotate(Math.PI / 2);
        Arc2D arc = new Arc2D.Double();
        double innerRadius = 1000 - (maxwidth + 12);
        //arc.setArcByCenter(0, 0, 1000 - (maxwidth + 12), 0, 180, Arc2D.OPEN);
        //g.draw(arc);
        double total = ElectionData.locations.length;
        double startangle = 0;
        g.setColor(Color.red);
        //g.drawLine(0,0,0,900);
        g.setColor(Color.green);
        g.drawLine(0, 0, 900, 0);
        RedHue rain = new RedHue();
        for (int i = 0; i < ElectionData.parties.length; i++) {
            double per = (double) ElectionData.strength[i] / total;
            double arcangle = 180 * per;
            Arc2D pie = new Arc2D.Double();
            pie.setArcByCenter(0, 0, innerRadius + 40, startangle, arcangle, Arc2D.PIE);
            g.setColor(rain.next());
            g.fill(pie);
            startangle += arcangle;
            // System.out.println("statr "+startangle+" pre "+per);
        }
        Arc2D fill = new Arc2D.Double();
        fill.setArcByCenter(0, 0, innerRadius, 0, 180, Arc2D.PIE);
        g.setColor(Color.white);
        g.fill(fill);
    }

    public void drawDistrictPie(Graphics2D g) {

        Rainbow rain = new Rainbow();
        setMax(g);
        int total = ElectionData.locations.length;
        double startAngle = 0;
        g.translate(width / 2, height / 2);
        g.rotate(-Math.PI / 2);
        for (int i = 0; i < ElectionData.distpolls.length; i++) {

            double per = (double) ElectionData.distpolls[i] / (double) total;
            per = 180 * per;
            //System.out.println("per "+per);
            double txtoffset = (i % 2) == 0 ? 0 : 10;
            Arc2D arc = new Arc2D.Double(Arc2D.PIE);
            arc.setArcByCenter(0, 0, (width / 2) * .75, startAngle, per, Arc2D.PIE);

            Arc2D txtarc = new Arc2D.Double(Arc2D.OPEN);
            txtoffset = fm.stringWidth(ElectionData.dist[i]);

            Point2D end = arc.getEndPoint();
            //txtoffset = (end.getY()/2);
            double angl =startAngle;
            if(per>10){
                angl= startAngle + ((per/2)-(ElectionData.dist[i].length()/2));
            }
            txtarc.setArcByCenter(0, 0, (width / 2) * .75 - 20, angl, per, Arc2D.OPEN);
            //txtarc=arc;
            //g.setColor(Color.red);
            //g.draw(arc);

            g.setColor(Color.BLACK);
            TextStroke txt = new TextStroke(ElectionData.dist[i], g.getFont(), false, false);

            g.fill(txt.createStrokedShape(txtarc));

            Color nxt = rain.next();

            g.setColor(new Color(nxt.getRed(), nxt.getGreen(), nxt.getBlue(), 150));
            g.fill(arc);
            startAngle += per;


        }
        g.setColor(Color.white);
        Ellipse2D ci = new Ellipse2D.Double(0, 0, 1000 - maxwidth, 1000 - maxwidth);
        ci.setFrameFromCenter(0, 0, 1000 - (maxwidth + 12), 1000 - (maxwidth + 12));
        g.fill(ci);
        //System.out.println("Dis done");
    }

    public void axis(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.red);
        g.drawLine(0, 0, 0, 30);
        g.setColor(Color.green);
        g.drawLine(0, 0, 30, 0);
        g.setColor(c);
    }

    public void setMax(Graphics2D g) {
        g.setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 9));
        String maxArea = "Dr.Radhakrishnan Nagar";
        fm = g.getFontMetrics();
        maxwidth = fm.stringWidth(maxArea);
    }

    public void drawArea(Graphics2D g) {
        setMax(g);
        g.translate(width / 2, height / 2);
        double inc = Math.PI / ElectionData.locations.length;
        g.setColor(Color.black);
        g.setStroke(new BasicStroke(1f));
        Font f = g.getFont();

        //System.out.println(f.getFontName());
        //g.setFont(new Font("Arial",Font.PLAIN,12));

        double d = 0;

        for (int i = ElectionData.locations.length - 1; i >= 0; i--) {
            AffineTransform t = g.getTransform();
            g.rotate(d);
            g.translate(0, 1000);
            g.rotate(Math.PI / 2);
            g.scale(-1, -1);
            //g.scale(1,-1);
            String label = ElectionData.locations[i];
            int lblwidth = fm.stringWidth(label);
            g.drawString(label, 0 + (maxwidth - lblwidth), 0);
            //g.drawOval( 0,-10,2,2);
            g.setTransform(t);
            d += inc;
        }
        /*for(double d = 0;d<Math.PI;){
        System.out.print(ElectionData.locations[i]);
        AffineTransform t = g.getTransform();
        g.rotate(d);
        g.translate(0, 350);
        //g.rotate(-Math.PI / 2);
        g.drawString("vivek", 0, 0);
        g.setTransform(t);
        d +=ang;
        i++;
        }*/
        System.out.println("drawn polls");
    }

    public Point2D pointAtCircle(double angleInRad, double px, double py, double ox, double oy) {
        double x = Math.cos(angleInRad) * (px - ox) - Math.cos(angleInRad) * (py - oy) + ox;
        double y = Math.sin(angleInRad) * (px - ox) + Math.cos(angleInRad) * (py - oy) + oy;
        return new Point2D.Double(x, y);
    }

    public static void main(String[] arg) {
        AssemblyElectionA a = new AssemblyElectionA();
        a.draw();
        a.repaint();
    }
}

