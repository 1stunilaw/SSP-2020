package ssp.marketplace.app.entity;

import lombok.*;
import ssp.marketplace.app.entity.statuses.*;
import ssp.marketplace.app.entity.user.User;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "comment")
@Getter
@Setter
@RequiredArgsConstructor
public class Comment extends BasicEntity{
    @Enumerated(EnumType.STRING)
    private StatusForComment status;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "replies",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id"))
    private List<Comment> answers;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "replies",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private Comment question;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private CommentAccessLevel accessLevel;
}