import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class IndianCenus extends JFrame {

    public Color fillColor = Color.getHSBColor(90,130,199);
    public Color neigh = new Color(239,228,176);
    public float minLum=106f;
    public float maxLum=10f;
    
    public float unitLum=1;
    
    public String[] density = {
        "IN-UP"//16.49]
        , "IN-MH"//9.29]
        , "IN-BR"//8.58]
        , "IN-WB"//7.55]
        , "IN-AP"//7]
        , "IN-MP"//6]
        , "IN-TN"//5.96]
        , "IN-RJ"//5.67]
        , "IN-KA"//5.05]
        , "IN-GJ"//4.99]
        , "IN-OR"//3.47]
        , "IN-KL"//2.76]
        , "IN-JH"//2.72]
        , "IN-AS"//2.58]
        , "IN-PB"//2.29]
        , "IN-CT"//2.11]
        , "IN-HR"//2.09]
        , "IN-DL"//1.38]
        , "IN-JK"//1.04]
        , "IN-UT"//0.84]
        , "IN-HP"//0.57]
        , "IN-TR"//0.3]
        , "IN-ML"//0.24]
        , "IN-MN"//0.22]
        , "IN-NL"//0.16]
        , "IN-GA"//0.12]
        , "IN-AR"//0.11]
        , "IN-PY"//0.1]
        , "IN-CH"//0.09]
        , "IN-MZ"//0.09]
        , "IN-SK"//0.05]
        , "IN-AN"//0.03]
        , "IN-DN"//0.03]
        , "IN-DD"//0.02]
        , "IN-LD"//0.01]
    };
    
    public int index(String id){
        for(int i=0;i<density.length;i++){
            if(density[i].equals(id))
                return i;
        }
        return 0;
    }

    public IndianCenus() {
        this.setSize(900, 900);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setTitle("India Population Density");
    }

    public class GeoPolygon {

        ArrayList<Point2D[]> polygon = new ArrayList<Point2D[]>();
        String name = "";

        public void add(Point2D[] coordinates) {
            polygon.add(coordinates);
        }
    }
    public ArrayList<GeoPolygon> prov = new ArrayList< GeoPolygon>();

    public void extractFeature(Node node) {
        String id = node.getFirstChild().getTextContent();
        GeoPolygon poly = new GeoPolygon();
        poly.name = id;
        //if(!id.startsWith("IN-"))
        // return;
        for (int i = 2; i < node.getChildNodes().getLength(); i++) {
            Point2D[] coordinates = extractPolygon(node.getChildNodes().item(i));
            poly.add(coordinates);
            prov.add(poly);
        }
    }

    public Point2D[] extractPolygon(Node node) {
        if (node.getNodeName().equals("polygon")) {
            NodeList points = node.getChildNodes();
            Point2D[] coordinates = new Point2D[points.getLength()];
            for (int i = 0; i < points.getLength(); i++) {
                double lat = Double.valueOf(points.item(i).getFirstChild().getTextContent());
                double log = Double.valueOf(points.item(i).getLastChild().getTextContent());
                Point2D coordinate = new Point2D.Double(log, lat2y(lat));
                System.out.println(coordinate);
                coordinates[i] = coordinate;
            }
            return coordinates;
        }
        return null;
    }

    public static double lat2y(double aLat) {
        return Math.toDegrees(Math.log(Math.tan(Math.PI / 4 + Math.toRadians(aLat) / 2)));
    }

    public void init() throws ParserConfigurationException, SAXException, IOException {
        unitLum =(maxLum-minLum)/density.length;
        DocumentBuilder docbuild = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = docbuild.parse("IN_PROVINCES.xml");
        NodeList nodes = doc.getElementsByTagName("feature");
        for (int i = 0; i < nodes.getLength(); i++) {
            extractFeature(nodes.item(i));
        }
    }

    public void paintPolyLine(String id, Point2D[] points, Graphics2D g2) {
        GeneralPath path = new GeneralPath();
        path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
        Point2D prev = points[0];
        path.moveTo(points[0].getY(), points[0].getX());//mistake
        for (int i = 1; i < points.length; i++) {
            //Point2D now = points[i];
            //if(!id.startsWith("IN-")){
            //}else{
            path.lineTo(points[i].getY(), points[i].getX());//mistake
            //g2.draw(new Line2D.Double(prev.getY(), prev.getX(), now.getY(), now.getX()));
            //}
            //prev=now;
        }
        path.closePath();
        if (id.equals("__DISPUTED__All")) {
            g2.setColor((new Color(239, 228, 176)).darker());
            g2.fill(path);
        } else if (!id.startsWith("IN-")) {
            //;
           
            g2.setColor(new Color(239, 228, 176));
            g2.fill(path);
           //g2.setColor((new Color(239, 228, 176)).darker());
           //g2.draw(path);

        } else {
            float hue = 90f/240f;
            float sat = 130f/240f;
            float lum=(maxLum+(7*index(id)))/240;//130f/240f;
            System.out.println("l:"+lum);
            System.out.println(index(id)+"--"+id);
            Color fill = Color.getHSBColor(hue, sat, lum);//Color.getHSBColor((90/255), (130/255), (unitLum*index(id)/255));
            g2.setColor(fill);
            g2.fill(path);
            g2.setColor(Color.black);
            g2.draw(path);
        }
    }

    public void paintGeoPolygon(GeoPolygon polygon, Graphics2D g2) {
        for (int i = 0; i < polygon.polygon.size(); i++) {
            Point2D[] points = polygon.polygon.get(i);
            if (points != null) {
                paintPolyLine(polygon.name, points, g2);
            }
        }
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(107, 184, 220));
        g2.fillRect(0, 0, 900, 900);
        int scalefactor = 18;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(-980, 830);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(.04f));
        g2.rotate(-Math.PI / 2);
        g2.scale(1 * scalefactor, 1 * scalefactor);
        for (int i = 0; i < prov.size(); i++) {
            System.out.println("paint " + prov.get(i).name);
            paintGeoPolygon(prov.get(i), g2);
        }
    }

    public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException {
        IndianCenus in = new IndianCenus();
        in.init();

    }
}

