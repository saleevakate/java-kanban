package Manager;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import Tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskHistoryTest {
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;
    private Subtask subtask2;

    @BeforeEach
    public void setUp() {
        task1 = new Task(1, "Имя1", "Описание 1", TaskStatus.NEW);
        task2 = new Task(2, "Имя2", "Описание 2", TaskStatus.NEW);
        epic1 = new Epic(3, "Эпик1", "Описание1");
        epic2 = new Epic(4, "Эпик2", "Описание2");
        subtask1 = new Subtask(5, "Сабтаск", "Описание", 3, TaskStatus.NEW);
        subtask2 = new Subtask(6, "Сабтаск", "Описание", 3, TaskStatus.NEW);
    }

    //экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void testTaskEqual() {
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2);
    }

    //экземпляры класса Epic равны друг другу, если равен их id
    @Test
    public void testEpicEqual() {
        epic1.setId(2);
        epic2.setId(2);
        assertEquals(epic1, epic2);
    }

    //экземпляры класса Subtask равны друг другу, если равен их id
    @Test
    public void testSubtaskEqual() {
        subtask1.setId(3);
        subtask2.setId(3);
        assertEquals(subtask1, subtask2);
    }

    //Объект epic нельзя добавить в самого себя в виде подзадачи
    @Test
    public void testEpicCannotBeSubtask() {

    }

    //объект Subtask нельзя сделать своим же эпиком
    @Test
    public void testSubtaskCannotBeEpic() {

    }

    //
}
