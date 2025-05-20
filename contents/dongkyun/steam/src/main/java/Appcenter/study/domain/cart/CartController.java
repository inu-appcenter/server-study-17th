package Appcenter.study.domain.cart;

import Appcenter.study.global.security.jwt.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController implements CartApiSpecification {

    private final CartService cartService;

    @PostMapping("/{gameId}")
    public ResponseEntity<CartResponse> addCart(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long gameId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addCart(userDetails, gameId));
    }
}
