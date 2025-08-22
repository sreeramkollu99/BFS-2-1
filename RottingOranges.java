import java.util.*;

// Time Complexity : O(m * n). Each cell is processed a constant number of times due to pruning.
// Space Complexity : O(m * n) worst-case recursion stack in a grid with long snake-like paths.
// Did this code successfully run on Leetcode : Yes
// Any problem you faced while coding this : Careful pruning so we don't overwrite with a later (larger) time.
//
// Approach (DFS, multi-source time marking):
// - Treat each initially rotten orange (value 2) as a source with "time" = 2.
//   We use the cell value to store the minute it got rotten: 2 → minute 0, 3 → minute 1, 4 → minute 2, etc.
// - From every rotten source, DFS to its 4 neighbors with time+1,
//   but only propagate if we reach a fresh orange (1) or if we can improve an already-set time (smaller value).
// - After propagation, scan the grid:
//     * If any fresh (1) remains → return -1.
//     * Otherwise, the answer is (maxCellValue - 2) which equals the minutes elapsed.
// - Note: Empty cells (0) are ignored by the pruning condition and never updated.
public class RottingOranges {
    int[][] dirs = {{0, 1}, {-1, 0}, {0, -1}, {1, 0}};
    int m, n;

    public int orangesRotting(int[][] grid) {
        if (grid == null || grid.length == 0) return -1;
        this.m = grid.length;
        this.n = grid[0].length;

        // Start DFS from every initially rotten orange (value = 2)
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 2) {
                    dfs(grid, i, j, 2); // time starts at 2
                }
            }
        }

        // Compute result: track maximum time and check for any remaining fresh oranges
        int maxVal = 2;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) return -1;        // unreachable fresh orange
                maxVal = Math.max(maxVal, grid[i][j]);  // track farthest rotting minute
            }
        }
        return maxVal - 2; // minutes elapsed
    }

    // DFS that spreads "time" to neighbors if it improves their state
    private void dfs(int[][] grid, int i, int j, int time) {
        // bounds
        if (i < 0 || j < 0 || i >= m || j >= n) return;

        // Prune:
        // - If it's empty (0), we don't spread there (0 < time so early return).
        // - If it's already set to a time <= current 'time', no need to update.
        if (grid[i][j] != 1 && grid[i][j] < time) return;

        // Set/update time. This works for:
        // - fresh orange (1) → turns to 'time'
        // - initial rotten (2) at same time (2) → remains 2
        // - any previously set larger time → we keep the smaller (earlier) time
        grid[i][j] = time;

        // Spread to neighbors with time+1
        for (int[] d : dirs) {
            dfs(grid, i + d[0], j + d[1], time + 1);
        }
    }

    // --- Main method for testing ---
    public static void main(String[] args) {
        RottingOranges sol = new RottingOranges();

        int[][] grid1 = {
                {2, 1, 1},
                {1, 1, 0},
                {0, 1, 1}
        };
        System.out.println("Minutes (expected 4): " + sol.orangesRotting(cloneGrid(grid1)));

        int[][] grid2 = {
                {2, 1, 1},
                {0, 1, 1},
                {1, 0, 1}
        };
        System.out.println("Minutes (expected -1): " + sol.orangesRotting(cloneGrid(grid2)));

        int[][] grid3 = {
                {0, 2}
        };
        System.out.println("Minutes (expected 0): " + sol.orangesRotting(cloneGrid(grid3)));

        // Optional: show the final grid state for a case
        int[][] g = {
                {2, 1, 1},
                {1, 1, 0},
                {0, 1, 1}
        };
        int ans = sol.orangesRotting(g);
        System.out.println("Final minutes: " + ans);
        System.out.println("Final grid (time stamps):");
        printGrid(g);
    }

    // Helpers for testing
    private static int[][] cloneGrid(int[][] g) {
        int[][] c = new int[g.length][g[0].length];
        for (int i = 0; i < g.length; i++) c[i] = Arrays.copyOf(g[i], g[i].length);
        return c;
    }

    private static void printGrid(int[][] g) {
        for (int[] row : g) {
            System.out.println(Arrays.toString(row));
        }
    }

}
