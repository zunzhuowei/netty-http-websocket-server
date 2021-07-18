package com.hbsoo.game.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.*;

/**
 *  游戏房间
 * Created by zun.wei on 2021/7/18.
 */
@Data
@Accessors(chain = true)
public class GameRoom implements Serializable {

    /**
     * 房间编号
     */
    private Long no;

    /**
     * 房间名称
     */
    private String name;

    /**
     * 玩家
     */
    private Set<Player> players;

    /**
     * 牌
     */
    private List<Card> cards;

    /**
     * 发牌者
     */
    private CardSender cardSender;

    /**
     * 已出的牌
     */
    private Map<Player, List<Card>> discardCards;

    /**
     * 手上的牌
     */
    private Map<Player, List<Card>> handCards;

    /**
     * 最后一次出的牌
     */
    private Map<Player, List<Card>> lastDiscardCards;


    public GameRoom(Long no, String name) {
        this.cardSender = new CardSender();
        this.cards = new ArrayList<>();
        this.cards.addAll(Arrays.asList(
                Card.card("A", 1), Card.card("A", 2), Card.card("A", 3), Card.card("A", 4),
                Card.card("2", 1), Card.card("2", 2), Card.card("2", 3), Card.card("2", 4),
                Card.card("3", 1), Card.card("3", 2), Card.card("3", 3), Card.card("3", 4),
                Card.card("4", 1), Card.card("4", 2), Card.card("4", 3), Card.card("4", 4),
                Card.card("5", 1), Card.card("5", 2), Card.card("5", 3), Card.card("5", 4),
                Card.card("6", 1), Card.card("6", 2), Card.card("6", 3), Card.card("6", 4),
                Card.card("7", 1), Card.card("7", 2), Card.card("7", 3), Card.card("7", 4),
                Card.card("8", 1), Card.card("8", 2), Card.card("8", 3), Card.card("8", 4),
                Card.card("9", 1), Card.card("9", 2), Card.card("9", 3), Card.card("9", 4),
                Card.card("10", 1), Card.card("10", 2), Card.card("10", 3), Card.card("10", 4),
                Card.card("J", 1), Card.card("J", 2), Card.card("J", 3), Card.card("J", 4),
                Card.card("Q", 1), Card.card("Q", 2), Card.card("Q", 3), Card.card("Q", 4),
                Card.card("K", 1), Card.card("K", 2), Card.card("K", 3), Card.card("K", 4),
                Card.card("KING", 1), Card.card("KING", 2)
        ));

        //随机排序
        Collections.shuffle(this.cards);
    }

    /**
     * 打牌
     */
    public void playerCard() {

    }

}
