package manager;

public class Managers {

    public static FileBackedTaskManager getDefaultManager() {
        return new FileBackedTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryTaskHistory();
    }
}
