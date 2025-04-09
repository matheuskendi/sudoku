package br.com.dio.Service;

import br.com.dio.model.Board;
import br.com.dio.model.GameStatusEnum;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardService {
    private final static int BOARD_LIMIT = 9;

    private final Board board;

    public BoardService(final Map<String, String> gameConfig){
        this.board = new Board(initBoard(gameConfig));
    }

    public List<List<Space>> getSpaces() {
        return this.board.getSpaces();
    }

    public void reset(){
        this.board.reset();
    }

    public boolean hasErrors(){
        return board.hasErrors();
    }

    public GameStatusEnum getStatus(){
        return board.getStatus();
    }

    public boolean gameIsFinished(){
        return board.gameIsFinished();
    }



    private List<List<Space>> initBoard(Map<String,String> gameConfig) {
        // Cria a estrutura de espaços do tabuleiro, representando cada célula.
            List<List<Space>> spaces = new ArrayList<>();
            for(int i = 0; i < BOARD_LIMIT; i++){
                spaces.add(new ArrayList<>());
                for (int j = 0; j < BOARD_LIMIT; j++){
                    // Obtém a configuração da posição no mapa, como valores esperados e fixados.
                    var positionConfig = gameConfig.get("%s,%s".formatted(i,j));
                    // Divide a configuração e mapeia os valores.
                    var expected = Integer.parseInt(positionConfig.split(",")[0]);
                    var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                    // Cria uma nova instância de Space e adiciona à estrutura.
                    var currentSpace = new Space(expected, fixed);
                    spaces.get(i).add(currentSpace);
                }
            }
            return spaces;

    }
}
