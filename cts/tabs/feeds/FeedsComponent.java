package ae.ac.ud.ceit.cts.tabs.feeds;

/**
 * Created by atalla on 21/02/17.
 */

public class FeedsComponent {
    private String title;
    private String subtitle;

    public FeedsComponent(String t, String sub) {
        title = t;
        subtitle = sub;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String s) {

        this.title = s;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String s) {
        this.subtitle = s;
    }
}
