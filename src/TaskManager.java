public class TaskManager {
    private static int idCounter = 0;

    private static int generateIdTask() {
        return idCounter++;
    }
}
