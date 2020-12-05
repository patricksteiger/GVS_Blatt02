package exercise2.model;

/******************************************************************************
 *
 * Patrick Steiger und Annalisa Degenhard, 04.12.2020
 *
 *****************************************************************************/


//Model of each post containing publisher and content
public class Post {
    String content;
    User publisher;

    public Post(User publisher, String content){
        this.publisher=publisher;
        this.content= content;
    }

    public User getPublisher() {
        return publisher;
    }

    public String getContent() {
        return content;
    }
}
