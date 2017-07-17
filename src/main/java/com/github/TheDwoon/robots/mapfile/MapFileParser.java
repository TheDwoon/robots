package com.github.TheDwoon.robots.mapfile;

import com.github.TheDwoon.robots.game.Board;
import com.github.TheDwoon.robots.game.Field;
import com.github.TheDwoon.robots.game.Material;
import com.github.TheDwoon.robots.game.interaction.BoardObserver;
import com.github.TheDwoon.robots.server.UUIDGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by sigmar on 28.05.17.
 */
public class MapFileParser {

    private List<BoardObserver> boardObservers;

    public MapFileParser() {
        boardObservers = new LinkedList<>();
    }

    public MapFileParser(BoardObserver... boardObservers) {
        this();
        for (BoardObserver boardObserver : boardObservers) {
            this.boardObservers.add(boardObserver);
        }
    }

    public MapFileParser(Collection<BoardObserver> boardObservers) {
        this();
        this.boardObservers.addAll(boardObservers);
    }

    public static Board parseBoard(InputStream in) throws ParseException {
    	long uuid = UUIDGenerator.obtainUUID();
    	
    	Scanner scanner = new Scanner(in);
    	Parser parser = new Parser(scanner);
    	
    	return new Board(uuid, parser.parseFields());
    }
    
    public void addBoardObserver(BoardObserver boardObserver) {
        boardObservers.add(boardObserver);
    }

    public void removeBoardObserver(BoardObserver boardObserver) {
        boardObservers.remove(boardObserver);
    }

    public void parse(String filename) throws FileNotFoundException, ParseException {
        parse(new File(filename));
    }

    public void parse(File file) throws FileNotFoundException, ParseException {
        try (Scanner scanner = new Scanner(file)) {
            parse(scanner);
        }
    }

    public void parse(InputStream is) throws ParseException {
        try (Scanner scanner = new Scanner(is)) {
            parse(scanner);
        }
    }

    private void parse(Scanner scanner) throws ParseException {
        long uuid = UUIDGenerator.obtainUUID();
        Field[][] fields = new Parser(scanner).parseFields();
        notifyObserver(uuid, fields);
    }
    
    private static class Parser {

        private int lineCount;
        private Scanner scanner;

        public Parser(Scanner scanner) {
            lineCount = 0;
            this.scanner = scanner;
        }

        private Field[][] parseFields() throws ParseException {
            // parse first line (field size)
            String[] fieldSizeLineSplit = readLine().split("x");
            int width;
            int height;
            try {
                width = Integer.parseInt(fieldSizeLineSplit[0]);
            } catch (NumberFormatException e) {
                throw new ParseException(lineCount, "Invalid width: " + fieldSizeLineSplit[0]);
            }
            try {
                height = Integer.parseInt(fieldSizeLineSplit[1]);
            } catch (NumberFormatException e) {
                throw new ParseException(lineCount, "Invalid height: " + fieldSizeLineSplit[1]);
            }

            // parse material aliases (if present)
            Map<String, Material> materialAliases = new HashMap<>();
            for (Material material : Material.values()) {
                materialAliases.put(material.name(), material);
                materialAliases.put(material.name().toLowerCase(), material);
            }
            for (String line = readLine().trim(); !line.isEmpty(); line = readLine().trim()) {
                String[] aliasSplit = line.split("\\s*[:=]\\s*");
                materialAliases.put(aliasSplit[1], materialAliases.get(aliasSplit[0]));
            }

            // parse field definition
            Field[][] fields = new Field[width][height];
            for (int y = 0; y < height; y++) {
                String line;
                try {
                    line = readLine().trim();
                } catch (ParseException e) {
                    throw new ParseException(lineCount,
                            String.format("Stopped at row %d of %d", y, height));
                }
                String[] fieldLineSplit = line.split("\\s+");

                if (fieldLineSplit.length < width) {
                    throw new NoSuchElementException(
                            String.format("Row %d has %d elements, but %d expected.", y,
                                    fieldLineSplit.length, width));
                }

                for (int x = 0; x < width; x++) {
                    Material material = materialAliases.get(fieldLineSplit[x]);
                    if (material == null) {
                        throw new ParseException(lineCount,
                                "Cannot find mapping for material alias: " + fieldLineSplit[x]);
                    } fields[x][y] = new Field(x, y, material);
                }
            }

            return fields;
        }

        private String readLine() throws ParseException {
            try {
                String line = scanner.nextLine().trim();
                lineCount++;
                return line;
            } catch (NoSuchElementException | IllegalStateException e) {
                throw new ParseException(lineCount, "Input ended unexpectedly", e);
            }
        }

    }

    private void notifyObserver(long uuid, Field[][] fields) {
        for (BoardObserver boardObserver : boardObservers) {
            boardObserver.setSize(uuid, fields.length, fields[0].length);
            for (Field[] column : fields) {
                for (Field field : column) {
                    boardObserver.updateField(uuid, field);
                }
            }
        }
    }

}
