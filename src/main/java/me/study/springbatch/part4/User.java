package me.study.springbatch.part4;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.study.springbatch.part5.Orders;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Enumerated(EnumType.STRING)
    private Level level = Level.NORMAL;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<Orders> orders;

    private LocalDate updatedDate;

    @Builder
    private User(String username, List<Orders> orders) {
        this.username = username;
        this.orders = orders;
    }

    public boolean availableLevelUp() {
        return level.availableLevelUp(getTotalAmount());
    }

    public void levelUp() {
        level = level.getNextLevel(getTotalAmount());
        updatedDate = LocalDate.now();
    }

    private int getTotalAmount() {
        return orders.stream()
                .mapToInt(Orders::getAmount)
                .sum();
    }

    public enum Level {
        VIP(500_000, null),
        GOLD(500_000, VIP),
        SILVER(300_000, GOLD),
        NORMAL(200_000, SILVER);

        private final int nextAmount;
        private final Level nextLevel;

        Level(int nextAmount, Level nextLevel) {
            this.nextAmount = nextAmount;
            this.nextLevel = nextLevel;
        }

        private boolean availableLevelUp(int totalAmount) {
            if (Objects.isNull(nextLevel)) {
                return false;
            }
            return totalAmount >= nextAmount;
        }

        private Level getNextLevel(int totalAmount) {
            if (totalAmount >= Level.VIP.nextAmount) {
                return VIP;
            }
            if (totalAmount >= Level.GOLD.nextAmount) {
                return Level.GOLD.nextLevel;
            }
            if (totalAmount >= Level.SILVER.nextAmount) {
                return Level.SILVER.nextLevel;
            }
            if (totalAmount >= Level.NORMAL.nextAmount) {
                return Level.NORMAL.nextLevel;
            }
            return NORMAL;
        }
    }
}
