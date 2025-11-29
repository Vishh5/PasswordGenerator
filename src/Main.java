import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();   // ⭐ Enable modern UI theme
        new PasswordGeneratorGUI();
    }
}
