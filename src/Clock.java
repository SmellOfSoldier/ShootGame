 class Display {
    private int value;//现在的值
    private int limit;//上限值
    Display( int limit) {
        this.limit = limit;
    }
    public void increase() {
        value++;
        if(value == limit) {
            value = 0;
        }
    }
    public int getValue() {
        return value;
    }
    public static void main(String[] args) {
        Display d = new Display(24);
        for(;;) {
            d.increase();
            System.out.println(d.getValue());
        }
    }
}

public class Clock {
    private Display h = new Display(24);
    private Display min = new Display(60);
    private Display s = new Display(60);

    public void start() {
        for (; ; ) {
            s.increase();
            if (s.getValue() == 0) {//如果分重置，小时+1
                min.increase();
                if (min.getValue() == 0) {//如果分重置，小时+1
                    h.increase();
                }
            }
            System.out.printf("%02d:%02d:%02d\n", h.getValue(), min.getValue(), s.getValue());//格式输出
        }
    }

    public static void main(String[] args) {
        Clock clock = new Clock();
        clock.start();
    }
}