package be.dafke;

/**
 * User: Dafke
 * Date: 24/02/13
 * Time: 13:20
 */
public interface RefreshableComponent {
    public void refresh();
    public void setVisible(boolean visible);
    public void dispose();
}
