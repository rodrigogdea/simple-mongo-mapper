package org.smp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.smm.*;
import org.smp.model.ChessBoard;

import com.mongodb.BasicDBObject;

public class SimpleFieldBindingTest extends SimpleMongoMapperTest {

    private static final String CHESS_BOARD_COLLECTION_NAME = "ChessBoard";
    private SimpleMongoMapper mongoMapper;

    @Before
    public void setUp() {
        initMongoMapper();
    }

    @Test
    public void testSaveASimpleEntity() {
        String bobbyFischer = "Bobby Fischer";
        String garryKasparov = "Garry Kasparov";
        ChessBoard chessBoard = new ChessBoard(bobbyFischer, garryKasparov);

        this.mongoMapper.save(CHESS_BOARD_COLLECTION_NAME, chessBoard, smpDb);

        ChessBoard aFoundBoard = this.mongoMapper.findOne(CHESS_BOARD_COLLECTION_NAME,
                new BasicDBObject("whiteName", bobbyFischer), smpDb);

        assertEquals(chessBoard, aFoundBoard);
    }

    @Test
    public void objectNotFound() {

        try {
            this.mongoMapper.findOne(CHESS_BOARD_COLLECTION_NAME, new BasicDBObject("witheName", "Bobby Fischer"), smpDb);
            fail();
        } catch (ObjectNotFoundException e) {
        }

    }

    @Test
    public void testSaveASimpleEntityWithAMapWithPrimitiveTypes() {
        String bobbyFischer = "Bobby Fischer";
        String garryKasparov = "Garry Kasparov";
        ChessBoard chessBoard = new ChessBoard(bobbyFischer, garryKasparov);

        this.mongoMapper.save(CHESS_BOARD_COLLECTION_NAME, chessBoard, smpDb);

        ChessBoard aFoundBoard = this.mongoMapper.findOne(CHESS_BOARD_COLLECTION_NAME,
                new BasicDBObject("whiteName", bobbyFischer), smpDb);

        assertEquals("BR", aFoundBoard.getPieceOnPosition("A1"));
        assertTrue(aFoundBoard.getMoves().contains("G1-F3,B8-C6"));
    }

    private void initMongoMapper() {
        mongoMapper = new SimpleMongoMapper();
        mongoMapper.addEntityMapper(CHESS_BOARD_COLLECTION_NAME, buildChessBoardMapper());
    }

    private EntityMapper<ChessBoard> buildChessBoardMapper() {
        SimpleEntityMapper<ChessBoard> entityMapper = new SimpleEntityMapper<>(ChessBoard.class);
        entityMapper.addBinder(new SimpleFieldBinder<>(ChessBoard.class, "whiteName", true, String.class));
        entityMapper.addBinder(new SimpleFieldBinder<>(ChessBoard.class, "blackName", true, String.class));
        entityMapper.addBinder(new SimpleFieldBinder<>(ChessBoard.class, "simpleBoard"));
        entityMapper.addBinder(new SimpleFieldBinder<>(ChessBoard.class, "moves"));
        return entityMapper;
    }

}
