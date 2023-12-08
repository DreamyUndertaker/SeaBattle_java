package FinalProject;

import java.util.Scanner;

public class BattleshipGame {
    private static final int BOARD_SIZE = 10;
    private static final char EMPTY_CELL = '-';
    private static final char SHIP_CELL = 'S';
    private static final char HIT_CELL = 'X';
    private static final char MISS_CELL = 'O';

    private char[][] player1Board;
    private char[][] player2Board;

    public BattleshipGame() {
        player1Board = new char[BOARD_SIZE][BOARD_SIZE];
        player2Board = new char[BOARD_SIZE][BOARD_SIZE];
        initializeBoards();
    }

    private void initializeBoards() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                player1Board[i][j] = EMPTY_CELL;
                player2Board[i][j] = EMPTY_CELL;
            }
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Игрок 1, разместите корабли на поле:");
        placeShips(player1Board, scanner);

        System.out.println("Игрок 2, разместите корабли на поле:");
        placeShips(player2Board, scanner);

        boolean gameOver = false;
        int currentPlayer = 1;

        while (!gameOver) {
            int opponentPlayer = currentPlayer == 1 ? 2 : 1;
            char[][] currentPlayerBoard = currentPlayer == 1 ? player1Board : player2Board;
            char[][] opponentPlayerBoard = currentPlayer == 1 ? player2Board : player1Board;

            System.out.println("Ход игрока " + currentPlayer);
            printBoard(opponentPlayerBoard);
            System.out.println("Введите координаты в формате 'строка столбец':");

            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (isValidMove(row, col)) {
                if (opponentPlayerBoard[row][col] == EMPTY_CELL) {
                    System.out.println("Мимо!");
                    currentPlayerBoard[row][col] = MISS_CELL;
                } else if (opponentPlayerBoard[row][col] == SHIP_CELL) {
                    System.out.println("Попадание!");
                    currentPlayerBoard[row][col] = HIT_CELL;
                    if (isSunk(opponentPlayerBoard, row, col)) {
                        System.out.println("Корабль потоплен!");
                        if (isGameOver(opponentPlayerBoard)) {
                            System.out.println("Игрок " + currentPlayer + " победил!");
                            gameOver = true;
                        }
                    }
                }
            } else {
                System.out.println("Недопустимый ход. Повторите ввод.");
            }

            currentPlayer = opponentPlayer;
        }

        scanner.close();
    }

    private void placeShips(char[][] board, Scanner scanner) {
        int[] shipLengths = { 1, 1, 1, 1, 2, 2, 2, 3, 3, 4 };
        for (int length : shipLengths) {
            printBoard(board);
            System.out.println("Разместите корабль длиной " + length + " клеток.");
            System.out.println("Введите координаты начальной точки в формате 'строка столбец':");
            int startRow = scanner.nextInt();
            int startCol = scanner.nextInt();
            System.out.println("Выберите направление размещения (1 - горизонтальное, 2 - вертикальное):");
            int direction = scanner.nextInt();

            if (isValidMove(startRow, startCol) && isValidPlacement(board, startRow, startCol, length, direction)) {
                placeShip(board, startRow, startCol, length, direction);
            } else {
                System.out.println("Недопустимое размещение. Повторите ввод.");
                length--; // Повторяем попытку для корабля с той же длиной
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    private boolean isValidPlacement(char[][] board, int startRow, int startCol, int length, int direction) {
        int endRow = startRow;
        int endCol = startCol;

        if (direction == 1) {
            endCol += length - 1; // Горизонтальное размещение
        } else {
            endRow += length - 1; // Вертикальное размещение
        }

        if (isValidMove(endRow, endCol)) {
            for (int i = startRow; i <= endRow; i++) {
                for (int j = startCol; j <= endCol; j++) {
                    if (board[i][j] != EMPTY_CELL) {
                        return false; // Найдена занятая клетка
                    }
                }
            }
            return true;
        }

        return false; // Некорректные координаты
    }

    private void placeShip(char[][] board, int startRow, int startCol, int length, int direction) {
        int endRow = startRow;
        int endCol = startCol;

        if (direction == 1) {
            endCol += length - 1; // Горизонтальное размещение
        } else {
            endRow += length - 1; // Вертикальное размещение
        }

        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                board[i][j] = SHIP_CELL;
            }
        }
    }

    private boolean isSunk(char[][] board, int row, int col) {
        char shipCell = board[row][col];

        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        for (int[] direction : directions) {
            int currentRow = row + direction[0];
            int currentCol = col + direction[1];

            while (isValidMove(currentRow, currentCol)
                    && (board[currentRow][currentCol] == shipCell || board[currentRow][currentCol] == HIT_CELL)) {
                if (board[currentRow][currentCol] == shipCell) {
                    return false; // Еще не все клетки корабля потоплены
                }
                currentRow += direction[0];
                currentCol += direction[1];
            }
        }

        return true;
    }

    private boolean isGameOver(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == SHIP_CELL) {
                    return false; // Найден не потопленный корабль
                }
            }
        }
        return true;
    }

    private void printBoard(char[][] board) {
        System.out.println("  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        BattleshipGame game = new BattleshipGame();
        game.play();
    }
}