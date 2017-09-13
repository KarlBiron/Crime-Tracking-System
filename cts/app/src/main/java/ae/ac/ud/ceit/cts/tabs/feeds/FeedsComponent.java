package ae.ac.ud.ceit.cts.tabs.feeds;

/**
 * Created by atalla on 21/02/17. (I added this comment)
 */

//a (class) is an extensible program-code-template for creating objects,
// providing initial values for state (member variables) and
// implementations of behavior (member functions or methods).
public class FeedsComponent {
    private String title;       //declare a string called "title"
    private String subtitle;    //declare a string called "subtitle"

    public FeedsComponent(String t, String sub) {   //creating a "FeedsComponent" method with 2 String(t,sub) parameters
        title = t;
        subtitle = sub;
    }


    public String getTitle() {return title;}    //sets the title and the subtitle for the current component

    public void setTitle(String s) {             //this method is never used

        this.title = s;
    }

    public String getSubtitle() {
        return subtitle;
    }           //this method is never used

    public void setSubtitle(String s) {
        this.subtitle = s;
    }   //this method is never used
}
