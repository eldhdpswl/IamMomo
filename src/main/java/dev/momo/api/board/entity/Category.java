package dev.momo.api.board.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;
    private String category;

    protected Category() {

    }

    @Builder
    public Category(Long categoryId, String category) {
        this.categoryId = categoryId;
        this.category = category;
       // this.questionList = questionList;
    }
}
