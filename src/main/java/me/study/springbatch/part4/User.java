package me.study.springbatch.part4;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
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

    private int totalAmount;

    private LocalDate updatedDate;

    @Builder
    private User(String username, int totalAmount) {
        this.username = username;
        this.totalAmount = totalAmount;
    }

    public boolean availableLevelUp() {
        return level.availableLevelUp(totalAmount);
    }

    public void levelUp() {
        level = level.getNextLevel(getTotalAmount());
        updatedDate = LocalDate.now();
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
