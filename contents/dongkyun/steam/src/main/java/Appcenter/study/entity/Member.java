package Appcenter.study.entity;

import Appcenter.study.dto.req.UpdateEmailRequest;
import Appcenter.study.dto.req.UpdateMypageRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 30)
    private String location;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false)
    private Long wallet;

    @Column(name = "profile_url", length = 250)
    private String profileUrl;

    @OneToMany(mappedBy = "member1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chatroom> chatroomsAsMember1 = new ArrayList<>();

    @OneToMany(mappedBy = "member2", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chatroom> chatroomsAsMember2 = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts = new ArrayList<>();

    public void addCart(Cart cart) {
        carts.add(cart);
        cart.setMember(this);
    }

    public void updateMypage(UpdateMypageRequest updateMypageRequest) {
        this.nickname = updateMypageRequest.getNickname();
        this.location = updateMypageRequest.getLocation();
        this.profileUrl = updateMypageRequest.getProfileUrl();
    }

    public void updateEmail(UpdateEmailRequest updateEmailRequest) {
        this.email = updateEmailRequest.getEmail();
    }

    public List<Chatroom> getChatrooms() {
        List<Chatroom> chatrooms = new ArrayList<>();
        if (chatroomsAsMember1 != null) {
            chatrooms.addAll(chatroomsAsMember1);
        }
        if (chatroomsAsMember2 != null) {
            chatrooms.addAll(chatroomsAsMember2);
        }
        return Collections.unmodifiableList(chatrooms);
    }
    public List<Review> getReviews() {
        return Collections.unmodifiableList(reviews);
    }
    public List<Purchase> getPurchases() {
        return Collections.unmodifiableList(purchases);
    }
    public List<Cart> getCarts() {
        return Collections.unmodifiableList(carts);
    }
}
