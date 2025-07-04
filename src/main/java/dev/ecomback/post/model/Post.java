package dev.ecomback.post.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
@Document(collection = "eCum-products")
public class Post {
    @Id
    private String id;
    private String name;
    private String imageUrl;
    private int quantity;
    private double sell;
    private double buy;
    private String category;
    private String type;
    private String desc;
    LocalDateTime dateCreated;

    public Post() {
        this.dateCreated = LocalDateTime.now();
    }

    public Post(String category, String type, String desc, double buy, double sell, int quantity, String imageUrl, String name) {
        this();
        this.category = category;
        this.type = type;
        this.desc =  desc;
        this.buy = buy;
        this.sell = sell;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.name = name;
    }


    public void adjust(Adjustment adjustment) {
        if (adjustment.isAdd()) {
            this.quantity += adjustment.getQuantity();
        } else {
            this.quantity -= adjustment.getQuantity();
        }
    }
}
