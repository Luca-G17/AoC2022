public class Monkey {
    private final String name;
    private final int type;
    private long value;
    private String operation;
    private String[] params;

    public Monkey(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public void setValue(long value) { this.value = value; }
    public void setOperation(String operation, String[] params) {
        this.operation = operation;
        this.params = params;
    }
    public long performOperation(long v1, long v2) {
        if (type == 0) {
            return value;
        } else {
            switch (operation) {
                case "+" -> { return v1 + v2; }
                case "-" -> { return v1 - v2; }
                case "/" -> { return v1 / v2; }
                case "*" -> { return v1 * v2; }
            }
        }
        return -1;
    }
    public int getType() { return type; }
    public long getValue() { return value; }
    public String[] getParams() { return params; }
    public String getName() { return name; }
    public String getOperation() { return operation; }
}
