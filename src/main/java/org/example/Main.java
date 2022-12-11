package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    record Operation(AtomicInteger cycles, int value) {

        void reduceCycles() {
            cycles.decrementAndGet();
        }

    }

    final String path = "TODO";

    List<Operation> operations = new ArrayList<>();

    String[][] ui = new String[6][40];

    void run() throws IOException {
        this.readOperations();

        int X = 1;
        int currentCycle = 0;
        Operation operation = this.operations.remove(0);
        while(true) {
            int col = currentCycle % 40;
            int row = (int) Math.floor(currentCycle / 40.0);
            ui[row][col] = (X-1 <= col && X+1 >= col) ? "#" : " ";

            currentCycle++;
            operation.reduceCycles();
            if (operation.cycles.get() == 0) {
                // execute operation
                X += operation.value;
                operation = this.operations.remove(0);
            }

            if (this.operations.isEmpty() || currentCycle >= 239) {
                break;
            }
        }

        for (int i = 0; i < this.ui.length; i++) {
            for (int j = 0; j < this.ui[i].length; j++) {
                String c = this.ui[i][j];
                System.out.print(c);
            }
            System.out.println();
        }
    }

    void readOperations() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(this.path + "input.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                if ("noop".equals(line)) {
                    this.operations.add(new Operation(new AtomicInteger(1), 0));
                    continue;
                }

                String[] parts = line.split(" ");
                int cycles = 2;
                int value = Integer.parseInt(parts[1]);
                this.operations.add(new Operation(new AtomicInteger(cycles), value));
            }
        }
    }
}