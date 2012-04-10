import java.awt.Color;

public class RedHue {

    public Color[] colors = {
        Color.WHITE,
        new Color(255, 255, 255),
        new Color(225, 235, 214),
        new Color(244, 203, 173),
       // new Color(234, 157, 100),
        new Color(235, 169, 135),
        new Color(225, 132, 101),
        new Color(213, 93, 69),
        new Color(205, 53, 40),
        new Color(196, 10, 11),
        new Color(180, 0, 0),
        new Color(119, 0, 0),
        
    };
    public int i = 0;

    public RedHue() {
    }

    public Color next() {
        System.out.println(i + "i" + colors.length);
        if (i < colors.length - 1) {
            System.out.println("in");
            ++i;
        } else {
            i = 0;
        }
        return colors[i];
    }
}

