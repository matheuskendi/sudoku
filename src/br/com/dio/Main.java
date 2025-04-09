package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board ;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        final var positions = Stream.of(args)
                .collect(Collectors.toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                ));
        var option  = 1;
        while (true){
            System.out.println("Selecione uma das opçoes a seguir");
            System.out.println("1 - Iniciar um novo jogo");
            System.out.println("2 - Colocar um novo numero");
            System.out.println("3 - Remover um novo numero");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - Limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            option = scanner.nextInt();

            switch (option){
                case 1 ->   startGame(positions);
                case 2 ->   inputNumber();
                case 3 ->   removeNumber();
                case 4 ->   showCurrentGame();
                case 5 ->   showGameStatus();
                case 6 ->   clearGame();
                case 7 ->   finishGame();
                case 8 ->   System.exit(0);
                default -> System.out.println("Opção invalida, selecione uma das opções do menu");
            }
        }

    }

    //Metodo responsável por iniciar o jogo. Aceita um mapa de posições configuradas.
    private static void startGame(Map<String, String> positions) {
        if(nonNull(board)){
            System.out.println("O jogo ja foi iniciado");
            return;
        }
        // Cria a estrutura de espaços do tabuleiro, representando cada célula.
        List<List<Space>> spaces = new ArrayList<>();
        for(int i = 0; i < BOARD_LIMIT; i++){
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++){

                // Obtém a configuração da posição no mapa, como valores esperados e fixados.
                var positionConfig = positions.get("%s,%s".formatted(i,j));

                // Divide a configuração e mapeia os valores.
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);

                // Cria uma nova instância de Space e adiciona à estrutura.
                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }
        // Inicializa o tabuleiro com a configuração gerada.
        board = new Board(spaces);
        System.out.println("o jogo esta pronto para começar");
    }

    // Metodo responsável por inserir um número em uma posição do tabuleiro.
    private static void inputNumber() {
        // Verifica se o jogo já foi iniciado. Se o tabuleiro for nulo, exibe uma mensagem e encerra o metodo.
        if (isNull(board)){
            System.out.println("O jogo não foi iniciado");
            return;
        }
        // Solicita ao jogador que informe a coluna onde o número será inserido.
        System.out.println("Informe a coluna em que o numero sera inserido");
        var col = runUntiGetValidNumber(8,8);

        // Solicita ao jogador que informe a linha onde o número será inserido.
        System.out.println("Informe a linha em que o numero sera inserido");
        var row = runUntiGetValidNumber(8,8);

        // Solicita ao jogador que insira o número desejado para a posição escolhida.
        System.out.printf("Informe o número que vai entrar na posição [%s,%s]\n", col, row);
        var value = runUntiGetValidNumber(1,9);

        // Tenta alterar o valor na posição informada. Caso falhe, exibe mensagem indicando que o espaço é fixo.
        if (!board.changeValue(col, row, value)){
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
        }

    }

    // Metodo responsável por remover um número em uma posição do tabuleiro.
    private static void removeNumber() {
        // Verifica se o jogo já foi iniciado. Se o tabuleiro for nulo, exibe uma mensagem e encerra o metodo.
        if (isNull(board)){
            System.out.println("O jogo não foi iniciado");
            return;
        }

        // Solicita ao jogador que informe a coluna onde o número será removido.
        System.out.println("Informe a coluna em que o numero sera REMOVIDO");
        var col = runUntiGetValidNumber(8,8);

        // Solicita ao jogador que informe a linha onde o número será removido.
        System.out.println("Informe a linha em que o numero sera REMOVIDO");
        var row = runUntiGetValidNumber(8,8);

        // Solicita ao jogador que insira o número que será removido da posição escolhida.
        System.out.printf("Informe o número que vai ser REMOVIDO da posição [%s,%s]\n", col, row);

        // Tenta remover o valor na posição informada. Caso falhe, exibe mensagem indicando que o espaço é fixo.
        if (!board.clearValue(col, row)){
            System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
        }

    }
    // Verifica se o jogo já foi iniciado. Se o tabuleiro for nulo, exibe uma mensagem e encerra o metodo.
    private static void showCurrentGame() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        // Cria um array para armazenar os argumentos que serão usados na formatação do tabuleiro.
        var args = new Object[81];
        var argPos = 0;// Inicializa o índice para preencher o array de argumentos.
        // Itera sobre as linhas do tabuleiro.
        for (int i = 0; i < BOARD_LIMIT; i++){
            // Para cada coluna (ou espaço) no tabuleiro, obtém os valores a serem exibidos.
            for (var col: board.getSpaces()){
                // Insere no array args o valor atual da célula ou um espaço vazio se o valor for nulo.
                args[argPos ++] = " " + (isNull((col.get(i).getActual())) ? "" : col.get(i).getActual());
            }
        }
        // Exibe o tabuleiro formatado, preenchendo o template com os valores do array args.
        System.out.println("Seu jogo se encontra da seguinte forma");
        System.out.printf((BOARD_TEMPLATE) + "\n", args );

        // Verifica se o tabuleiro contém erros e informa ao usuário.
        if(board.hasErrors()){
            System.out.println("O jogo contém erros");
        }else{
            System.out.println("O jogo não contem erros");
        }

    }

    private static void showGameStatus() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        System.out.printf("O jogo atualmente se encontra no status %s\n", board.getStatus());

    }

    private static void clearGame() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.println("Tem certeza que deseja limpar");
        var confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("sim") || !confirm.equalsIgnoreCase("nao")){
            System.out.println("Informe 'sim' ou 'nao'");
            confirm = scanner.next();
        }
        if(confirm.equalsIgnoreCase("sim")){
            board.reset();
        }
    }



    private static void finishGame() {
        if (isNull(board)){
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        if (board.gameIsFinished());
    }

    private static int runUntiGetValidNumber(final int min, final int max) {
        var current = scanner.nextInt();
        while(current < min || current > max){
            System.out.println("Informe um número entre %s e %s");
            current = scanner.nextInt();
        }
        return current;
    }
}

//0,0;4,false 1,0;7,false 2,0;9,true 3,0;5,false 4,0;8,true 5,0;6,true 6,0;2,true 7,0;3,false 8,0;1,false 0,1;1,false 1,1;3,true 2,1;5,false 3,1;4,false 4,1;7,true 5,1;2,false 6,1;8,false 7,1;9,true 8,1;6,true 0,2;2,false 1,2;6,true 2,2;8,false 3,2;9,false 4,2;1,true 5,2;3,false 6,2;7,false 7,2;4,false 8,2;5,true 0,3;5,true 1,3;1,false 2,3;3,true 3,3;7,false 4,3;6,false 5,3;4,false 6,3;9,false 7,3;8,true 8,3;2,false 0,4;8,false 1,4;9,true 2,4;7,false 3,4;1,true 4,4;2,true 5,4;5,true 6,4;3,false 7,4;6,true 8,4;4,false 0,5;6,false 1,5;4,true 2,5;2,false 3,5;3,false 4,5;9,false 5,5;8,false 6,5;1,true 7,5;5,false 8,5;7,true 0,6;7,true 1,6;5,false 2,6;4,false 3,6;2,false 4,6;3,true 5,6;9,false 6,6;6,false 7,6;1,true 8,6;8,false 0,7;9,true 1,7;8,true 2,7;1,false 3,7;6,false 4,7;4,true 5,7;7,false 6,7;5,false 7,7;2,true 8,7;3,false 0,8;3,false 1,8;2,false 2,8;6,true 3,8;8,true 4,8;5,true 5,8;1,false 6,8;4,true 7,8;7,false 8,8;9,false