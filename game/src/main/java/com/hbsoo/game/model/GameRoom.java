package com.hbsoo.game.model;

import com.hbsoo.game.protocol.GameProtocol;
import com.hbsoo.game.utils.MessageUtils;
import com.hbsoo.protobuf.exception.GlobalExceptionProcessor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 游戏房间
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
     * 玩家，key 玩家id（用户id），value 玩家
     */
    private Map<Long, Player> players = new ConcurrentHashMap<>(8);

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


    private GameRoom(){}

    public GameRoom(Long no, String name) {
        this.no = no;
        this.name = name;
        this.cardSender = new CardSender();
        this.cards = new ArrayList<>(60);
        this.cards.addAll(Arrays.asList(
                Card.card("3", 1), Card.card("3", 1), Card.card("3", 1), Card.card("3", 1),
                Card.card("4", 2), Card.card("4", 2), Card.card("4", 2), Card.card("4", 2),
                Card.card("5", 3), Card.card("5", 3), Card.card("5", 3), Card.card("5", 3),
                Card.card("6", 4), Card.card("6", 4), Card.card("6", 4), Card.card("6", 4),
                Card.card("7", 5), Card.card("7", 5), Card.card("7", 5), Card.card("7", 5),
                Card.card("8", 6), Card.card("8", 6), Card.card("8", 6), Card.card("8", 6),
                Card.card("9", 7), Card.card("9", 7), Card.card("9", 7), Card.card("9", 7),
                Card.card("10", 8), Card.card("10", 8), Card.card("10", 8), Card.card("10", 8),
                Card.card("J", 9), Card.card("J", 9), Card.card("J", 9), Card.card("J", 9),
                Card.card("Q", 10), Card.card("Q", 10), Card.card("Q", 10), Card.card("Q", 10),
                Card.card("K", 11), Card.card("K", 11), Card.card("K", 11), Card.card("K", 11),
                Card.card("A", 12), Card.card("A", 12), Card.card("A", 12), Card.card("A", 12),
                Card.card("2", 13), Card.card("2", 13), Card.card("2", 13), Card.card("2", 13),
                Card.card("KING", 14), Card.card("KING", 14)
        ));
        //随机排序
        Collections.shuffle(this.cards);
    }

    /**
     * 打牌
     */
    public void playerCard() {

    }

    /**
     * 添加玩家
     * @param player 玩家
     * @return 错误码
     */
    public synchronized boolean addPlayer(Player player) {
        if (Objects.isNull(player)) {
            throw GlobalExceptionProcessor.getInstance
                    (MessageUtils.commonWebSocketResp(GameProtocol.RespCode.FAIL, "玩家为空"));
        }
        if (players.size() >= 3) {
            throw GlobalExceptionProcessor.getInstance
                    (MessageUtils.commonWebSocketResp(GameProtocol.RespCode.FAIL, "房间已经满了"));
        }
        final Long id = player.getId();
        final boolean containsKey = players.containsKey(id);
        if (containsKey) {
            throw GlobalExceptionProcessor.getInstance
                    (MessageUtils.commonWebSocketResp(GameProtocol.RespCode.FAIL, "玩家已经在房间中"));
        }
        players.putIfAbsent(id, player);
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameRoom gameRoom = (GameRoom) o;
        return Objects.equals(no, gameRoom.no);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no);
    }
}
